package solvers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit un solveur permettant de vérifier l'arc-cohérence de domaines de variables en fonction des contraintes.
 */
public class MACSolver extends AbstractSolver {
    /**
     * Constructeur par défaut.
     * @param variables ensemble de variables
     * @param constraints ensemble de contraintes
     */
    public MACSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for(Variable variable: this.variables){
            Set<Object> domain = new HashSet<>();
            domain.addAll(variable.getDomain());
            domains.put(variable, domain);
        }
        return this.mac(domains, new HashMap<>());
    }
    
    public Map<Variable, Object> mac(Map<Variable, Set<Object>> domains, Map<Variable, Object> instanciation){
        ArcConsistency arc = new ArcConsistency(this.constraints);
        arc.enforceArcConsistency(domains);

        // P inconsistant et ayant un domain vide
        if(!this.isConsistent(instanciation)){
            for(Set<Object> domain: domains.values()){
                if(domain.size() == 0){
                    return null;
                }
            }
        }

        // toutes les variables sont instanciées
        if(instanciation.keySet().size() == this.variables.size()){
            return instanciation;
        }

        // choisi la dernière variable parmi celle non instanciées
        Variable variableNotInstanciated = null;
        for(Variable variable: this.variables){
            if(!instanciation.containsKey(variable)){
                variableNotInstanciated = variable;
            }
        }

        for(Object value: domains.get(variableNotInstanciated)){
            Set<Object> newDomainForVariable = new HashSet<>();
            newDomainForVariable.add(value);
            
            Set<Object> newDomainForVariable2 = new HashSet<>();
            for(Object value2: variableNotInstanciated.getDomain()){
                if(!value.equals(value2)){
                    newDomainForVariable2.add(value2);
                }
            }
            newDomainForVariable2.addAll(newDomainForVariable);

            Map<Variable, Object> instanciation2 = new HashMap<>();
            instanciation2.putAll(instanciation);
            instanciation2.put(variableNotInstanciated, value);

            instanciation2 = this.mac(domains, instanciation2);
            if(instanciation2 != null){
                return instanciation2;
            }
        }
        return null;
    } 
}
