package datamining;

import java.util.HashSet;
import java.util.Set;

import representation.BooleanVariable;

/**
 * Classe abstraite représentant la base des extracteurs de règles.
 */
public abstract class AbstractAssociationRuleMiner implements AssociationRuleMiner {
    /**
     * Base de données transactionnelles.
     */
    private BooleanDatabase database;

    /**
     * Constructeur par défaut.
     * 
     * @param database base de données transactionnelles
     */
    public AbstractAssociationRuleMiner(BooleanDatabase database) {
        this.database = database;
    }

    @Override
    public BooleanDatabase getDatabase() {
        return this.database;
    }

    /**
     * Retourne la fréquence d'un motif parmi un ensemble de motifs.
     * 
     * @param itemset  motif que l'on souhaite calculer sa fréquence
     * @param itemsets ensemble de motifs
     * @return fréquence de {@code itemset} dans {@code itemsets}
     * @throws Exception levée lorsque le motif recherché ne se trouve pas dans
     *                   l'ensemble de motifs
     */
    public static float frequency(Set<BooleanVariable> itemset, Set<Itemset> itemsets) throws Exception {
        for (Itemset transaction : itemsets) {
            if (itemset.equals(transaction.getItems())) { // on prend que si c'est exactement égal
                return transaction.getFrequency();
            }
        }
        throw new Exception("searched itemset is not present in itemsets");
    }

    /**
     * Retourne la confiance de la règle d'association en fonction de la prémisse et
     * des candidates donnés.
     * 
     * @param premise    prémisse de la règle d'association
     * @param conclusion candidates de la règle d'association
     * @param itemsets   ensemble de motifs
     * @return confiance de la règle d'association
     */
    public static float confidence(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion,
            Set<Itemset> itemsets) {
        float premiseFreq = 0, conclusionFreq = 0;
        for (Itemset itemset : itemsets) {
            Set<BooleanVariable> set = new HashSet<>(premise);
            set.addAll(conclusion);
            if (itemset.getItems().equals(premise)) {
                premiseFreq = itemset.getFrequency();
            }
            if (itemset.getItems().equals(set)) {
                conclusionFreq = itemset.getFrequency();
            }
        }
        return conclusionFreq / premiseFreq;
    }
}
