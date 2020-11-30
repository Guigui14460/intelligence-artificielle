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
 * Démonstration d'un solveur de contraintes sur un exemple précis d'une maison
 * pour le fil rouge.
 */
public class HouseSolver {
    /**
     * Largeur et longueur par défaut pour cette classe là uniquement.
     */
    public static final int WIDTH = 3, LENGTH = 2;

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
        Set<Object> pieceDomain = new HashSet<>();
        pieceDomain.addAll(wetRooms);
        pieceDomain.addAll(dryRooms);

        HouseExample house = new HouseExample(HouseSolver.WIDTH, HouseSolver.LENGTH, wetRooms, dryRooms);

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= HouseSolver.LENGTH; i++) {
            for (int j = 1; j <= HouseSolver.WIDTH; j++) {
                pieces.put(i + "," + j, new Variable("Pièce " + i + "," + j, new HashSet<>(pieceDomain)));
            }
        }
        house.addVariables(dalleCoulee, dalleHumide, mursEleves, toitureTerminee);
        house.addVariables(pieces.values());

        // contraintes d'état
        Constraint c1 = new Rule(dalleCoulee, false, dalleHumide, false);
        Constraint c2 = new Rule(dalleHumide, true, dalleCoulee, true);
        Constraint c3 = new Rule(dalleHumide, true, mursEleves, false);
        Constraint c4 = new Rule(mursEleves, false, toitureTerminee, false);
        Constraint c5 = new Rule(mursEleves, true, dalleCoulee, true);
        Constraint c6 = new Rule(mursEleves, true, dalleHumide, false);
        Constraint c7 = new Rule(toitureTerminee, true, mursEleves, true);
        house.addConstraints(c1, c2, c3, c4, c5, c6, c7);
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
        for (int x = 1; x <= HouseSolver.WIDTH; x++) {
            for (int y = 1; y <= HouseSolver.LENGTH; y++) {
                Variable variable = pieces.get(x + "," + y);
                if (variable != null) {
                    Set<Variable> notNeighbors = new HashSet<>();
                    for (int i = 1; i <= HouseSolver.WIDTH; i++) {
                        for (int j = 1; j <= HouseSolver.LENGTH; j++) {
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
            Variable v1 = entry.getKey();
            for (Variable v2 : entry.getValue()) {
                BinaryExtensionConstraint c = new BinaryExtensionConstraint(v1, v2);
                for (Object firstRoom : v1.getDomain()) {
                    for (Object secondRoom : v2.getDomain()) {
                        if (!wetRooms.contains(firstRoom) || !wetRooms.contains(secondRoom)) { // si au moins une des
                                                                                               // deux pièces n'est pas
                                                                                               // une pièce d'eau
                            c.addTuple(firstRoom, secondRoom);
                        }
                    }
                }
                house.addConstraints(c);
            }
        }

        Map<Variable, Object> result = HouseSolver.solveWithHeuristicMAC(house);
        HouseSolver.printResults(result, "Ma super villa");
    }

    /**
     * Affiche les résultats du solveur.
     * 
     * @param results   résultats
     * @param houseName nom de la maison
     */
    public static final void printResults(Map<Variable, Object> results, String houseName) {
        System.out.println("Résultats du solveur pour la maison : " + houseName);
        if (results == null) {
            System.out.println("Aucune solution possible");
        } else {
            for (Map.Entry<Variable, Object> entry : results.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    /**
     * Méthode permettant d'afficher proprement les pièces de la maison.
     * 
     * @param results   résultats
     * @param houseName nom de la maison
     * @param house     maison
     * @param pieces    dictionnaire de pièces pour faciliter la récupération des
     *                  pièces
     */
    public static final void printHousePlan(Map<Variable, Object> results, String houseName, HouseExample house,
            Map<String, Variable> pieces) {
        System.out.println("Plan de la maison : " + houseName);
        if (results == null) {
            System.out.println("Pas de plan de maison possible");
        } else {
            boolean onePieceAtLeast = false;
            for (int i = 1; i <= house.getLength(); i++) {
                if (house.getWidth() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 1; j <= house.getWidth(); j++) {
                        onePieceAtLeast = true;
                        int maxStringLength = 0;
                        for (int k = 1; k <= house.getLength(); k++) { // on prend la longueur maximale sur la colonne
                            String str = (String) results.get(pieces.get(j + "," + k));
                            if (str.length() > maxStringLength) {
                                maxStringLength = str.length();
                            }
                        }
                        String pieceType = (String) results.get(pieces.get(j + "," + i));
                        sb.append("[ " + pieceType);
                        for (int k = pieceType.length(); k <= maxStringLength; k++) {
                            sb.append(" ");
                        }
                        sb.append("] ");
                    }
                    System.out.println(sb.toString());
                }
            }
            if (!onePieceAtLeast) {
                System.out.println("Maison pas assez grande pour avoir une pièce");
            }
        }
    }
}