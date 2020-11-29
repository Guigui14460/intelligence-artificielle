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
        System.out.println("#~~~~~~~~~~~~~~~~ Vérification arguments ~~~~~~~~~~~~~~~~#");
        String houseName = "NoBodyHouse";
        if (args.length < 4) {
            System.err.println(
                    "Usage : java [options] examples.HouseDemo <width> <length> <minFrequency> <minConfidence> [<houseName>]");
            System.exit(1);
        }
        if (args.length >= 5) {
            houseName = "";
            for (int i = 4; i < args.length; i++) {
                houseName += args[i] + " ";
            }
        }
        int width = Integer.parseInt(args[0]), length = Integer.parseInt(args[1]);
        float minFrequency = Float.parseFloat(args[2]), minConfidence = Float.parseFloat(args[3]);

        System.out.println("#~~~~~~~~~~~~~~~~ Génération des types de pièce ~~~~~~~~~~~~~~~~#");
        List<String> dryRooms = new ArrayList<>(
                Arrays.asList("Chambre 1", "Salle", "Salon", "Chambre 2", "Salle de jeux", "Chambre 3"));
        List<String> wetRooms = new ArrayList<>(
                Arrays.asList("Cuisine", "Salle de bains 1", "WC 1", "Salle de bains 2", "WC 2", "Salle de bains 3"));

        if (width * length > dryRooms.size() + wetRooms.size()) {
            System.err.println("Il n'a pas assez de types de pièce pour votre maison");
            System.exit(1);
        }

        // on change pour ne pas avoir trop de valeurs inutiles dans les domaines
        while (wetRooms.size() > ((width * length) / 2)) {
            wetRooms.remove(wetRooms.size() - 1);
        }
        while (dryRooms.size() - 1 > (((width * length) / 2) + 1)) {
            dryRooms.remove(dryRooms.size() - 1);
        }

        System.out.println("#~~~~~~~~~~~~~~~~ Instanciation de la maison ~~~~~~~~~~~~~~~~#");
        Set<Object> pieceDomain = new HashSet<>();
        pieceDomain.addAll(wetRooms);
        pieceDomain.addAll(dryRooms);
        HouseExample house = new HouseExample(width, length, new HashSet<>(wetRooms), new HashSet<>(dryRooms));

        System.out.println("#~~~~~~~~~~~~~~~~ Création des variables ~~~~~~~~~~~~~~~~#");
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= length; j++) {
                pieces.put(i + "," + j, new Variable("Pièce " + i + "," + j, new HashSet<>(pieceDomain)));
            }
        }
        house.addVariables(dalleCoulee, dalleHumide, mursEleves, toitureTerminee);
        house.addVariables(pieces.values());

        System.out.println("#~~~~~~~~~~~~~~~~ Solveur ~~~~~~~~~~~~~~~~#");
        // contraintes d'état
        Constraint c1 = new Rule(toitureTerminee, true, mursEleves, true);
        Constraint c2 = new Rule(mursEleves, true, dalleCoulee, true);
        Constraint c3 = new Rule(mursEleves, true, dalleHumide, false);
        Constraint c4 = new Rule(dalleHumide, true, dalleCoulee, true);
        house.addConstraints(c1, c2, c3, c4);

        // pièces uniques et non vides
        List<String> keys = new ArrayList<>(pieces.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Variable v1 = pieces.get(keys.get(i));
            house.addConstraints(new DifferenceConstraint(v1, null));
            for (int j = i + 1; j < keys.size(); j++) {
                house.addConstraints(new DifferenceConstraint(v1, pieces.get(keys.get(j))));
            }
        }

        // on récupère les pièces qui ne sont pas voisines
        Map<Variable, Set<Variable>> pieceNotNeighbors = new HashMap<>();
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= length; y++) {
                Variable variable = pieces.get(x + "," + y);
                if (variable != null) {
                    Set<Variable> notNeighbors = new HashSet<>();
                    for (int i = 1; i <= width; i++) {
                        for (int j = 1; j <= length; j++) {
                            if (i < x - 1 || i > x + 1 || j < y - 1 || j > y + 1) {
                                notNeighbors.add(pieces.get(i + "," + j));
                            }
                        }
                    }
                    pieceNotNeighbors.put(variable, notNeighbors);
                }
            }
        }
        // contrainte pour avoir des pièces d'eau côte à côte
        for (Map.Entry<Variable, Set<Variable>> entry : pieceNotNeighbors.entrySet()) {
            for (Variable v2 : entry.getValue()) {
                BinaryExtensionConstraint c = new BinaryExtensionConstraint(entry.getKey(), v2);
                for (Object value : entry.getKey().getDomain()) {
                    if (wetRooms.contains(value)) {
                        for (Object dryRoom : dryRooms) {
                            c.addTuple(value, dryRoom);
                            c.addTuple(dryRoom, value);
                        }
                    } else {
                        for (Object wetRoom : wetRooms) {
                            c.addTuple(value, wetRoom);
                            c.addTuple(wetRoom, value);
                        }
                    }
                }
                // house.addConstraints(c);
            }
        }

        /*
         * Solveur
         */
        Map<Variable, Object> solverResults = HouseSolver.solveWithHeuristicMAC(house);
        HouseSolver.printResults(solverResults, houseName);
        HouseSolver.printHousePlan(solverResults, houseName, house, pieces);

        System.gc();

        /*
         * Planificateur
         */
        System.out.println("#~~~~~~~~~~~~~~~~ Planification ~~~~~~~~~~~~~~~~#");
        if (solverResults != null) {
            Map<Variable, Object> initialState = new HashMap<>();
            initialState.put(dalleCoulee, false);
            for (Variable variable : pieces.values()) {
                initialState.put(variable, null); // met des emplacements vides
            }
            Map<Variable, Object> finalState = new HashMap<>(solverResults);
            finalState.put(dalleCoulee, true);
            finalState.put(dalleHumide, false);
            finalState.put(mursEleves, true);
            finalState.put(toitureTerminee, true);
            Goal but = new BasicGoal(finalState);

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
            for (Object nomPiece : pieceDomain) {
                for (Variable position : pieces.values()) {
                    Map<Variable, Object> preconditionCopy = new HashMap<>(precondition);
                    preconditionCopy.put(position, null);
                    effect = new HashMap<>();
                    effect.put(position, nomPiece);
                    actions.add(new NamedAction("Placer \"" + nomPiece + "\" à l'emplacement " + position.getName(),
                            preconditionCopy, effect, (position.getDomain().contains(nomPiece) ? 2 : 5)));
                }
            }

            Planner planner = new AStarPlanner(initialState, actions, but, new Heuristic() {
                @Override
                public float estimate(Map<Variable, Object> state) {
                    // on calcule le nombre de pièces correctement instanciées dans l'état actuel
                    int numOfExact = 0;
                    for (Map.Entry<Variable, Object> entry : state.entrySet()) {
                        if (entry.getValue() != null && entry.getValue().equals(finalState.get(entry.getKey()))) {
                            numOfExact++;
                        }
                    }
                    return -numOfExact; // on renvoie l'inverse car plus il y en a, plus on se rapproche du but
                }
            });
            List<Action> planTrouve = HousePlanner.printExecutionTime(planner, "A*");
            HousePlanner.printPlan(planTrouve, houseName);
        } else {
            System.out.println("Pas de solution du solveur donc pas d'exécution du planificateur");
        }

        System.gc();

        /*
         * Extracteur de connaissance
         */
        System.out.println("#~~~~~~~~~~~~~~~~ Extraction de connaissances ~~~~~~~~~~~~~~~~#");
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
