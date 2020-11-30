package datamining;

import java.util.Set;

/**
 * Classe représentant une règle d'association.
 */
public class AssociationRule<E> {
    /**
     * Représente la prémisse de cette règle d'association.
     */
    private final Set<E> premise;

    /**
     * Représente la conclusion de cette règle d'association.
     */
    private final Set<E> conclusion;

    /**
     * Représente la fréquence d'apparition de cette règle dans les transactions.
     */
    private final float frequency;

    /**
     * Représente la confiance dans cette règle.
     */
    private final float confidence;

    /**
     * Constructeur par défaut.
     * 
     * @param premise    prémisse
     * @param conclusion conclusion
     * @param frequency  fréquence d'apparition dans la base de données
     * @param confidence confiance de la règle
     */
    public AssociationRule(final Set<E> premise, final Set<E> conclusion, final float frequency,
            final float confidence) {
        this.premise = premise;
        this.conclusion = conclusion;
        this.frequency = frequency;
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "AssociationRule[premise=" + this.premise + ", conclusion=" + this.conclusion + ", frequency="
                + this.frequency + ", confidence=" + this.confidence + "]";
    }

    /**
     * Récupère la prémisse de la règle.
     * 
     * @return prémisse
     * @see #premise
     */
    public Set<E> getPremise() {
        return this.premise;
    }

    /**
     * Récupère la conclusion de la règle.
     * 
     * @return conclusion
     * @see #conclusion
     */
    public Set<E> getConclusion() {
        return this.conclusion;
    }

    /**
     * Récupère la fréquence d'apparition dans la base de données.
     * 
     * @return fréquence
     * @see #frequency
     */
    public float getFrequency() {
        return this.frequency;
    }

    /**
     * Récupère la confiance dans cette règle.
     * 
     * @return confiance
     * @see #confidence
     */
    public float getConfidence() {
        return this.confidence;
    }
}
