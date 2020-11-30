package examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import datamining.Apriori;
import datamining.AssociationRule;
import datamining.BooleanDatabase;
import datamining.BruteForceAssociationRuleMiner;
import datamining.Database;
import datamining.Itemset;
import representation.BooleanVariable;
import representation.Variable;

/**
 * Démonstration d'un extracteur de connaissances sur un exemple précis d'une
 * maison pour le fil rouge.
 */
public class HouseMiner {
    /**
     * Largeur et longueur par défaut pour cette classe là uniquement.
     */
    public static final int WIDTH = 3, LENGTH = 2;

    /**
     * Fréquence et confiance minimales par défaut pour cette classe là uniquement.
     */
    public static final float MIN_FREQUENCY = 0.35f, MIN_CONFIDENCE = 0.7f;

    public static void main(String[] args) {
        Set<String> dryRooms = new HashSet<>(Arrays.asList("Chambre 1", "Chambre 2", "Salle", "Salon"));
        Set<String> wetRooms = new HashSet<>(Arrays.asList("Cuisine", "Salle de bain"));
        Set<Object> pieceDomaine = new HashSet<>();
        pieceDomaine.addAll(wetRooms);
        pieceDomaine.addAll(dryRooms);

        HouseExample house = new HouseExample(HouseMiner.WIDTH, HouseMiner.LENGTH, wetRooms, dryRooms);

        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleHumide = new BooleanVariable("Dalle humide");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");
        Map<String, Variable> pieces = new HashMap<>();
        for (int i = 1; i <= HouseMiner.LENGTH; i++) {
            for (int j = 1; j <= HouseMiner.WIDTH; j++) {
                pieces.put(i + "," + j, new Variable("Pièce " + i + "," + j, new HashSet<>(pieceDomaine)));
            }
        }

        house.addVariables(dalleCoulee, dalleHumide, mursEleves, toitureTerminee);
        house.addVariables(pieces.values());

        Database database = new Database(house.getVariables());
        // on crée les différences instances pour la BD
        database.add(new HashMap<>());
        Map<Variable, Object> instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        database.add(new HashMap<>(instance));
        instance.put(dalleHumide, true);
        database.add(new HashMap<>(instance));
        instance.put(dalleHumide, false);
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        database.add(new HashMap<>(instance));
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        instance.put(dalleHumide, false);
        instance.put(pieces.get("1,1"), "Chambre 1");
        instance.put(pieces.get("1,2"), "Chambre 2");
        instance.put(pieces.get("1,3"), "Salon");
        instance.put(pieces.get("2,1"), "Salle");
        instance.put(pieces.get("2,2"), "Salle de bain");
        instance.put(pieces.get("2,3"), "Cuisine");
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        instance.put(dalleHumide, false);
        instance.put(pieces.get("1,1"), "Chambre 1");
        instance.put(pieces.get("1,2"), "Chambre 2");
        instance.put(pieces.get("1,3"), "Salle");
        instance.put(pieces.get("2,1"), "Salon");
        instance.put(pieces.get("2,2"), "Salle de bain");
        instance.put(pieces.get("2,3"), "Cuisine");
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        instance.put(dalleHumide, false);
        instance.put(pieces.get("1,1"), "Chambre 1");
        instance.put(pieces.get("1,2"), "Chambre 2");
        instance.put(pieces.get("1,3"), "Salle de bain");
        instance.put(pieces.get("2,1"), "Salle");
        instance.put(pieces.get("2,2"), "Salon");
        instance.put(pieces.get("2,3"), "Cuisine");
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        instance.put(dalleHumide, false);
        instance.put(pieces.get("1,1"), "Chambre 1");
        instance.put(pieces.get("1,2"), "Chambre 2");
        instance.put(pieces.get("1,3"), "Salle de bain");
        instance.put(pieces.get("2,1"), "Salon");
        instance.put(pieces.get("2,2"), "Salle");
        instance.put(pieces.get("2,3"), "Cuisine");
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        instance.put(dalleHumide, false);
        instance.put(pieces.get("1,1"), "Chambre 1");
        instance.put(pieces.get("1,2"), "Salon");
        instance.put(pieces.get("1,3"), "Salle de bain");
        instance.put(pieces.get("2,1"), "Chambre 2");
        instance.put(pieces.get("2,2"), "Salle");
        instance.put(pieces.get("2,3"), "Cuisine");
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        instance = new HashMap<>();
        instance.put(dalleCoulee, true);
        instance.put(dalleHumide, false);
        instance.put(pieces.get("1,1"), "Salon");
        instance.put(pieces.get("1,2"), "Chambre 1");
        instance.put(pieces.get("1,3"), "Salle de bain");
        instance.put(pieces.get("2,1"), "Salle");
        instance.put(pieces.get("2,2"), "Cuisine");
        instance.put(pieces.get("2,3"), "Chambre 2");
        database.add(new HashMap<>(instance));
        instance.put(mursEleves, true);
        instance.put(toitureTerminee, true);
        database.add(new HashMap<>(instance));

        BooleanDatabase booleanDatabase = database.propositionalize(); // on transforme en BD booléenne pour extraire
                                                                       // les motifs et les règles
        Set<Itemset> extractedItemsets = (new Apriori(booleanDatabase)).extract(HouseMiner.MIN_FREQUENCY);
        HouseMiner.printExtractedItemsets(extractedItemsets, HouseMiner.MIN_FREQUENCY);
        Set<AssociationRule<BooleanVariable>> extractedAssociationRule = (new BruteForceAssociationRuleMiner(
                booleanDatabase)).extract(HouseMiner.MIN_FREQUENCY, HouseMiner.MIN_CONFIDENCE);
        HouseMiner.printExtractedAssociationRule(extractedAssociationRule, HouseMiner.MIN_FREQUENCY,
                HouseMiner.MIN_CONFIDENCE);
    }

    /**
     * Affiche les motifs extraits.
     * 
     * @param itemsets     motifs
     * @param minFrequency fréquence minimale donnée à l'extracteur
     */
    public static final void printExtractedItemsets(Set<Itemset> itemsets, float minFrequency) {
        System.out.println(
                "Il y a " + itemsets.size() + " motifs ayant une fréquence supérieure ou égale à " + minFrequency);
        for (Itemset itemset : itemsets) {
            System.out.println(itemset);
        }
    }

    /**
     * Affiche les règles associatives extraites.
     * 
     * @param rules         règles associatives
     * @param minFrequency  fréquence minimale donnée à l'extracteur
     * @param minConfidence confiance minimale donnée à l'extracteur
     */
    public static final void printExtractedAssociationRule(Set<AssociationRule<BooleanVariable>> rules,
            float minFrequency, float minConfidence) {
        System.out.println("Il y a " + rules.size() + " règles associatives ayant une fréquence supérieure ou égale à "
                + minFrequency + " une confiance supérieure ou égale à " + minConfidence);
        for (AssociationRule<BooleanVariable> rule : rules) {
            System.out.println("(" + rule.getPremise() + " => " + rule.getConclusion() + ", frequency "
                    + rule.getFrequency() + ", confidence " + rule.getConfidence() + ")");
        }
    }
}
