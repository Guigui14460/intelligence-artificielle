package solvers;

import java.util.Arrays;
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
            domains.put(variable, new HashSet<>(variable.getDomain()));
        }
        return this.mac(domains, new HashMap<>());
    }
    
    public Map<Variable, Object> mac(Map<Variable, Set<Object>> domains, Map<Variable, Object> instanciation){
        ArcConsistency arc = new ArcConsistency(this.constraints);
        arc.enforceArcConsistency(domains);

        // vérifie si P inconsistant et ayant un domaine vide
        if(!this.isConsistent(instanciation)){
            for(Set<Object> domain: domains.values()){
                if(domain.size() == 0){
                    return null;
                }
            }
        }

        // vérifie si toutes les variables sont instanciées
        if(instanciation.keySet().containsAll(this.variables)){
            return instanciation;
        }

        // choisi une variable parmi celle non instanciées
        Variable variableNotInstanciated = null;
        for(Variable variable: this.variables){
            if(!instanciation.containsKey(variable)){
                variableNotInstanciated = variable;
                break;
            }
        }

        for(Object value: domains.get(variableNotInstanciated)){
            Set<Object> newDomainForVariable = new HashSet<>(Arrays.asList(value));

            Map<Variable, Set<Object>> domains2 = new HashMap<>(domains);
            domains2.remove(variableNotInstanciated);
            domains2.put(variableNotInstanciated, newDomainForVariable);

            // copie pour garder le contexte
            Map<Variable, Object> instanciation2 = new HashMap<>(instanciation);
            instanciation2.put(variableNotInstanciated, value);

            // récusivité de l'algo
            instanciation2 = this.mac(domains, instanciation2);
            if(instanciation2 != null){
                return instanciation2;
            }
        }
        return null;
    } 
}
