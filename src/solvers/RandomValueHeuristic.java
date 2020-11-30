package solvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe permet de faire une ordonnance aléatoire de valeurs.
 */
public class RandomValueHeuristic implements ValueHeuristic {
    /**
     * Instance d'un objet permettant d'utiliser un générateur pseudo-aléatoire.
     */
    private final Random random;

    /**
     * Constructeur par défaut.
     * 
     * @param random instance d'un objet permettant d'utiliser un générateur
     *               pseudo-aléatoire
     */
    public RandomValueHeuristic(final Random random) {
        this.random = random;
    }

    @Override
    public List<Object> ordering(Variable variable, Set<Object> domain) {
        if (domain == null) {
            return new ArrayList<>();
        }
        List<Object> list = new ArrayList<>(domain);
        Collections.shuffle(list, this.random);
        return list;
    }
}
