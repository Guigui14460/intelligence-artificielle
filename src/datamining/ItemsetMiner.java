package datamining;

import java.util.Set;

/**
 * Interface décrivant un mineur de données.
 */
public interface ItemsetMiner {
    /**
     * Récupère une base de données transactionnelle booléenne.
     * @return base de données
     */
    public BooleanDatabase getDatabase();

    /**
     * Extrait un ensemble de motifs à partir des données d'une base de données.
     * @param minimalFrequency fréquence minimal pour le filtrage des fréquences des motifs
     * @return ensemble de motifs
     */
    public Set<Itemset> extract(float minimalFrequency);
}
