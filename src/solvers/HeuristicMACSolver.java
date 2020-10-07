package solvers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit un solveur permettant de résoudre un problème au sens de l'heuristique.
 */
public class HeuristicMACSolver extends AbstractSolver {
    /**
     * Heuristique sur les variables.
     */
    private final VariableHeuristic variableHeuristic;

    /**
     * Heuristique sur les valeurs.
     */
    private final ValueHeuristic valueHeuristic;

    /**
     * Constructeur par défaut.
     * 
     * @param variables   ensemble de variables
     * @param constraints ensemble de contraintes
     * @param variableHeuristic heuristique sur les variables
     * @param valueHeuristic heuristique sur les valeurs
     */
    public HeuristicMACSolver(Set<Variable> variables, Set<Constraint> constraints, VariableHeuristic variableHeuristic, ValueHeuristic valueHeuristic) {
        super(variables, constraints);
        this.variableHeuristic = variableHeuristic;
        this.valueHeuristic = valueHeuristic;
    }

    @Override
    public Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for(Variable var: this.variables){
            domains.put(var, var.getDomain());
        }
        
        for(Variable var: this.variables){
            Variable var2 = this.variableHeuristic.best(this.variables, domains);

            List<Object> domainList = this.valueHeuristic.ordering(var, var.getDomain());
        }
        return null;
    }

    /**
     * Récupère l'heuristique sur les variables.
     * @return heuristique sur les variables
     * @see #variableHeuristic
     */
    public VariableHeuristic getVariableHeuristic(){
        return this.variableHeuristic;
    }

    /**
     * Récupère l'heuristique sur les valeurs.
     * @return heuristique sur les valeurs
     * @see #valueHeuristic
     */
    public ValueHeuristic getValueHeuristic(){
        return this.valueHeuristic;
    }
}
