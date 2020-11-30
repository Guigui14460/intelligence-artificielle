package planning;

import java.util.Map;

import representation.Variable;

/**
 * Cette classe décrit une action nommée.
 */
public class NamedAction extends BasicAction {
    /**
     * Nom de l'action.
     */
    private final String name;

    /**
     * Constructeur par défaut.
     * 
     * @param name         nom de l'action
     * @param precondition précondition pour que l'action s'effectue
     * @param effect       effet à appliquer sur l'état
     * @param cost         coût de l'action
     */
    public NamedAction(final String name, final Map<Variable, Object> precondition, final Map<Variable, Object> effect,
            final int cost) {
        super(precondition, effect, cost);
        this.name = name;
    }

    @Override
    public String toString() {
        return "NamedAction[name=" + this.name + ", precondition=" + this.precondition + ", effect=" + this.effect
                + ", cost=" + this.cost + "]";
    }

    /**
     * Récupère le nom de l'action.
     * 
     * @return nom de l'action
     * @see #name
     */
    public String getName() {
        return this.name;
    }
}
