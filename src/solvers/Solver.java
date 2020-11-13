package solvers;

import java.util.Map;

import representation.Variable;

/**
 * Interface permettant d'implémenter des solveurs de contraintes.
 */
public interface Solver {
    /**
     * Résout un problème à partir de contraintes et renvoie une solution.
     * 
     * @return solution au problème (instanciation complète) ou {@code null} si
     *         aucune solution n'a été trouvée
     */
    public Map<Variable, Object> solve();
}
