package datamining;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import representation.BooleanVariable;

/**
 * Classe représentant une base de données transactionnelles où toutes les
 * variables sont booléennes.
 */
public class BooleanDatabase {
    /**
     * Ensemble de variables booléennes pouvant être mis dans la base de données.
     */
    private Set<BooleanVariable> items;

    /**
     * Liste de transactions (instances).
     */
    private List<Set<BooleanVariable>> transactions;

    /**
     * Constructeur par défaut.
     * 
     * @param items ensemble de variables booléennes
     */
    public BooleanDatabase(Set<BooleanVariable> items) {
        this.items = items;
        this.transactions = new ArrayList<>();
    }

    /**
     * Ajoute une transaction à la liste des transactions.
     * 
     * @param transaction transaction à ajouter
     * @see #transactions
     */
    public void add(Set<BooleanVariable> transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Récupère l'ensemble des variables.
     * 
     * @return ensemble des variables
     * @see #items
     */
    public Set<BooleanVariable> getItems() {
        return this.items;
    }

    /**
     * Récupère la liste des transactions.
     * 
     * @return liste des transactions
     * @see #transactions
     */
    public List<Set<BooleanVariable>> getTransactions() {
        return this.transactions;
    }
}
