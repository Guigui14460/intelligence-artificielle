package solvers;

import java.util.Map;
import java.util.Set;

import representation.Variable;

/**
 * Interface permettant de faire des découvertes sur les variables en résolvant un problème.
 */
public interface VariableHeuristic {
    /**
     * Retourne la meilleure variable au sens de l'heuristique.
     * @param variables ensemble de variables
     * @param domains ensemble de domaines
     * @return meilleure variable au sens de l'heuristique
     */
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains);
}
