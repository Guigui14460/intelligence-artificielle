package solvers;

import java.util.List;
import java.util.Set;

import representation.Variable;

/**
 * Interface décrivant une heuristic capable d'optimiser les découvertes sur les
 * valeurs lors de la résolution d'un problème.
 */
public interface ValueHeuristic {
    /**
     * Retourne une liste de valeurs ordonnées selon l'heuristique.
     * 
     * @param variable variable
     * @param domain   domaine de la variable
     * @return liste ordonnée au sens de l'heuristique
     */
    public List<Object> ordering(Variable variable, Set<Object> domain);
}
