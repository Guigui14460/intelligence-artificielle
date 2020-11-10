package datamining;

import java.util.Set;

import representation.BooleanVariable;

/**
 * Interface décrivant un extracteur de règles.
 */
public interface AssociationRuleMiner {
    /**
     * Récupère la base de données transactionnelles.
     * 
     * @return base de données transactionnelles
     */
    public BooleanDatabase getDatabase();

    /**
     * Extrait des règles d'association fréquemment retrouvées à partir des
     * transactions de la base de données transactionnelles.
     * 
     * @param minimalFrequency  fréquence minimale des règles à prendre
     * @param minimalConfidence confiance minimale des règles à prendre
     * @return ensemble de règles d'association fréquentes
     */
    public Set<AssociationRule<BooleanVariable>> extract(float minimalFrequency, float minimalConfidence);
}
