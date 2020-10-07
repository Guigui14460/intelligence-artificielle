package representation;

import java.util.Map;
import java.util.Set;

/**
 * Cette interface définie des méthodes liées à n'importe quelle contrainte existante.
 */
public interface Constraint {
    /**
     * Récupère l'ensemble des variables sur laquelle porte la contrainte actuelle.
     * @return un ensemble de variables
     */
    public Set<Variable> getScope();

    /**
     * Vérifie si la contrainte actuelle est satisfaite par l'instanciation donnée.
     * @param map l'instanciation à vérifier
     * @throws IllegalArgumentException exception levée lorsque qu'une variable ne se trouve pas dans l'instanciation donnée
     * @return un booléen vérifiant la contrainte est satisfaite par l'instanciation ou non
     */
    public boolean isSatisfiedBy(Map<Variable, Object> map) throws IllegalArgumentException;
}