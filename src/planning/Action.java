package planning;

import java.util.Map;

import representation.Variable;

/**
 * Interface définissant une action à effectuer sur des états (instanciations).
 */
public interface Action {
    /**
     * Vérifie si une action est applicable pour un état donné.
     * @param state état à tester
     * @return booléen représentant la validité de l'action
     */
    public boolean isApplicable(Map<Variable, Object> state);

    /**
     * Renvoie le successeur de l'état actuel.
     * @param state état actuel
     * @return état suivant
     */
    public Map<Variable, Object> successor(Map<Variable, Object> state);

    /**
     * Récupère le coût de l'action.
     * @return coût de l'action
     */
    public int getCost();
}
