package solvers;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe abstraite décrit la base d'implémentation d'un solveur de
 * contraintes.
 */
public abstract class AbstractSolver implements Solver {
    /**
     * Ensemble de variables du problème.
     */
    protected final Set<Variable> variables;

    /**
     * Ensemble de contraintes à satisfaire par le solveur.
     */
    protected final Set<Constraint> constraints;

    /**
     * Constructeur par défaut.
     * 
     * @param variables   ensemble de variables du problème
     * @param constraints ensemble de contraintes à satisfaire
     */
    public AbstractSolver(Set<Variable> variables, Set<Constraint> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

    /**
     * Retourne si l'affectation passée en argument vérifie toutes les contraintes
     * qui portent sur les variables.
     * 
     * @param affectation affectation partielle
     * @return si l'affectation est cohérente avec les contraintes et les variables
     */
    public boolean isConsistent(Map<Variable, Object> affectation) {
        for (Constraint constraint : this.constraints) {
            // vérifie que toutes les variables de la contrainte se trouvent dans
            // l'affectation
            // (ici contraposée => l'ensemble des variables de la contraintes sont inclues
            // dans l'ensemble de clés de l'instanciation)
            if (affectation.keySet().containsAll(constraint.getScope())) {
                if (!constraint.isSatisfiedBy(affectation)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Récupère l'ensemble des variables de l'instance.
     * 
     * @return ensemble des variables de l'instance
     * @see #variables
     */
    public Set<Variable> getVariables() {
        return this.variables;
    }

    /**
     * Ajoute des variables à l'ensemble de variables {@link #variables}.
     * 
     * @param variables variables à ajouter
     */
    public boolean addVariables(Variable... variables) {
        return this.variables.addAll(Arrays.asList(variables));
    }

    /**
     * Enlève des variables à l'ensemble de variables {@link #variables}.
     * 
     * @param variables variables à enlever
     */
    public boolean removeVariables(Variable... variables) {
        return this.variables.removeAll(Arrays.asList(variables));
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
