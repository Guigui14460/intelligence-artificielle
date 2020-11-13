package planning;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe décrit un planificateur qui utilise l'algorithme BFS (Breadth
 * First Search), permettant de faire de la recherche de chemin en largeur dans
 * un graphe.
 */
public class BFSPlanner implements Planner {
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
     * 
     * @param initialState état initial
     * @param actions      ensemble d'actions pouvant être effectuées
     * @param goal         but à atteindre
     */
    public BFSPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public String toString() {
        return "BFSPlanner[initialState=" + this.initialState + ", actions=" + this.actions + ", goal=" + this.goal
                + "]";
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
        return this.bfs();
    }

    /**
     * Implémentation de l'algorithme de recherche en largeur BFS.
     * 
     * @return plan d'actions
     * @see #getBFSPlan(Map, Map, Map)
     */
    private List<Action> bfs() {
        // instanciation des différentes structures de données
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
        Map<Map<Variable, Object>, Action> plan = new HashMap<>();
        List<Map<Variable, Object>> closed = new LinkedList<>();
        Queue<Map<Variable, Object>> open = new LinkedList<>();
        father.put(this.initialState, null);
        closed.add(this.initialState);
        open.offer(this.initialState);

        // on vérifie que le but n'est pas déjà satisfait par l'état initial
        if (this.goal.isSatisfiedBy(this.initialState)) {
            return new LinkedList<>();
        }

        // tant qu'il reste des états ouverts
        while (open.size() != 0) {
            // on défile
            Map<Variable, Object> instanciation = open.poll();
            closed.add(instanciation);

            // on teste toutes les actions
            for (Action action : this.actions) {
                if (action.isApplicable(instanciation)) { // si on peut appliquer l'action
                    Map<Variable, Object> next = action.successor(instanciation);
                    if (!closed.contains(next) && !open.contains(next)) { // si le successeur n'a pas déjà été vu
                        father.put(next, instanciation);
                        plan.put(next, action);
                        if (this.goal.isSatisfiedBy(next)) { // on vérifie si le but est atteint
                            return this.getBFSPlan(father, plan, next); // retourne le plus court plan d'actions
                        }
                        // ajout du next dans la liste des ouverts afin de montrer que l'on l'a vu
                        open.offer(next);
                    }
                }
            }
        }
        return null; // aucun plan d'actions trouvé
    }

    /**
     * Permet de reconstruire la liste d'actions utilisée pour passer de l'état
     * initial à l'état but.
     * 
     * @param father contient le père de chacun des états visités
     * @param plan   contient toutes les actions associées aux différents états
     *               visités
     * @param goal   prochain but à atteindre
     * @return liste d'actions reconstruite dans le bon ordre
     */
    private List<Action> getBFSPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, Map<Variable, Object> goal) {
        List<Action> bfsPlan = new LinkedList<>();
        // on ajoute les actions au plan tant qu'on a pas atteint l'état initial
        while (goal != this.initialState) {
            bfsPlan.add(plan.get(goal));
            goal = father.get(goal);
        }
        // on inverse le plan (pour le remettre dans le bon ordre)
        Collections.reverse(bfsPlan);
        return bfsPlan;
    }
}
