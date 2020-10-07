package representation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe décrit une règle du type "V1 implique V2".
 */
public class Rule implements Constraint {
    /**
     * Variables utilisées par la contrainte.
     */
    private final BooleanVariable v1, v2;

    /**
     * Booléen représentant la valeur de la variable associée.
     * {@code true} si la variable associée, {@code false} si c'est sa négation.
     * @see #v1
     * @see #v2
     */
    private final boolean v1Bool, v2Bool;
    
    /**
     * Constructeur par défaut.
     * @param v1 première variable de la contrainte
     * @param v1Bool booléen pour la valeur de la première variable de la contrainte
     * @param v2 seconde variable de la contrainte
     * @param v2Bool booléen pour la valeur de la seconde variable de la contrainte
     */
    public Rule(BooleanVariable v1, boolean v1Bool, BooleanVariable v2, boolean v2Bool){
        this.v1 = v1;
        this.v2 = v2;
        this.v1Bool = v1Bool;
        this.v2Bool = v2Bool;
    }

    @Override
    public String toString(){
        return "Rule[v1=" + this.v1 + ", v2=" + this.v2 + ", v1Bool=" + this.v1Bool + ", v2Bool=" + this.v2Bool + "]";
    }

    @Override
    public Set<Variable> getScope(){
        return new HashSet<Variable>(Arrays.asList(this.v1, this.v2));
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) throws IllegalArgumentException {
        // vérifie si l'affectation contient les deux variables de la contrainte
        if(!map.containsKey(this.v1) || !map.containsKey(this.v2)){
            throw new IllegalArgumentException("one of the variable is not contains in the set");
        }
        Boolean v1MapBool = (Boolean) map.get(this.v1);
        Boolean v2MapBool = (Boolean) map.get(this.v2);
        
        // on regarde si elles sont égales
        boolean diffv1 = this.v1Bool == v1MapBool;
        boolean diffv2 = this.v2Bool == v2MapBool;
        return !diffv1 || diffv2; // opérateur logique (implication)
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