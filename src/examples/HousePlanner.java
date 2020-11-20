package examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import planning.AStarPlanner;
import planning.Action;
import planning.BFSPlanner;
import planning.BasicAction;
import planning.BasicGoal;
import planning.DFSPlanner;
import planning.DijkstraPlanner;
import planning.Goal;
import planning.Heuristic;
import planning.Planner;
import representation.BooleanVariable;
import representation.Variable;

/**
 * Démonstration d'un planificateur sur l'exemple de la maison du fil rouge.
 */
public class HousePlanner {
    /**
     * Largeur et hauteur par défaut pour cette classe là uniquement.
     */
    public static final int WIDTH = 3, HEIGHT = 1;

    /**
     * Méthode principale.
     * 
     * @param args arguments passés au terminal lors de l'exécution de cette classe
     */
    public static void main(String[] args) {
        Set<String> dryRooms = new HashSet<>(Arrays.asList("Chambre 1", "Chambre 2", "Salle", "Salon"));
        Set<String> wetRooms = new HashSet<>(Arrays.asList("Cuisine", "Salle de bain"));
        Set<Object> pieceDomaine = new HashSet<>();
        pieceDomaine.addAll(wetRooms);
        pieceDomaine.addAll(dryRooms);

        HouseExample house = new HouseExample(HousePlanner.WIDTH, HousePlanner.HEIGHT, wetRooms, dryRooms);

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        BooleanVariable toitureCommencee = new BooleanVariable("Toiture commencée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= HousePlanner.HEIGHT; i++) {
            for (int j = 1; j <= HousePlanner.WIDTH; j++) {
                pieces.put(i + "," + j, new Variable("Pièce " + i + "," + j, new HashSet<>(pieceDomaine)));
            }
        }

        house.addVariables(dalleCoulee, dalleHumide, mursEleves, toitureTerminee, toitureCommencee);
        house.addVariables(pieces.values());

        Map<Variable, Object> etatInitial = new HashMap<>(), etatBut = new HashMap<>();
        etatInitial.put(dalleCoulee, false);
        etatBut.put(dalleCoulee, true);
        etatBut.put(dalleHumide, false);
        etatBut.put(mursEleves, true);
        etatBut.put(toitureTerminee, true);
        etatBut.put(toitureCommencee, true);
        etatBut.put(pieces.get("1,1"), "Salon");
        etatBut.put(pieces.get("1,2"), "Cuisine");
        etatBut.put(pieces.get("1,3"), "Chambre 1");
        Goal goal = new BasicGoal(etatBut);

        Set<Action> actions = new HashSet<>();
        Map<Variable, Object> precondition = new HashMap<>(), effect = new HashMap<>();
        effect.put(dalleCoulee, true);
        actions.add((Action) new BasicAction(new HashMap<>(precondition), new HashMap<>(effect), 1));
        effect.put(dalleCoulee, true);
        effect.put(dalleHumide, false);
        effect.put(mursEleves, true);
        effect.put(toitureTerminee, true);
        Action actionToChange = (Action) new BasicAction(new HashMap<>(precondition), new HashMap<>(effect), 10);
        actions.add(actionToChange);
        precondition.put(dalleCoulee, true);
        effect = new HashMap<>();
        effect.put(dalleHumide, false);
        actions.add((Action) new BasicAction(new HashMap<>(precondition), new HashMap<>(effect), 1));
        effect = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, false);
        effect.put(mursEleves, true);
        actions.add((Action) new BasicAction(new HashMap<>(precondition), new HashMap<>(effect), 1));
        effect = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, false);
        precondition.put(mursEleves, true);
        effect.put(toitureCommencee, true);
        actions.add((Action) new BasicAction(new HashMap<>(precondition), new HashMap<>(effect), 1));
        effect = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, false);
        precondition.put(mursEleves, true);
        precondition.put(toitureCommencee, true);
        effect.put(toitureTerminee, true);
        actions.add((Action) new BasicAction(new HashMap<>(precondition), new HashMap<>(effect), 1));
        precondition = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, false);
        for (Object nomPiece : pieceDomaine) {
            for (Variable position : pieces.values()) {
                effect = new HashMap<>();
                effect.put(position, nomPiece);
                actions.add(new BasicAction(new HashMap<>(precondition), effect, 2));
            }
        }

        Planner planner = new DFSPlanner(etatInitial, actions, goal);
        List<Action> plan = HousePlanner.printExecutionTime(planner, "DFS");
        planner = new BFSPlanner(etatInitial, actions, goal);
        plan = HousePlanner.printExecutionTime(planner, "BFS");
        planner = new DijkstraPlanner(etatInitial, actions, goal);
        plan = HousePlanner.printExecutionTime(planner, "Dijkstra");
        planner = new AStarPlanner(etatInitial, actions, goal, new Heuristic() {
            @Override
            public float estimate(Map<Variable, Object> state) {
                return 0;
            }
        });
        plan = HousePlanner.printExecutionTime(planner, "A*");
        HousePlanner.printPlan(plan, "Ma super villa");

        // on fait en sorte que les ouvriers soient multi-thread :)
        Set<Set<Object>> subsetsPieces = HousePlanner.allSubsets(pieceDomaine);
        Set<Set<Object>> filteredSubsets = subsetsPieces.stream().filter(item -> item.size() == 2)
                .collect(Collectors.toSet());
        for (Set<Object> subset : filteredSubsets) {
            Iterator<Object> ite = subset.iterator();
            Object piece1 = ite.next();
            Object piece2 = ite.next();
            for (Variable position1 : pieces.values()) {
                for (Variable position2 : pieces.values()) {
                    precondition = new HashMap<>();
                    precondition.put(dalleCoulee, true);
                    precondition.put(dalleHumide, false);
                    effect = new HashMap<>();
                    effect.put(position1, piece1);
                    effect.put(position2, piece2);
                    actions.add(new BasicAction(precondition, effect, 3));
                }
            }
        }
        planner = new AStarPlanner(new HashMap<>(), actions, goal, new Heuristic() {
            @Override
            public float estimate(Map<Variable, Object> state) {
                return etatBut.size() - state.size();
            }
        });
        plan = HousePlanner.printExecutionTime(planner, "A*");
        HousePlanner.printPlan(plan, "Ma seconde villa");
    }

    /**
     * Affiche la durée d'exécution pour trouver le plan et retourne le plan trouvé.
     * 
     * @param planner planificateur
     * @return plan trouvé
     */
    public static List<Action> printExecutionTime(Planner planner, String plannerName) {
        long begin = System.currentTimeMillis();
        List<Action> plan = planner.plan();
        long finish = System.currentTimeMillis();
        System.out.println(plannerName + " (Durée : " + (finish - begin) / 1000.0 + " s, Noeuds explorés : "
                + planner.getNumOfExploredNodes() + ")");
        return plan;
    }

    /**
     * Affiche les caractéristiques du plan trouvé.
     * 
     * @param plan      plan trouvé
     * @param houseName nom de la maison
     */
    public static void printPlan(List<Action> plan, String houseName) {
        System.out.println("Plan pour construire la maison : " + houseName);
        if (plan == null) {
            System.out.println("Aucun plan trouvé");
            return;
        }
        System.out.println("Nombre d'actions à effectuer : " + plan.size());
        int cost = 0;
        for (Action action : plan) {
            System.out.println(action);
            cost += action.getCost();
        }
        System.out.println("Coût du plan : " + cost);
    }

    /**
     * Retourne l'ensemble de tous ses sous-ensembles.
     * 
     * @param objects ensemble d'objets
     * @return l'ensemble de ses sous-ensembles
     */
    public static Set<Set<Object>> allSubsets(Set<Object> objects) {
        Set<Set<Object>> subsets = new HashSet<>();
        int max = 1 << objects.size(); // 2 puissance N
        for (int i = 0; i < max; i++) { // génère tous les sous-ensembles
            Set<Object> subset = new HashSet<>();
            Iterator<Object> iterator = objects.iterator();
            for (int j = 0; j < objects.size(); j++) {
                Object item = iterator.next();
                if (((i >> j) & 1) == 1) {
                    subset.add(item);
                }
            }
            subsets.add(subset);
        }
        return subsets;
    }
}