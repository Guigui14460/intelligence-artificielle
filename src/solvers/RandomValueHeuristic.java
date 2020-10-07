package solvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import representation.Variable;

/**
 * Cette classe décrit une ordonnance aléatoire d'objets.
 */
public class RandomValueHeuristic implements ValueHeuristic {
    /**
     * Instance d'un objet permettant d'utiliser du pseudo-aléatoire.
     */
    private final Random random;

    /**
     * Constructeur par défaut.
     * @param random objet permettant d'utiliser de la génération pseudo-aléatoire
     */
    public RandomValueHeuristic(Random random) {
        this.random = random;
    }

    @Override
    public List<Object> ordering(Variable variable, Set<Object> domain) {
        List<Object> list = new ArrayList<>(domain);
        Collections.shuffle(list, this.random);
        return list;
    }
}
