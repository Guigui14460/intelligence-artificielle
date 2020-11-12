package datamining;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import representation.BooleanVariable;

/**
 * Classe représentant un extracteur de règle utilisant un algorithme
 * <em>brute-force</em> d'énumération des règles d'association fréquentes.
 */
public class BruteForceAssociationRuleMiner extends AbstractAssociationRuleMiner {
    /**
     * Constructeur par défaut.
     * 
     * @param database base de données transactionnelles
     */
    public BruteForceAssociationRuleMiner(BooleanDatabase database) {
        super(database);
    }

    @Override
    public Set<AssociationRule<BooleanVariable>> extract(float minimalFrequency, float minimalConfidence) {
        // initialisation des structures de données et récupération de tous les motifs
        // fréquents
        Set<AssociationRule<BooleanVariable>> results = new HashSet<>();
        Set<Itemset> frequentItemsets = (new Apriori(this.database)).extract(minimalFrequency);

        for (Itemset itemset : frequentItemsets) {
            Set<Set<BooleanVariable>> allPremises = BruteForceAssociationRuleMiner
                    .allCandidatePremises(itemset.getItems()); // récupère tous les sous-ensembles possibles
            for (Set<BooleanVariable> premise : allPremises) {
                // on récupère la conclusion associée à cette prémisse
                Set<BooleanVariable> conclusion = null;
                for (Set<BooleanVariable> set : allPremises) {
                    // on prend le complémentaire de la prémisse
                    boolean isNotInPremise = true;
                    for (BooleanVariable var : premise) {
                        isNotInPremise = isNotInPremise && !set.contains(var);
                    }
                    if (isNotInPremise) {
                        conclusion = set;
                    }
                }
                float confidence = AbstractAssociationRuleMiner.confidence(premise, conclusion, frequentItemsets);
                if (confidence >= minimalConfidence) {
                    results.add(new AssociationRule<>(premise, conclusion,
                            AbstractAssociationRuleMiner.frequency(itemset.getItems(), frequentItemsets), confidence));
                }
            }
        }
        return results;
    }

    /**
     * Retourne l'ensemble de tous ses sous-ensembles à l'exception de l'ensemble
     * vide et de l'ensemble lui-même.
     * 
     * @param itemset motif
     * @return l'ensemble de ses sous-ensembles
     */
    public static Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> itemset) {
        Set<Set<BooleanVariable>> subsets = new HashSet<>();
        int max = 1 << itemset.size(); // 2 puissance N
        for (int i = 0; i < max; i++) { // génère tous les sous-ensembles
            Set<BooleanVariable> subset = new HashSet<>();
            Iterator<BooleanVariable> iterator = itemset.iterator();
            for (int j = 0; j < itemset.size(); j++) {
                BooleanVariable item = iterator.next();
                if (((i >> j) & 1) == 1) {
                    subset.add(item);
                }
            }
            subsets.add(subset);
        }
        subsets.remove(new HashSet<>()); // retire l'ensemble vide
        subsets.remove(itemset); // retire l'ensemble lui-même
        return subsets;
    }
}
