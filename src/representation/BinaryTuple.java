package representation;

import java.util.Objects;

/**
 * Cette classe décrit un couple de deux objets.
 */
public class BinaryTuple {
    /**
     * Les deux objets du couple.
     */
    private final Object val1, val2;

    /**
     * Constructeur par défaut.
     * 
     * @param val1 premier objet du couple
     * @param val2 second objet du couple
     */
    public BinaryTuple(final Object val1, final Object val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return "BinaryTuple[val1=" + this.val1 + ", val2=" + this.val2 + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.val1, this.val2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        BinaryTuple object = (BinaryTuple) obj;
        return object.val1.equals(this.val1) && object.val2.equals(this.val2);
    }

    /**
     * Retourne le premier objet stocké dans le couple.
     * 
     * @return le premier objet du couple
     * @see #val1
     */
    public Object getVal1() {
        return this.val1;
    }

    /**
     * Retourne le second objet stocké dans le couple.
     * 
     * @return le second objet du couple
     * @see #val2
     */
    public Object getVal2() {
        return this.val2;
    }
}