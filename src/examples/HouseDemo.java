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
import planning.BasicGoal;
import planning.Goal;
import planning.Heuristic;
import planning.NamedAction;
import planning.Planner;
import representation.BinaryExtensionConstraint;
import representation.BooleanVariable;
import representation.Constraint;
import representation.DifferenceConstraint;
import representation.Rule;
import representation.Variable;

/**
 * Démonstration primitive sur l'exemple de la maison du fil rouge.
 */
public class HouseDemo {
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
        String houseName = "Ma super Villa";

        Set<String> dryRooms = new HashSet<>(Arrays.asList("Chambre 1", "Chambre 2", "Salle", "Salon"));
        Set<String> wetRooms = new HashSet<>(Arrays.asList("Cuisine", "Salle de bain"));
        Set<Object> pieceDomaine = new HashSet<>();
        pieceDomaine.addAll(wetRooms);
        pieceDomaine.addAll(dryRooms);

        HouseExample house = new HouseExample(HouseDemo.WIDTH, HouseDemo.HEIGHT, wetRooms, dryRooms);

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= HouseDemo.HEIGHT; i++) {
            for (int j = 1; j <= HouseDemo.WIDTH; j++) {
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

        /*
         * Solveur
         */
        Map<Variable, Object> resultatSolveur = HouseSolver.solveWithHeuristicMAC(house);
        HouseSolver.printResults(resultatSolveur, houseName);

        /*
         * Planificateur
         */
        Map<Variable, Object> etatInitial = new HashMap<>();
        etatInitial.put(dalleCoulee, false);
        Map<Variable, Object> etatFinal = new HashMap<>(resultatSolveur);
        etatFinal.put(dalleCoulee, true);
        etatFinal.put(dalleHumide, false);
        Goal but = new BasicGoal(etatFinal);

        Set<Action> actions = new HashSet<>();
        Map<Variable, Object> precondition = new HashMap<>(), effect = new HashMap<>();
        effect.put(dalleCoulee, true);
        effect.put(dalleHumide, true);
        actions.add(new NamedAction("Couler la dalle", precondition, effect, 5));

        precondition = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, true);
        effect = new HashMap<>();
        effect.put(dalleHumide, false);
        actions.add(new NamedAction("Attendre séchage de la dalle", precondition, effect, 10));

        precondition = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, false);
        effect = new HashMap<>();
        effect.put(mursEleves, true);
        actions.add(new NamedAction("Élever les murs", precondition, effect, 5));

        precondition = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(mursEleves, true);
        effect = new HashMap<>();
        effect.put(toitureTerminee, true);
        actions.add(new NamedAction("Faire le toit", precondition, effect, 5));

        precondition = new HashMap<>();
        precondition.put(dalleCoulee, true);
        precondition.put(dalleHumide, false);
        for (Object nomPiece : pieceDomaine) {
            for (Variable position : pieces.values()) {
                effect = new HashMap<>();
                effect.put(position, nomPiece);
                actions.add(new NamedAction("Placer \"" + nomPiece + "\" à l'emplacement " + position.getName(),
                        new HashMap<>(precondition), effect, (position.getDomain().contains(nomPiece) ? 2 : 5)));
            }
        }

        Planner planner = new AStarPlanner(etatInitial, actions, but, new Heuristic() {
            @Override
            public float estimate(Map<Variable, Object> state) {
                return state.size() - etatFinal.size();
            }
        });
        List<Action> planTrouve = HousePlanner.printExecutionTime(planner, "A*");
        HousePlanner.printPlan(planTrouve, houseName);

        /*
         * Extractteur de connaissance
         */
    }
}
