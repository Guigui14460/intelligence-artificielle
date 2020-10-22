package datamining;

import java.util.Set;

import representation.BooleanVariable;

/**
 * Classe représentant une règle d'association.
 */
public class AssociationRule {
    /**
     * Représente de prémisse de cette règle d'association.
     */
    private Set<BooleanVariable> premise;

    /**
     * Représente des candidates de cette règle d'association.
     */
    private Set<BooleanVariable> conclusion;

    /**
     * Représente la fréquence d'apparition de cette règle dans les transactions.
     */
    private float frequency;

    /**
     * Représente la confiance dans cette règle.
     */
    private float confidence;

    /**
     * Constructeur par défaut.
     * @param premise prémisse
     * @param conclusion candidates
     * @param frequency fréquence d'apparition dans la base de données
     * @param confidence confiance de la règle
     */
    public AssociationRule(Set<BooleanVariable> premise, Set<BooleanVariable> conclusion, float frequency, float confidence){
        this.premise = premise;
        this.conclusion = conclusion;
        this.frequency = frequency;
        this.confidence = confidence;
    }

    /**
     * Récupère la prémisse de la règle.
     * @return prémisse
     * @see #premise
     */
    public Set<BooleanVariable> getPremise(){
        return this.premise;
    }

    /**
     * Récupère les candidates de la règle.
     * @return candidates
     * @see #conclusion
     */
    public Set<BooleanVariable> getConclusion(){
        return this.conclusion;
    }

    /**
     * Récupère la fréquence d'apparition dans la base de données.
     * @return fréquence
     * @see #frequency
     */
    public float getFrequency(){
        return this.frequency;
    }

    /**
     * Récupère la confiance dans cette règle.
     * @return confiance
     * @see #confidence
     */
    public float getConfidence(){
        return this.confidence;
    }
}
