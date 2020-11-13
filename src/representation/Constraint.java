package representation;

import java.util.Map;
import java.util.Set;

/**
 * Cette interface définit des méthodes par n'importe quelle contrainte
 * existante.
 */
public interface Constraint {
    /**
     * Récupère l'ensemble des variables sur laquelle porte la contrainte.
     * 
     * @return un ensemble de variables
     */
    public Set<Variable> getScope();

    /**
     * Vérifie si la contrainte est satisfaite par l'instanciation donnée.
     * 
     * @param map l'instanciation à vérifier
     * @throws IllegalArgumentException exception levée lorsque qu'une variable ne
     *                                  se trouve pas dans l'instanciation donnée
     * @return un booléen vérifiant la contrainte est satisfaite par l'instanciation
     *         ou non
     */
    public boolean isSatisfiedBy(Map<Variable, Object> map) throws IllegalArgumentException;
}