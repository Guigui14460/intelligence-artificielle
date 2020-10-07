package representation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe décrit une contrainte de type "différente".
 */
public class DifferenceConstraint implements Constraint {
    /**
     * Variables utilisées par la contrainte.
     */
    private final Variable v1, v2;

    /**
     * Constructeur par défaut.
     * @param v1 première variable
     * @param v2 deuxième variable
     */
    public DifferenceConstraint(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public String toString(){
        return "DifferenceConstraint[v1=" + this.v1 + ", v2=" + this.v2 + "]";
    }

    @Override
    public Set<Variable> getScope() {
        return new HashSet<Variable>(Arrays.asList(this.v1, this.v2));
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) throws IllegalArgumentException {
        // vérifie si l'affectation contient les deux variables de la contrainte
        if(!map.containsKey(this.v1) || !map.containsKey(this.v2)){
            throw new IllegalArgumentException("one of the variable is not contains in the set");
        }
        // simple opération logique (!=)
        return !map.get(this.v1).equals(map.get(this.v2));
    }

    /**
     * Retourne la première variable de la contrainte.
     * @return première variable de la contrainte
     * @see #v1
     */
    public Variable getFirstVariable(){
        return this.v1;
    }

    /**
     * Retourne la seconde variable de la contrainte.
     * @return seconde variable de la contrainte
     * @see #v2
     */
    public Variable getSecondVariable(){
        return this.v2;
    }
}