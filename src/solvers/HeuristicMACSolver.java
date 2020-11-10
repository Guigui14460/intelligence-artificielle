package solvers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit un solveur permettant de résoudre un problème au sens de
 * l'heuristique.
 */
public class HeuristicMACSolver extends AbstractSolver {
    /**
     * Heuristique sur les variables.
     */
    private final VariableHeuristic variableHeuristic;

    /**
     * Heuristique sur les valeurs.
     */
    private final ValueHeuristic valueHeuristic;

    /**
     * Constructeur par défaut.
     * 
     * @param variables         ensemble de variables
     * @param constraints       ensemble de contraintes
     * @param variableHeuristic heuristique sur les variables
     * @param valueHeuristic    heuristique sur les valeurs
     */
    public HeuristicMACSolver(Set<Variable> variables, Set<Constraint> constraints, VariableHeuristic variableHeuristic,
            ValueHeuristic valueHeuristic) {
        super(variables, constraints);
        this.variableHeuristic = variableHeuristic;
        this.valueHeuristic = valueHeuristic;
    }

    @Override
    public Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for (Variable var : this.variables) {
            domains.put(var, var.getDomain());
        }
        return this.macHeuristic(domains, new HashMap<>());
    }

    /**
     * Algorithme Maintaining Arc Consistency (MAC) avec des heuristics. Permet
     * d'élaguer l'arbre de recherche de solutions à des problème computationnels,
     * notamment pour les problèmes de satisfaction de contraintes.
     * 
     * @param domains       domaines des variables
     * @param instanciation instanciation vide ou partielle
     * @return une instanciation partielle, totale ou vide (dépendant de l'exécution
     *         de l'agorithme sur les contraintes du problème)
     * @see AbstractSolver#variables
     * @see AbstractSolver#constraints
     * @see ArcConsistency#enforceArcConsistency(Map)
     * @see #variableHeuristic
     * @see #valueHeuristic
     */
    private Map<Variable, Object> macHeuristic(Map<Variable, Set<Object>> domains,
            Map<Variable, Object> instanciation) {
        List<Variable> notInstanciatedVariables = new LinkedList<>();
        for (Variable variable : this.variables) {
            if (!instanciation.containsKey(variable))
                notInstanciatedVariables.add(variable);
        }

        if (notInstanciatedVariables.size() == 0 && this.isConsistent(instanciation)) {
            return instanciation;
        }

        // copie des domaines
        Map<Variable, Set<Object>> domainsCopy = new HashMap<>(domains);
        ArcConsistency arc = new ArcConsistency(this.constraints);
        boolean result = arc.enforceArcConsistency(domainsCopy);
        if (!result) {
            return null;
        }

        // choisi une variable parmi celle non instanciées
        Variable variableNotInstanciated = this.variableHeuristic.best(new HashSet<>(notInstanciatedVariables),
                domainsCopy);

        // On parcourt toutes les valeurs du domaine de la variable choisie
        for (Object value : this.valueHeuristic.ordering(variableNotInstanciated,
                domainsCopy.get(variableNotInstanciated))) {
            // nouvelle instanciation pour garder le contexte
            Map<Variable, Object> instanciation2 = new HashMap<>(instanciation);
            instanciation2.put(variableNotInstanciated, value);

            // vérifie que l'instanciation est valide
            if (this.isConsistent(instanciation2)) {
                if (instanciation2.keySet().containsAll(this.variables)) {
                    // toutes les variables sont instanciées
                    return instanciation2;
                }
                instanciation2 = this.macHeuristic(domainsCopy, instanciation2);
                if (instanciation2 != null) {
                    // solution trouvée
                    return instanciation2;
                }
            }
        }
        return null;
    }

    /**
     * Récupère l'heuristique sur les variables.
     * 
     * @return heuristique sur les variables
     * @see #variableHeuristic
     */
    public VariableHeuristic getVariableHeuristic() {
        return this.variableHeuristic;
    }

    /**
     * Récupère l'heuristique sur les valeurs.
     * 
     * @return heuristique sur les valeurs
     * @see #valueHeuristic
     */
    public ValueHeuristic getValueHeuristic() {
        return this.valueHeuristic;
    }
}
