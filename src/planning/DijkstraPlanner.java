package planning;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe décrit un planificateur qui utilise l'algorithme de Dijsktra,
 * permettant de faire de la recherche de chemin en fonction des coûts des
 * actions dans un graphe.
 */
public class DijkstraPlanner implements Planner {
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
    public DijkstraPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public String toString() {
        return "DijkstraPlanner[initialState=" + this.initialState + ", actions=" + this.actions + ", goal=" + this.goal
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
        return this.dijkstra();
    }

    /**
     * Implémentation de l'algorithme de Dijkstra (généralisation de l'algorithme
     * BFS).
     * 
     * @return plan d'actions
     * @see #getDijkstraPlan(Map, Map, List, Map)
     * @see #argmin(Map, List)
     */
    private List<Action> dijkstra() {
        // instanciation des différentes structures de données
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
        Map<Map<Variable, Object>, Action> plan = new HashMap<>();
        Map<Map<Variable, Object>, Double> distance = new HashMap<>();
        List<Map<Variable, Object>> goals = new LinkedList<>(), open = new LinkedList<>();
        father.put(this.initialState, null);
        distance.put(this.initialState, 0.0);
        open.add(this.initialState);

        while (open.size() != 0) { // tant qu'il reste des états ouvert
            Map<Variable, Object> instanciation = this.argmin(distance, open); // on prend l'état avec la plus petite
                                                                               // distance
            open.remove(instanciation);

            if (this.goal.isSatisfiedBy(instanciation)) { // on est arrivé à un des buts
                goals.add(instanciation);
            }

            // on teste toutes les actions
            for (Action action : this.actions) {
                if (action.isApplicable(instanciation)) { // si on peut appliquer l'action
                    Map<Variable, Object> next = action.successor(instanciation);
                    if (!distance.containsKey(next)) { // si le successeur n'a pas déjà été vu
                        distance.put(next, Double.POSITIVE_INFINITY);
                    }
                    double newCost = distance.get(instanciation) + action.getCost();
                    if (distance.get(next) > newCost) {
                        // si le coût pour arriver au noeud est inférieur à celui précédemment trouvé
                        distance.put(next, newCost);
                        father.put(next, instanciation);
                        plan.put(next, action);
                        open.add(next);
                    }
                }
            }
        }
        if (goals.size() == 0) { // si aucun but trouvé
            return null;
        }
        return this.getDijkstraPlan(father, plan, goals, distance); // meilleur plan d'actions
    }

    /**
     * Permet de reconstruire la liste d'actions utilisée pour passer de l'état
     * initial à l'état but.
     * 
     * @param father   contient le père de chacun des états visités
     * @param plan     contient toutes les actions associées aux différents états
     *                 visités
     * @param goals    différents buts trouvés
     * @param distance contient toutes les distances minimales pour arriver à chacun
     *                 des noeuds visités
     * @return meilleure liste d'actions reconstruite dans le bon ordre (celle qui a
     *         le coup le plus petit)
     * @see #argmin(Map, List)
     */
    private List<Action> getDijkstraPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, List<Map<Variable, Object>> goals,
            Map<Map<Variable, Object>, Double> distance) {
        List<Action> dijPlan = new LinkedList<>();
        // on prend le but le plus proche
        Map<Variable, Object> goal = this.argmin(distance, goals);
        // on ajoute les actions au plan tant qu'on a pas atteint l'état initial
        while (goal != this.initialState) {
            dijPlan.add(plan.get(goal));
            goal = father.get(goal);
        }
        // on inverse le plan (pour le remettre dans le bon ordre)
        Collections.reverse(dijPlan);
        return dijPlan;
    }

    /**
     * Permet de retourner l'élément ayant la valeur la plus petite dans une map.
     * 
     * @param values map contenant les valeurs associées aux états
     * @param list   liste contenant les états concernés par la recherche de la
     *               valeur minimale
     * @return l'état associé à la valeur minimale trouvée
     */
    private Map<Variable, Object> argmin(Map<Map<Variable, Object>, Double> values, List<Map<Variable, Object>> list) {
        Map<Variable, Object> state = null;
        double min = Double.POSITIVE_INFINITY;
        // on regarde chaque entrée des valeurs
        for (Map.Entry<Map<Variable, Object>, Double> entry : values.entrySet()) {
            if (list.contains(entry.getKey())) { // on vérifie si l'état (clé de l'entrée) est concerné par la recherche
                if (entry.getValue() < min) {
                    min = entry.getValue();
                    state = entry.getKey();
                }
            }
        }
        return state;
    }
}
