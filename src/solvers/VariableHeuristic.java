package solvers;

import java.util.Map;
import java.util.Set;

import representation.Variable;

/**
 * Interface décrivant une heuristic capable d'optimiser les découvertes sur les
 * variables lors de la résolution d'un problème.
 */
public interface VariableHeuristic {
    /**
     * Retourne la meilleure variable au sens de l'heuristique.
     * 
     * @param variables ensemble de variables
     * @param domains   ensemble de domaines
     * @return meilleure variable au sens de l'heuristique
     */
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains);
}
