package planning;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe décrit un planificateur qui utilise l'algorithme A*, permettant de faire de la recherche de chemin.
 */
public class AStarPlanner implements Planner {
    /**
     * État initial du planificateur.
     */
    private Map<Variable, Object> initialState;

    /**
     * But à atteindre.
     */
    private Goal goal;

    /**
     * Ensemble d'actions pouvant être effectuées.
     */
    private Set<Action> actions;

    /**
     * Heuristique utilisé pour optimiser l'algorithme de recherche de plan. 
     */
    private Heuristic heuristic;

    /**
     * Constructeur par défaut.
     * @param initialState état initial
     * @param actions ensemble d'actions pouvant être effectuées
     * @param goal but à atteindre
     * @param heuristic heuristique
     */
    public AStarPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal, Heuristic heuristic) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
        this.heuristic = heuristic;
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
        return this.aStar();
    }

    /**
     * Implémentation de l'algorithme A*, généralisation de l'algorithme de Dijkstra 
     * permettant de ne chercher un chemin que pour un but avec une recherche heuristique.
     * @return meilleur plan d'actions à réaliser pour passer de l'état initial à l'état but
     * @see #getBFSPlan(Map, Map, Map)
     * @see #argmin(Map, List)
     */
    private List<Action> aStar(){
        // instanciation des différentes variables
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>();
        Map<Map<Variable, Object>, Action> plan = new HashMap<>();
        Map<Map<Variable, Object>, Double> distance = new HashMap<>(), value = new HashMap<>();
        List<Map<Variable, Object>> open = new LinkedList<>();
        open.add(this.initialState);
        father.put(this.initialState, null);
        distance.put(this.initialState, 0.0);
        value.put(this.initialState, Float.valueOf(this.heuristic.estimate(this.initialState)).doubleValue());
        
        while (open.size() != 0) { // tant qu'il reste des états ouvert
            Map<Variable, Object> instanciation = this.argmin(value, open); // on prend l'état avec la plus petite valeur associé
            if(this.goal.isSatisfiedBy(instanciation)){  // on est arrivé au but
                return this.getBFSPlan(father, plan, instanciation); // on reconstruit le plan
            }
            open.remove(instanciation);
            // on teste toutes les actions
            for(Action action: this.actions){
                if(action.isApplicable(instanciation)){ // si on peut appliquer l'action
                    Map<Variable, Object> next = action.successor(instanciation);
                    if(!distance.containsKey(next)){ // si le successeur n'a pas déjà été vu
                        distance.put(next, Double.POSITIVE_INFINITY);
                    }
                    double newCost = distance.get(instanciation) + action.getCost();
                    if(distance.get(next) > newCost){
                        // si le coût pour arriver au noeud est inférieur à celui précédemment trouvé
                        distance.put(next, newCost);
                        value.put(next, distance.get(next) + this.heuristic.estimate(next));
                        father.put(next, instanciation);
                        plan.put(next, action);
                        open.add(next);
                    }
                }
            }
        }
        return null; // aucun plan d'action trouvé
    }

    /**
     * Permet de reconstruire la liste d'actions utilisée pour passer de l'état initial à l'état but.
     * @param father contient tous états qu'on a pu visité
     * @param plan contient toutes les actions associées aux différents états visités
     * @param goal prochain but à atteindre
     * @return liste d'actions reconstruite dans le bon ordre
     */
    private List<Action> getBFSPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father, Map<Map<Variable, Object>, Action> plan, Map<Variable, Object> goal){
        List<Action> bfsPlan = new LinkedList<>();
        // on ajoute les actions au plan tant qu'il reste un but
        while(goal != this.initialState){
            bfsPlan.add(plan.get(goal));
            goal = father.get(goal);
        }
        // on inverse le plan (pour le remettre dans le bon ordre)
        Collections.reverse(bfsPlan);
        return bfsPlan;
    }

    /**
     * Permet de retourner l'élément ayant la valeur la plus petite dans une map.
     * @param values map contenant les valeurs associées aux états
     * @param list liste contenant les états concernés par la recherche de la valeur minimale
     * @return l'état associé à la valeur minimale trouvée
     */
    private Map<Variable, Object> argmin(Map<Map<Variable, Object>, Double> values, List<Map<Variable, Object>> list){
        Map<Variable, Object> state = null;
        double min = Double.POSITIVE_INFINITY;
        // on regarde chaque entrée des valeurs
        for(Map.Entry<Map<Variable, Object>, Double> entry: values.entrySet()){
            if(list.contains(entry.getKey())){ // on vérifie si l'état (clé de l'entrée) est concerné par la recherche
                if(entry.getValue() < min){
                    min = entry.getValue();
                    state = entry.getKey();
                }
            }
        }
        return state;
    }
}
