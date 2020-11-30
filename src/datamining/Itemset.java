package datamining;

import java.util.Set;

import representation.BooleanVariable;

/**
 * Classe représentant un motif.
 */
public class Itemset {
    /**
     * Ensemble de variables.
     */
    private final Set<BooleanVariable> items;

    /**
     * Fréquence d'apparition de l'ensemble de variables dans la base de données
     * transactionnelle.
     */
    private final float frequency;

    /**
     * Constructeur par défaut.
     * 
     * @param items     ensemble de variables
     * @param frequency fréquence d'apparition de l'ensemble de variables dans la
     *                  base de données transactionnelle
     */
    public Itemset(final Set<BooleanVariable> items, final float frequency) {
        this.items = items;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Itemset[items=" + this.items + ", frequency=" + this.frequency + "]";
    }

    /**
     * Récupère l'ensemble de variables.
     * 
     * @return ensemble de variables
     * @see #items
     */
    public Set<BooleanVariable> getItems() {
        return this.items;
    }

    /**
     * Récupère la fréquence d'apparition de l'ensemble de variables dans la base de
     * données transactionnelle.
     * 
     * @return fréquence d'apparition
     * @see #frequency
     */
    public float getFrequency() {
        return this.frequency;
    }
}
