package planning;

import java.util.Map;

import representation.Variable;

/**
 * Interface décrivant un but à atteindre.
 */
public interface Goal {
    /**
     * Vérifie si le but est satisfait.
     * 
     * @param state état actuel
     * @return booléen représentant la validation du but
     */
    public boolean isSatisfiedBy(Map<Variable, Object> state);
}
