package examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import representation.BooleanVariable;
import representation.Variable;

public class HouseMiner {
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
    }
}
