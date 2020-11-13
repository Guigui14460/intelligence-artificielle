package examples;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import representation.BooleanVariable;
import representation.Constraint;
import representation.Rule;
import representation.Variable;
import solvers.BacktrackSolver;
import solvers.Solver;

/**
 * Démonstration d'un solveur de contraintes sur l'exemple de la maison du fil
 * rouge.
 */
public class HouseSolverDemo {
    /**
     * Méthode principale.
     * 
     * @param args arguments passés au terminal lors de l'exécution de cette classe
     */
    public static void main(String[] args) {
        HouseExample house = new HouseExample(3, 4,
                new HashSet<>(Arrays.asList("Cuisine 1", "Salle de bain 1", "Salle de bain 2")),
                new HashSet<>(Arrays.asList("Chambre 1", "Chambre 2", "Chambre 3", "Salle à manger", "Salon")));

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleCouleeSeche = new BooleanVariable("Dalle coulée sèche");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");

        // contraintes de l'exemple
        Constraint c1 = new Rule(dalleCouleeSeche, true, dalleCoulee, true);
        Constraint c2 = new Rule(mursEleves, true, dalleCouleeSeche, true);
        Constraint c3 = new Rule(toitureTerminee, true, mursEleves, true);

        // on ajoute les vars et les cons au problème
        house.addConstraints(c1, c2, c3);
        house.addVariables(dalleCoulee, dalleCouleeSeche, mursEleves, toitureTerminee);

        Solver solver = new BacktrackSolver(house.getVariables(), house.getConstraints());
        Map<Variable, Object> result = solver.solve();
        HouseSolverDemo.printResults(result, "Ma super villa");
    }

    /**
     * Affiche les résultats du solveur.
     * 
     * @param results   résultats
     * @param houseName nom de la maison
     */
    public static void printResults(Map<Variable, Object> results, String houseName) {
        System.out.println("Résultats du solveur pour la maison : " + houseName);
        for (Map.Entry<Variable, Object> entry : results.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}