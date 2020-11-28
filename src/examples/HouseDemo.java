package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datamining.AssociationRule;
import datamining.BooleanDatabase;
import datamining.BruteForceAssociationRuleMiner;
import datamining.Database;
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
import solvers.BacktrackAllSolver;

/**
 * Démonstration primitive sur l'exemple de la maison du fil rouge.
 */
public class HouseDemo {
    /**
     * Méthode principale.
     * 
     * @param args arguments passés au terminal lors de l'exécution de cette classe
     */
    public static void main(String[] args) {
        String houseName = "NoBodyHouse";
        if (args.length < 4) {
            System.err.println(
                    "Usage : java [options] examples.HouseDemo <width> <height> <minFrequency> <minConfidence> [<houseName>]");
            System.exit(1);
        }
        if (args.length >= 5) {
            houseName = args[4];
        }
        int width = Integer.parseInt(args[0]), height = Integer.parseInt(args[1]);
        float minFrequency = Float.parseFloat(args[2]), minConfidence = Float.parseFloat(args[3]);

        List<String> dryRooms = new ArrayList<>(
                Arrays.asList("Chambre 1", "Salle", "Salon", "Chambre 2", "Salle de jeux", "Chambre 3"));
        List<String> wetRooms = new ArrayList<>(
                Arrays.asList("Cuisine", "Salle de bains 1", "WC 1", "Salle de bains 2", "WC 2", "Salle de bains 3"));

        if (width * height > dryRooms.size() + wetRooms.size()) {
            System.err.println("Il n'a pas assez de types de pièce pour votre maison");
            System.exit(1);
        }

        // on change pour ne pas avoir trop de domaine inutile
        while (wetRooms.size() + 1 > ((width * height) / 2)) {
            wetRooms.remove(wetRooms.get(wetRooms.size() - 1));
        }
        while (dryRooms.size() - 1 > (((width * height) / 2) + 1)) {
            dryRooms.remove(dryRooms.get(dryRooms.size() - 1));
        }

        Set<Object> pieceDomaine = new HashSet<>();
        pieceDomaine.addAll(wetRooms);
        pieceDomaine.addAll(dryRooms);
        HouseExample house = new HouseExample(width, height, new HashSet<>(wetRooms), new HashSet<>(dryRooms));

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                pieces.put(i + "," + j, new Variable("Pièce " + i + "," + j, new HashSet<>(pieceDomaine)));
            }
        }
        house.addVariables(dalleCoulee, dalleHumide, mursEleves, toitureTerminee);
        house.addVariables(pieces.values());

        // contraintes d'état
        Constraint c1 = new Rule(toitureTerminee, true, mursEleves, true);
        Constraint c2 = new Rule(mursEleves, true, dalleCoulee, true);
        Constraint c3 = new Rule(mursEleves, true, dalleHumide, false);
        Constraint c4 = new Rule(dalleHumide, true, dalleCoulee, true);
        house.addConstraints(c1, c2, c3, c4);

        // pièces uniques
        List<String> keys = new ArrayList<>(pieces.keySet());
        for (int i = 0; i < keys.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (i != j) {
                    Variable v1 = pieces.get(keys.get(i));
                    Variable v2 = pieces.get(keys.get(j));
                    house.addConstraints(new DifferenceConstraint(v1, v2), new DifferenceConstraint(v2, v1));
                }
            }
        }

        // on récupère les pièces qui ne sont pas voisines
        Map<Variable, Set<Variable>> pieceNotNeighbors = new HashMap<>();
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                Variable variable = pieces.get(x + "," + y);
                if (variable != null) {
                    Set<Variable> notNeighbors = new HashSet<>();
                    for (int i = 1; i <= width; i++) {
                        for (int j = 1; j <= height; j++) {
                            if (i < x - 1 || i > x + 1 || j < y - 1 || j > y + 1) {
                                notNeighbors.add(pieces.get(i + "," + j));
                            }
                        }
                    }
                    pieceNotNeighbors.put(variable, notNeighbors);
                }
            }
        }
        // contrainte pour avoir des pièce d'eau côte à côte
        for (Map.Entry<Variable, Set<Variable>> entry : pieceNotNeighbors.entrySet()) {
            for (Variable v2 : entry.getValue()) {
                BinaryExtensionConstraint c = new BinaryExtensionConstraint(entry.getKey(), v2);
                for (Object value : entry.getKey().getDomain()) {
                    if (wetRooms.contains(value)) {
                        for (Object dryRoom : dryRooms) {
                            c.addTuple(value, dryRoom);
                            c.addTuple(dryRoom, value);
                        }
                    }
                }
                house.addConstraints(c);
            }
        }

        /*
         * Solveur
         */
        Map<Variable, Object> resultatSolveur = HouseSolver.solveWithHeuristicMAC(house);
        HouseSolver.printResults(resultatSolveur, houseName);

        /*
         * Planificateur
         */
        if (resultatSolveur != null) {
            Map<Variable, Object> etatInitial = new HashMap<>();
            etatInitial.put(dalleCoulee, false);
            Map<Variable, Object> etatFinal = new HashMap<>(resultatSolveur);
            etatFinal.put(dalleCoulee, true);
            etatFinal.put(dalleHumide, false);
            etatFinal.put(mursEleves, true);
            etatFinal.put(toitureTerminee, true);
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
                    return state.size() - etatFinal.size(); // on regarde juste le nombre de
                    // variables qu'il reste à
                    // // instancier
                }
            });
            List<Action> planTrouve = HousePlanner.printExecutionTime(planner, "A*");
            HousePlanner.printPlan(planTrouve, houseName);
        }

        /*
         * Extracteur de connaissance
         */
        Database database = new Database(house.getVariables());
        // on crée les différences instances pour la BD
        BacktrackAllSolver allSolver = new BacktrackAllSolver(house.getVariables(), house.getConstraints());
        allSolver.solve();
        for (Map<Variable, Object> instance : allSolver.getAllSolutions()) {
            database.add(instance);
        }

        BooleanDatabase booleanDatabase = database.propositionalize();// on transforme en BD booléenne pour extraire les
                                                                      // motifs et les règles
        Set<AssociationRule<BooleanVariable>> extractedAssociationRule = (new BruteForceAssociationRuleMiner(
                booleanDatabase)).extract(minFrequency, minConfidence);
        HouseMiner.printExtractedAssociationRule(extractedAssociationRule, minFrequency, minConfidence);
    }
}
