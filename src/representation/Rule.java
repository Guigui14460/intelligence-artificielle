package representation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe décrit une règle du type "var1 implique var2".
 */
public class Rule implements Constraint {
    /**
     * Variables utilisées par la contrainte.
     */
    private final BooleanVariable var1, var2;

    /**
     * Booléen représentant la valeur de la variable associée. {@code true} si la
     * variable associée, {@code false} si c'est sa négation.
     * 
     * @see #var1
     * @see #var2
     */
    private final boolean isPositive1, isPositive2;

    /**
     * Constructeur par défaut.
     * 
     * @param var1        première variable de la contrainte
     * @param isPositive1 booléen pour la valeur de la première variable de la
     *                    contrainte
     * @param var2        seconde variable de la contrainte
     * @param isPositive2 booléen pour la valeur de la seconde variable de la
     *                    contrainte
     */
    public Rule(final BooleanVariable var1, final boolean isPositive1, final BooleanVariable var2,
            final boolean isPositive2) {
        this.var1 = var1;
        this.var2 = var2;
        this.isPositive1 = isPositive1;
        this.isPositive2 = isPositive2;
    }

    @Override
    public String toString() {
        return "Rule[" + (this.isPositive1 ? "" : "!") + this.var1 + " => " + (this.isPositive2 ? "" : "!") + this.var2
                + "]";
    }

    @Override
    public Set<Variable> getScope() {
        return new HashSet<Variable>(Arrays.asList(this.var1, this.var2));
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) throws IllegalArgumentException {
        // vérifie si l'affectation contient les deux variables de la contrainte
        if (!map.containsKey(this.var1) || !map.containsKey(this.var2)) {
            throw new IllegalArgumentException("one of the variable is not contains in the set");
        }
        Boolean mapIsPositive1 = (Boolean) map.get(this.var1);
        Boolean mapIsPositive2 = (Boolean) map.get(this.var2);

        // on regarde si elles sont égales
        boolean diffvar1 = this.isPositive1 == mapIsPositive1;
        boolean diffvar2 = this.isPositive2 == mapIsPositive2;
        return !diffvar1 || diffvar2; // opérateur logique (implication)
    }

    /**
     * Retourne la première variable de la contrainte.
     * 
     * @return première variable de la contrainte
     * @see #var1
     */
    public Variable getFirstVariable() {
        return this.var1;
    }

    /**
     * Retourne la seconde variable de la contrainte.
     * 
     * @return seconde variable de la contrainte
     * @see #var2
     */
    public Variable getSecondVariable() {
        return this.var2;
    }
}