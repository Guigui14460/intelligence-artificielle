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
        Set<AssociationRule<BooleanVariable>> results = new HashSet<>();
        Set<BooleanVariable> items = this.getDatabase().getItems();
        Set<Itemset> itemsets = new HashSet<>();
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
