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
        // vérifie que l'instanciation but est inclue dans l'état à tester
        for (Map.Entry<Variable, Object> entry : this.partialInstantiation.entrySet()) {
            // vérifie que chaque variable de l'instanciation but se trouvent dans l'état
            // et que chacun sont égaux (entre état but et état donné)
            if (state.get(entry.getKey()) == null || !state.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
