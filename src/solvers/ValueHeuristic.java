package solvers;

import java.util.List;
import java.util.Set;

import representation.Variable;

/**
 * Interface permettant de faire des découvertes sur les valeurs en résolvant un problème.
 */
public interface ValueHeuristic {
    /**
     * Retourne une liste ordonnée selon l'heuristique.
     * @param variable variable
     * @param domain domaine de la variable
     * @return liste ordonnée au sens de l'heuristique
     */
    public List<Object> ordering(Variable variable, Set<Object> domain);
}
