package representation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe décrit le fait de représenter des contraintes binaires.
 */
public class BinaryConstraint implements Constraint {
    /**
     * Variables qui ne satisfont aucun couple de valeurs.
     */
    private final Variable v1, v2;

    /**
     * Constructeur par défaut.
     * 
     * @param v1 première variable de la contrainte
     * @param v2 seconde variable de la contrainte
     */
    public BinaryConstraint(final Variable v1, final Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "BinaryConstraint[" + this.v1 + " && " + this.v2 + "]";
    }

    @Override
    public Set<Variable> getScope() {
        return new HashSet<Variable>(Arrays.asList(this.v1, this.v2));
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) throws IllegalArgumentException {
        // vérifie si l'affectation contient les deux variables de la contrainte
        if (!map.containsKey(this.v1) || !map.containsKey(this.v2)) {
            throw new IllegalArgumentException("one of the variable is not contains in the set");
        }
        return map.get(this.v1) != null && map.get(this.v2) != null;
    }

    /**
     * Retourne la première variable de la contrainte.
     * 
     * @return première variable de la contrainte
     * @see #v1
     */
    public Variable getFirstVariable() {
        return this.v1;
    }

    /**
     * Retourne la seconde variable de la contrainte.
     * 
     * @return seconde variable de la contrainte
     * @see #v2
     */
    public Variable getSecondVariable() {
        return this.v2;
    }
}
