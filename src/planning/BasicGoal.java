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
    private final Map<Variable, Object> instanciationGoal;

    /**
     * Constructeur par défaut.
     * @param instanciationGoal instanciation à atteindre
     */
    public BasicGoal(Map<Variable, Object> instanciationGoal){
        this.instanciationGoal = instanciationGoal;
    }

    @Override
    public String toString(){
        return "BasicGoal[instanciationGoal=" + this.instanciationGoal + "]";
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> state) {
        // vérifie que l'instanciation but est inclue dans l'état à tester
        for(Map.Entry<Variable, Object> entry: this.instanciationGoal.entrySet()){
            // vérifie que chaque variable de l'instanciation but se trouvent dans l'état
            // et que chacun sont égaux (entre état but et état donné)
            if(state.get(entry.getKey()) == null || !state.get(entry.getKey()).equals(entry.getValue())){
                return false;
            }
        }
        return true;
    }
}
