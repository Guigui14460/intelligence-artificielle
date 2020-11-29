package planning;

import java.util.Map;

import representation.Variable;

/**
 * Cette classe décrit une instanciation but à atteindre.
 */
public class BasicGoal implements Goal {
    /**
     * Instanciation but à atteindre.
     */
    private final Map<Variable, Object> partialInstantiation;

    /**
     * Constructeur par défaut.
     * 
     * @param partialInstantiation instanciation à atteindre
     */
    public BasicGoal(Map<Variable, Object> partialInstantiation) {
        this.partialInstantiation = partialInstantiation;
    }

    @Override
    public String toString() {
        return "BasicGoal[partialInstantiation=" + this.partialInstantiation + "]";
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> state) {
        return state.entrySet().containsAll(this.partialInstantiation.entrySet());
    }
}
