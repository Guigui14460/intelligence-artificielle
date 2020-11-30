package planning;

import java.util.HashMap;
import java.util.Map;

import representation.Variable;

/**
 * Cette classe décrit une action à faire en fonction de la précondition et
 * donne un effet lors de l'exécution de celle-ci.
 */
public class BasicAction implements Action {
    /**
     * Précondition à satisfaire pour effectuer l'action.
     */
    protected final Map<Variable, Object> precondition;

    /**
     * Effet à appliquer sur un état donné.
     */
    protected final Map<Variable, Object> effect;

    /**
     * Coût de l'action.
     */
    protected final int cost;

    /**
     * Constructeur par défaut.
     * 
     * @param precondition précondition pour que l'action s'effectue
     * @param effect       effet à appliquer sur l'état
     * @param cost         coût de l'action
     */
    public BasicAction(final Map<Variable, Object> precondition, final Map<Variable, Object> effect, final int cost) {
        this.precondition = precondition;
        this.effect = effect;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "BasicAction[precondition=" + this.precondition + ", effect=" + this.effect + ", cost=" + this.cost
                + "]";
    }

    @Override
    public boolean isApplicable(Map<Variable, Object> state) {
        return state.entrySet().containsAll(this.precondition.entrySet());
    }

    @Override
    public Map<Variable, Object> successor(Map<Variable, Object> state) {
        // nouvelle instance à qui on remet tout l'état passé en argument
        Map<Variable, Object> newState = new HashMap<>();
        newState.putAll(state);

        // vérifie si l'action est applicable puis effectue l'action si c'est la cas
        if (this.isApplicable(state)) {
            newState.putAll(this.effect);
        }
        return newState;
    }

    @Override
    public int getCost() {
        return this.cost;
    }
}
