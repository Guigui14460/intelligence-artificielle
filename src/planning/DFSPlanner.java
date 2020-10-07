package planning;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe décrit un planificateur qui utilise l'algorithme DFS (Deep First Search), permettant de faire de la recherche de chemin en profondeur dans un graphe.
 */
public class DFSPlanner implements Planner {
    /**
     * État initial du planificateur.
     */
    private final Map<Variable, Object> initialState;

    /**
     * But à atteindre.
     */
    private final Goal goal;

    /**
     * Ensemble d'actions pouvant être effectuées.
     */
    private final Set<Action> actions;

    /**
     * Constructeur par défaut.
     * @param initialState état initial
     * @param actions ensemble d'actions pouvant être effectuées
     * @param goal but à atteindre
     */
    public DFSPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public Map<Variable, Object> getInitialState() {
        return this.initialState;
    }

    @Override
    public Set<Action> getActions() {
        return this.actions;
    }

    @Override
    public Goal getGoal() {
        return this.goal;
    }

    @Override
    public List<Action> plan() {
        return this.dfs(this.initialState, new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Implémentation de l'algorithme de recherche en profondeur DFS (Deep First Search).
     * @param state état actuel
     * @param plan plan actuel de la recherche
     * @param closed liste d'états déjà visités
     * @return nouveau plan
     */
    private List<Action> dfs(Map<Variable, Object> state, 
                            List<Action> plan, 
                            List<Map<Variable, Object>> closed){
        // condition de terminaison
        if(this.goal.isSatisfiedBy(state)){ // but satisfait
            return plan;
        }
        // itération de toutes les actions existantes dans le problème
        for(Action action: this.actions){
            if(action.isApplicable(state)){ // si l'action est réalisable
                Map<Variable, Object> next = action.successor(state);
                if(!closed.contains(next)){ // si le successeur n'a pas déjà été vu
                    plan.add(action);
                    closed.add(next);
                    // appel récursif de l'algorithme
                    List<Action> subPlan = this.dfs(next, plan, closed);
                    if(subPlan != null){ // si l'on atteint l'état but
                        return subPlan;
                    }
                    // sinon en supprime l'action du plan
                    plan.remove(action);
                }
            }
        }
        // aucune action possiblement réalisable
        return null;
    }
}
