package solvers;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit une façon de récupérer la meilleure variable parmi un ensemble de variables.
 */
public class NbConstraintsVariableHeuristic implements VariableHeuristic {
    /**
     * Ensemble de variables.
     */
    private final Set<Variable> variables;

    /**
     * Ensemble de contraintes.
     */
    private final Set<Constraint> constraints;

    /**
     * Booléen représentant la préférence au niveau des variables.
     * S'il est à {@code true}, c'est que l'on préfère que les variables apparaissent dans le plus de contraintes possibles.
     * S'il est à {@code false}, c'est que l'on préfère que les variables apparaissent dans le moins de contraintes possibles.
     */
    private final boolean greatest;

    /**
     * Constructeur par défaut.
     * @param variables ensemble de variables
     * @param constraints ensemble de contraintes
     * @param greatest booléen représentant une préférence au niveau du choix de classement
     */
    public NbConstraintsVariableHeuristic(Set<Variable> variables, Set<Constraint> constraints,
            boolean greatest) {
        this.variables = variables;
        this.constraints = constraints;
        this.greatest = greatest;
    }

    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        Variable best = null;
        int val = 0;
        for(Variable var: variables){
            int actualValue = 0;
            for(Constraint c: this.constraints){
                if(c.getScope().contains(var)){
                    actualValue++;
                }
            }
            if(this.greatest){
                // on vérifie pour avoir le maximum
                if(actualValue > val){
                    val = actualValue;
                    best = var;
                }
            } else {
                // on vérifie s'il n'y a pas encore de variable
                // et s'il n'y en a pas, on fait la vérification pour avoir le minimum
                if(best == null || actualValue < val){
                    val = actualValue;
                    best = var;
                }
            }
        }
        return best;
    }

    /**
     * Retourne un booléen représentant la préférence au niveau des variables.
     * S'il est à {@code true}, c'est que l'on préfère que les variables apparaissent dans le plus de contraintes possibles.
     * S'il est à {@code false}, c'est que l'on préfère que les variables apparaissent dans le moins de contraintes possibles.
     * @return booléen
     * @see #greatest
     */
    public boolean isGreatest(){
        return this.greatest;
    }

    /**
     * Récupère l'ensemble des variables de l'instance.
     * @return ensemble des variables de l'instance
     * @see #variables
     */
    public Set<Variable> getVariables(){
        return this.variables;
    }

    /**
     * Ajoute des variables à l'ensemble de variables {@link #variables}.
     * @param variables variables à ajouter
     */
    public boolean addVariables(Variable... variables){
        return this.variables.addAll(Arrays.asList(variables));
    }

    /**
     * Enlève des variables à l'ensemble de variables {@link #variables}.
     * @param variables variables à enlever
     */
    public boolean removeVariables(Variable... variables){
        return this.variables.removeAll(Arrays.asList(variables));
    }

    /**
     * Récupère l'ensemble des contraintes de l'instance.
     * @return ensemble des contraintes de l'instance
     * @see #constraints
     */
    public Set<Constraint> getConstraints(){
        return this.constraints;
    }

    /**
     * Ajoute des contraintes à l'ensemble de contraintes {@link #constraints}.
     * @param constraints contraintes à ajouter
     */
    public boolean addConstraint(Constraint... constraints){
        return this.constraints.addAll(Arrays.asList(constraints));
    }

    /**
     * Enlève des contraintes à l'ensemble de contraintes {@link #constraints}.
     * @param constraints contraintes à enlever
     */
    public boolean removeConstraint(Constraint... constraints){
        return this.constraints.removeAll(Arrays.asList(constraints));
    }
}
