package representation;

import java.util.Objects;
import java.util.Set;

/**
 * Cette classe décrit une variable utilisable.
 */
public class Variable {
    /**
     * Nom de la variable.
     */
    public final String name;

    /**
     * Ensemble de valeurs possible pour instancier cette variable.
     */
    private final Set<Object> domain;

    /**
     * Conctructeur par défaut.
     * 
     * @param name   nom de la variable
     * @param domain ensemble de valeurs possibles pour instancier la variable
     */
    public Variable(String name, Set<Object> domain) {
        this.name = name;
        this.domain = domain;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[name=" + this.name + ", domain=" + this.domain + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Variable object = (Variable) obj;
        return object.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    /**
     * Retourne le nom de la variable.
     * 
     * @return nom de la variable
     * @see #name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retourne l'ensemble des valeurs possibles pour instancier la variable.
     * 
     * @return l'ensemble des valeurs possibles pour instancier la variable
     * @see #domain
     */
    public Set<Object> getDomain() {
        return this.domain;
    }
}