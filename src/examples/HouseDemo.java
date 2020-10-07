package examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import representation.BooleanVariable;
import representation.Constraint;
import representation.Rule;
import representation.Variable;

/**
 * Démonstration primitive sur l'exemple de la maison du fil rouge.
 */
public class HouseDemo {
    /**
     * Méthode principale.
     * @param args arguments passés au terminal lors de l'exécution de cette classe
     */
    public static void main(String[] args){
        HouseExample house = new HouseExample(3, 4, 
            new HashSet<>(Arrays.asList("Cuisine 1", "Salle de bain 1", "Salle de bain 2")), 
            new HashSet<>(Arrays.asList("Chambre 1", "Chambre 2", "Chambre 3", "Salle à manger", "Salon"))
        );
        
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
    
        // on crée une instanciation qui est une solution
        Map<Variable, Object> goodAffectation = new HashMap<>();
        goodAffectation.put(dalleCoulee, true);
        goodAffectation.put(dalleCouleeSeche, true);
        goodAffectation.put(mursEleves, true);
        goodAffectation.put(toitureTerminee, false);

        // on crée une instanciation qui n'est pas une solution
        Map<Variable, Object> badAffectation = new HashMap<>();
        badAffectation.put(dalleCoulee, false);
        badAffectation.put(dalleCouleeSeche, true);
        badAffectation.put(mursEleves, false);
        badAffectation.put(toitureTerminee, true);

        // on regarde les résultats
        isASolution(house, goodAffectation, "Well-built house (in progress)"); // renvoie true
        isASolution(house, badAffectation, "Badly assembled house (finished)"); // renvoie false
    }

    /**
     * Vérifie si l'instanciation correspond aux contraintes liées à la maison.
     * @param house maison (problème)
     * @param affectation instanciation (partielle ou finie) à tester
     * @param houseName nom de la maison
     * @return booléen représentant le fait que l'instanciation est une solution au problème ou non
     */
    public static boolean isASolution(HouseExample house, Map<Variable, Object> affectation, String houseName){
        // on vérifie que l'instanciation est une solution au problème
        boolean isASolution = true;
        for(Constraint c: house.getConstraints()){
            if(!c.isSatisfiedBy(affectation)){
                isASolution = false;
                break;
            }
        }
        
        // on affiche un message différent en fonction de si l'instanciation est une solution ou non
        if(isASolution){
            System.out.println("L'instance est une solution au problème de la maison " + houseName + ".");
        } else {
            System.out.println("L'instance n'est pas une solution au problème de la maison " + houseName + ".");
        }
        return isASolution;
    }
}
