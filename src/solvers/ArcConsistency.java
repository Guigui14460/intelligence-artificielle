package solvers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit une arc-consistence locale sur des contraintes.
 */
public class ArcConsistency {
    /**
     * Ensemble de contraintes à satisfaire.
     */
    private final Set<Constraint> constraints;

    /**
     * Constructeur par défaut.
     * 
     * @param constraints ensemble de contraintes
     */
    public ArcConsistency(Set<Constraint> constraints) {
        this.constraints = constraints;
    }

    /**
     * Supprime les valeurs qui ne sont pas arc-cohérente avec la contrainte
     * {@code constraint}.
     * 
     * @param var1       première variable
     * @param var1Domain domain de la première variable
     * @param var2       seconde variable
     * @param var2Domain domain de la seconde variable
     * @param constraint contrainte qui porte sur les deux variables {@code var1} et
     *                   {@code var2}
     * @return booléen qui détermine si le domaine a changé
     */
    public static boolean filter(Variable var1, Set<Object> var1Domain, Variable var2, Set<Object> var2Domain,
            Constraint constraint) {
        Map<Variable, Object> i = new HashMap<>(); // instanciation
        Set<Object> objectsToDelete = new HashSet<>(); // poubelle
        for (Object obj1 : var1Domain) {
            i.put(var1, obj1);
            boolean toDelete = true;
            for (Object obj2 : var2Domain) {
                i.put(var2, obj2);
                toDelete = toDelete && !constraint.isSatisfiedBy(i);
            }
            if (toDelete) { // ajout d'une valeur du domain D(var1) à la poubelle
                objectsToDelete.add(obj1);
            }
        }
        var1Domain.removeAll(objectsToDelete);
        return !objectsToDelete.isEmpty(); // vérifie que la poubelle n'est pas vide (=> domaine a été changé)
    }

    /**
     * Rend tous les domaines arc-cohérents.
     * 
     * @param constraint contrainte
     * @param domains    domaines des variables concernées par la contrainte
     * @return booléen qui détermine si au moins un des domaines a été modifié
     */
    public static boolean enforce(Constraint constraint, Map<Variable, Set<Object>> domains) {
        // permet de récupérer les deux variables de la contrainte
        Iterator<Variable> iterator = constraint.getScope().iterator();
        Variable var1, var2;
        if (!iterator.hasNext()) { // on vérifie qu'il y a la première variable
            return false;
        }
        var1 = iterator.next();
        if (!domains.keySet().contains(var1)) { // on vérifie la première variable est dans les domaines
            return false;
        }
        if (!iterator.hasNext()) { // on vérifie qu'il y a la seconde variable
            return false;
        }
        var2 = iterator.next();
        if (!domains.keySet().contains(var2)) { // on vérifie la seconde variable est dans les domaines
            return false;
        }
        if (iterator.hasNext()) { // on vérifie qu'il n'y a pas d'autres variables
            return false;
        }
        // on filtre pour les couples (var1, var2) et (var2, var1)
        boolean filter1 = ArcConsistency.filter(var1, domains.get(var1), var2, domains.get(var2), constraint);
        boolean filter2 = ArcConsistency.filter(var2, domains.get(var2), var1, domains.get(var1), constraint);
        return filter1 || filter2; // on regarde si au moins l'un des deux domaines a changé
    }

    /**
     * Rend tous les domaines arc-cohérents avec les contraintes stockées dans
     * l'attribut {@link #constraints}.
     * 
     * @param domains domaines des variables concernées par la contrainte
     * @return booléen qui détermine si au moins un des domaines a été modifié
     */
    public boolean enforceArcConsistency(Map<Variable, Set<Object>> domains) {
        boolean hasChanged1TimeAtLeast = false, enforceHasChanged;
        // rend les domaines arc-cohérents
        do {
            enforceHasChanged = false;
            for (Constraint constraint : this.constraints) {
                boolean b = ArcConsistency.enforce(constraint, domains);
                enforceHasChanged = enforceHasChanged || b;
                hasChanged1TimeAtLeast = hasChanged1TimeAtLeast || b;
            }
        } while (enforceHasChanged); // jusqu'à qu'il n'y est plus aucune modification sur les domaines

        // vérifie si tous les domaines sont non vides
        for (Set<Object> domain : domains.values()) {
            if (domain.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Récupère l'ensemble des contraintes de l'instance.
     * 
     * @return ensemble des contraintes de l'instance
     * @see #constraints
     */
    public Set<Constraint> getConstraints() {
        return this.constraints;
    }

    /**
     * Ajoute des contraintes à l'ensemble de contraintes {@link #constraints}.
     * 
     * @param constraints contraintes à ajouter
     */
    public boolean addConstraint(Constraint... constraints) {
        return this.constraints.addAll(Arrays.asList(constraints));
    }

    /**
     * Enlève des contraintes à l'ensemble de contraintes {@link #constraints}.
     * 
     * @param constraints contraintes à enlever
     */
    public boolean removeConstraint(Constraint... constraints) {
        return this.constraints.removeAll(Arrays.asList(constraints));
    }
}
