package datamining;

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
     * @param database base de données transactionnelles
     */
    public AbstractAssociationRuleMiner(BooleanDatabase database){
        this.database = database;
    }

    @Override
    public BooleanDatabase getDatabase(){
        return this.database;
    }

    /**
     * Retourne la fréquence d'un motif parmi un ensemble de motifs.
     * @param itemset motif que l'on souhaite calculer sa fréquence
     * @param itemsets ensemble de motifs
     * @return fréquence de {@code itemset} dans {@code itemsets}
     */
    public static float frequency(Set<BooleanVariable> itemset, Set<Itemset> itemsets){
        float numberOfOccurence = 0;
        for(Itemset transaction: itemsets){
            if(transaction.getItems().containsAll(itemset)){
                numberOfOccurence++;
            }
        }
        return numberOfOccurence / itemsets.size();
    }

    /**
     * Retourne la confiance de la règle d'association en fonction de la prémisse et des candidates donnés.
     * @param premise prémisse de la règle d'association
     * @param conclusion candidates de la règle d'association
     * @param itemsets ensemble de motifs
     * @return confiance de la règle d'association
     */
    public float confidence(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, Set<Itemset> itemsets){
        float numberOfOccurence = 0;
        float frequencies = 0;
        for(Itemset transaction: itemsets){
            if(transaction.getItems().containsAll(premise)){
                numberOfOccurence++;
                frequencies += transaction.getFrequency();
            }
        }
        return frequencies / numberOfOccurence;
    }
}
