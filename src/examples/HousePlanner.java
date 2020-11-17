package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import planning.AStarPlanner;
import planning.Action;
import planning.BasicAction;
import planning.BasicGoal;
import planning.Goal;
import planning.Heuristic;
import planning.Planner;
import representation.BinaryExtensionConstraint;
import representation.BooleanVariable;
import representation.Constraint;
import representation.DifferenceConstraint;
import representation.Rule;
import representation.Variable;

/**
 * Démonstration d'un planificateur sur l'exemple de la maison du fil rouge.
 */
public class HousePlanner {
    /**
     * Largeur et hauteur par défaut pour cette classe là uniquement.
     */
    public static final int WIDTH = 3, HEIGHT = 2;

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
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= HousePlanner.HEIGHT; i++) {
            for (int j = 1; j <= HousePlanner.WIDTH; j++) {
                pieces.put(i + "," + j, new Variable("Pièce " + i + "," + j, new HashSet<>(pieceDomaine)));
            }
        }

        // contraintes de l'exemple
        Constraint c1 = new Rule(toitureTerminee, true, mursEleves, true);
        Constraint c2 = new Rule(mursEleves, true, dalleCoulee, true);
        Constraint c3 = new Rule(mursEleves, true, dalleHumide, false);
        Constraint c4 = new Rule(dalleHumide, true, dalleCoulee, true);
        List<String> keys = new ArrayList<>(pieces.keySet());
        for (int i = 0; i < keys.size(); i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                house.addConstraints(new DifferenceConstraint(pieces.get(keys.get(i)), pieces.get(keys.get(j))));
            }
        }
        BinaryExtensionConstraint c5 = new BinaryExtensionConstraint(pieces.get("2,1"), pieces.get("1,3"));
        c5.addTuple("Salle de bain", "Chambre 1");
        c5.addTuple("Chambre 1", "Cuisine");
        c5.addTuple("Salle de bain", "Chambre 2");
        c5.addTuple("Salle de bain", "Chambre 1");
        BinaryExtensionConstraint c6 = new BinaryExtensionConstraint(pieces.get("2,3"), pieces.get("1,1"));
        c6.addTuple("Salle de bain", "Chambre 1");
        c6.addTuple("Chambre 1", "Cuisine");
        c6.addTuple("Salle de bain", "Chambre 2");
        c6.addTuple("Salle de bain", "Chambre 1");

        // on ajoute les vars et les cons au problème
        house.addVariables(dalleCoulee, dalleHumide, mursEleves, toitureTerminee);
        house.addVariables(pieces.values());
        house.addConstraints(c1, c2, c3, c4, c5, c6);

        // état initial
        Map<Variable, Object> initialState = new HashMap<>();
        initialState.put(dalleCoulee, false);

        // état but
        Map<Variable, Object> instanciationGoal = new HashMap<>();
        instanciationGoal.put(dalleCoulee, true);
        instanciationGoal.put(dalleHumide, false);
        instanciationGoal.put(mursEleves, true);
        instanciationGoal.put(toitureTerminee, true);
        Goal goal = new BasicGoal(instanciationGoal);

        Set<Action> actions = new HashSet<>();
        Map<Variable, Object> preconditions = new HashMap<>();
        Map<Variable, Object> effects = new HashMap<>();
        effects.put(dalleCoulee, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));
        effects.put(dalleCoulee, true);
        effects.put(dalleHumide, false);
        effects.put(mursEleves, true);
        effects.put(toitureTerminee, true);
        Action actionToChange = (Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 10);
        Action actionToChange2 = (Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 3);
        actions.add(actionToChange);
        preconditions.put(dalleCoulee, true);
        effects = new HashMap<>();
        effects.put(dalleHumide, false);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));
        preconditions.put(dalleCoulee, true);
        preconditions.put(dalleHumide, false);
        effects.put(mursEleves, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));
        preconditions.put(dalleCoulee, true);
        preconditions.put(dalleHumide, false);
        preconditions.put(mursEleves, true);
        effects.put(toitureTerminee, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));

        Planner planer = new AStarPlanner(initialState, actions, goal, new Heuristic() {
            @Override
            public float estimate(Map<Variable, Object> state) {
                return 0.0f;
            }
        });
        List<Action> plan = planer.plan();
        HousePlanner.printPlan(plan, "Ma super villa");

        System.out.println("\n");

        actions.add(actionToChange2);
        plan = planer.plan();
        HousePlanner.printPlan(plan, "Ma deuxième super villa");
    }

    /**
     * Affiche les caractéristiques du plan trouvé.
     * 
     * @param plan      plan trouvé
     * @param houseName nom de la maison
     */
    public static void printPlan(List<Action> plan, String houseName) {
        System.out.println("Plan pour construire la maison : " + houseName);
        System.out.println("Nombre d'actions à effectuer : " + plan.size());
        int cost = 0;
        for (Action action : plan) {
            System.out.println(action);
            cost += action.getCost();
        }
        System.out.println("Coût du plan : " + cost);
    }
}