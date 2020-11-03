package planning;

import java.util.HashMap;
import java.util.Map;

import representation.Variable;

/**
 * Cette classe décrit une action à faire en fonction de préconditions.
 */
public class BasicAction implements Action {
    /**
     * Précondition à satisfaire pour effectuer l'action.
     */
    private final Map<Variable, Object> precondition;

    /**
     * Effet à appliquer sur un état donné.
     */
    private final Map<Variable, Object> effect;

    /**
     * Coût de l'action.
     */
    private final int cost;

    /**
     * Constructeur par défaut.
     * @param precondition préconditions pour que l'action s'effectue
     * @param effect effet à appliquer sur l'état
     * @param cost coût de l'action
     */
    public BasicAction(Map<Variable, Object> precondition, Map<Variable, Object> effect, int cost){
        this.precondition = precondition;
        this.effect = effect;
        this.cost = cost;
    }

    @Override
    public String toString(){
        return "BasicAction[precondition=" + this.precondition + ", effect=" + this.effect + "]";
    }

    @Override
    public boolean isApplicable(Map<Variable, Object> state) {
        for(Map.Entry<Variable, Object> entry: this.precondition.entrySet()){
            // vérifie que chaque variable des préconditions se trouvent dans l'état
            // et que chacun sont égaux (entre précondition et état)
            if(state.get(entry.getKey()) == null || !state.get(entry.getKey()).equals(entry.getValue())){
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Variable, Object> successor(Map<Variable, Object> state) {
        // nouvelle instance à qui on remet tout l'état passé en argument
        Map<Variable, Object> newState = new HashMap<>();
        newState.putAll(state);

        // vérifie si l'action est applicable puis effectue l'action si c'est la cas
        if(this.isApplicable(state)){
            newState.putAll(this.effect);
        }
        return newState;
    }

    @Override
    public int getCost() {
        return this.cost;
    }
}
