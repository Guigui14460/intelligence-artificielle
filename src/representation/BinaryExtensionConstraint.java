package representation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe décrit le fait de représenter des contraintes binaires en
 * extension.
 */
public class BinaryExtensionConstraint implements Constraint {
    /**
     * Extension de couple de la contrainte. Tout couple de valeur se trouvant dans
     * cette extension autorise les deux variables à prendre ces valeurs.
     */
    private final Set<BinaryTuple> extension = new HashSet<>();

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
    public BinaryExtensionConstraint(final Variable v1, final Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "BinaryExtensionConstraint[v1=" + this.v1 + ", v2=" + this.v2 + "]";
    }

    /**
     * Ajoute un couple de valeurs autorisé à l'ensemble en extension.
     * 
     * @param val1 première valeur du couple à ajouter
     * @param val2 seconde valeur du couple à ajouter
     */
    public void addTuple(Object val1, Object val2) {
        this.extension.add(new BinaryTuple(val1, val2));
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
        // récupération des valeurs associées aux variables dans l'instanciation
        Object first = map.get(this.v1);
        Object second = map.get(this.v2);

        for (BinaryTuple tuple : this.extension) {
            // on vérifie que les valeurs récupérées satisfont au moins un couple de valeurs
            if (first.equals(tuple.getVal1()) && second.equals(tuple.getVal2())) {
                return true;
            }
        }
        return false; // ne satisfont aucun couple
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

    /**
     * Retourne l'ensemble de couple de valeurs associé à la contrainte.
     * 
     * @return ensemble de couple de valeurs associé à la contrainte
     * @see #extension
     */
    public Set<BinaryTuple> getExtension() {
        return this.extension;
    }
}