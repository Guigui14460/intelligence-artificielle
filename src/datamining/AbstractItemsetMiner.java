package datamining;

import java.util.Comparator;
import java.util.Set;

import representation.BooleanVariable;

/**
 * Classe représentant la base d'une implémentation d'un mineur de données.
 */
public abstract class AbstractItemsetMiner implements ItemsetMiner {
    /**
     * Permet de comparer deux variables booléennes entre elles.
     */
    public static final Comparator<BooleanVariable> COMPARATOR = (var1, var2) -> var1.getName()
            .compareTo(var2.getName());

    /**
     * Base de données transactionnelle booléenne.
     */
    protected final BooleanDatabase database;

    /**
     * Contructeur par défaut.
     * 
     * @param database base de données à utiliser
     */
    public AbstractItemsetMiner(final BooleanDatabase database) {
        this.database = database;
    }

    /**
     * Calcule la fréquence d'un motif dans les transactions de la base de données.
     * 
     * @param itemset motif
     * @return fréquence du motif dans la base de données
     */
    public float frequency(Set<BooleanVariable> itemset) {
        float numberOfOccurence = 0;
        for (Set<BooleanVariable> iteratedItemset : this.database.getTransactions()) {
            if (iteratedItemset.containsAll(itemset)) {
                numberOfOccurence++;
            }
        }
        return numberOfOccurence / this.database.getTransactions().size();
    }

    @Override
    public BooleanDatabase getDatabase() {
        return this.database;
    }
}