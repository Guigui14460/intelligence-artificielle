package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import representation.BinaryExtensionConstraint;
import representation.BooleanVariable;
import representation.Constraint;
import representation.DifferenceConstraint;
import representation.Rule;
import representation.Variable;
import solvers.BacktrackSolver;
import solvers.HeuristicMACSolver;
import solvers.MACSolver;
import solvers.NbConstraintsVariableHeuristic;
import solvers.RandomValueHeuristic;
import solvers.ValueHeuristic;
import solvers.VariableHeuristic;

/**
 * Démonstration d'un solveur de contraintes sur l'exemple de la maison du fil
 * rouge.
 */
public class HouseSolver {
    /**
     * Largeur et hauteur par défaut pour cette classe là uniquement.
     */
    public static final int WIDTH = 3, HEIGHT = 2;

    /**
     * Résout le problème de maison avec l'algorithme "backtrack".
     * 
     * @param house maison
     * @return solution
     */
    public static final Map<Variable, Object> solveWithBacktrack(HouseExample house) {
        return (new BacktrackSolver(house.getVariables(), house.getConstraints())).solve();
    }

    /**
     * Résout le problème de maison avec l'algorithme "MAC".
     * 
     * @param house maison
     * @return solution
     */
    public static final Map<Variable, Object> solveWithMAC(HouseExample house) {
        return (new MACSolver(house.getVariables(), house.getConstraints())).solve();
    }

    /**
     * Résout le problème de maison avec l'algorithme "MAC" disposant d'heuristiques
     * de variable et de valeur.
     * 
     * @param house maison
     * @return solution
     */
    public static final Map<Variable, Object> solveWithHeuristicMAC(HouseExample house) {
        return HouseSolver.solveWithHeuristicMAC(house,
                new NbConstraintsVariableHeuristic(house.getVariables(), house.getConstraints(), true),
                new RandomValueHeuristic(new Random()));
    }

    /**
     * Résout le problème de maison avec l'algorithme "MAC" disposant d'heuristiques
     * de variable et de valeur.
     * 
     * @param house maison
     * @return solution
     */
    public static final Map<Variable, Object> solveWithHeuristicMAC(HouseExample house,
            VariableHeuristic variableHeuristic, ValueHeuristic valueHeuristic) {
        return (new HeuristicMACSolver(house.getVariables(), house.getConstraints(), variableHeuristic, valueHeuristic))
                .solve();
    }

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

        HouseExample house = new HouseExample(HouseSolver.WIDTH, HouseSolver.HEIGHT, wetRooms, dryRooms);

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= HouseSolver.HEIGHT; i++) {
            for (int j = 1; j <= HouseSolver.WIDTH; j++) {
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

        Map<Variable, Object> result = HouseSolver.solveWithHeuristicMAC(house);
        HouseSolver.printResults(result, "Ma super villa");
    }

    /**
     * Affiche les résultats du solveur.
     * 
     * @param results   résultats
     * @param houseName nom de la maison
     */
    public static void printResults(Map<Variable, Object> results, String houseName) {
        System.out.println("Résultats du solveur pour la maison : " + houseName);
        if (results == null) {
            System.out.println("Aucune solution possible");
        } else {
            for (Map.Entry<Variable, Object> entry : results.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }
}