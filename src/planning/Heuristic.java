package planning;

import java.util.Map;

import representation.Variable;

/**
 * Interface permettant de définir une heuristique qui pourra donner une
 * estimation d'un coup pour passer d'un état A à un état B.
 */
public interface Heuristic {
    /**
     * Donne une estimation d'un coup.
     * 
     * @param state état
     * @return un nombre réel correspondant à une estimation du coup menant à l'état
     *         suivant
     */
    public float estimate(Map<Variable, Object> state);
}
