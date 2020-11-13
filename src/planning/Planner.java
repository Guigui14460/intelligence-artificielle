package planning;

import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe décrit n'importe quel planificateur.
 */
public interface Planner {
    /**
     * Planifie une liste d'actions (chemin) à emprunter pour aller de l'état
     * initial à l'état but.
     * 
     * @return liste d'actions planifiées
     */
    public List<Action> plan();

    /**
     * Récupère l'état initial du planificateur.
     * 
     * @return état initial du planificateur
     */
    public Map<Variable, Object> getInitialState();

    /**
     * Retourne un ensemble d'actions pouvant être effectuées.
     * 
     * @return ensemble d'actions pouvant être effectuées
     */
    public Set<Action> getActions();

    /**
     * Récupère le but attendu.
     * 
     * @return le but attendu
     */
    public Goal getGoal();
}
