package representation;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Cette classe décrit une variable de type booléenne.
 */
public class BooleanVariable extends Variable {
    /**
     * Constructeur par défaut.
     * 
     * @param name nom de la variable
     */
    public BooleanVariable(String name) {
        super(name, new HashSet<Object>(Arrays.asList(true, false)));
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        BooleanVariable object = (BooleanVariable) obj;
        return object.getDomain().equals(this.getDomain());
    }
}