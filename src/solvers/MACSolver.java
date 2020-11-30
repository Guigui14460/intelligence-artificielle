package solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe est un solveur de contraintes qui vérifie l'arc-cohérence des
 * domaines de variables pour optimiser la recherche de solution.
 */
public class MACSolver extends AbstractSolver {
    /**
     * Constructeur par défaut.
     * 
     * @param variables   ensemble de variables
     * @param constraints ensemble de contraintes
     */
    public MACSolver(final Set<Variable> variables, final Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public final Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for (Variable variable : this.variables) {
            domains.put(variable, new HashSet<>(variable.getDomain()));
        }
        return this.mac(domains, new HashMap<>());
    }

    /**
     * Algorithme Maintaining Arc Consistency (MAC). Permet d'élaguer l'arbre de
     * recherche de solutions à des problème computationnels, notamment pour les
     * problèmes de satisfaction de contraintes.
     * 
     * Cet algorithme ressemble à celui utiliser dans la classe
     * {@link BacktrackSolver}. La seule différence est qu'ici, on vérifie
     * l'arc-cohérence des domaines en effectuant donc une filtration ce qui permet
     * de réduire le nombre potentiel de feuille à visiter avant de trouver une
     * solution au problème.
     * 
     * @param domains       domaines des variables
     * @param instanciation instanciation vide ou partielle
     * @return une instanciation partielle, totale ou vide (dépendant de l'exécution
     *         de l'agorithme sur les contraintes du problème)
     * @see AbstractSolver#variables
     * @see AbstractSolver#constraints
     * @see ArcConsistency#enforceArcConsistency(Map)
     */
    private Map<Variable, Object> mac(Map<Variable, Set<Object>> domains, Map<Variable, Object> instanciation) {
        List<Variable> notInstanciatedVariables = new ArrayList<>();
        for (Variable variable : this.variables) {
            if (!instanciation.containsKey(variable))
                notInstanciatedVariables.add(variable);
        }

        if (notInstanciatedVariables.isEmpty() && this.isConsistent(instanciation)) {
            return instanciation;
        }

        // copie des domaines et arc-consistance de celui-ci
        Map<Variable, Set<Object>> domainsCopy = new HashMap<>(domains);
        ArcConsistency arc = new ArcConsistency(this.constraints);
        boolean result = arc.enforceArcConsistency(domainsCopy);
        if (!result) {
            return null;
        }

        // choisi la première variable parmi celles non instanciées
        Variable variableNotInstanciated = notInstanciatedVariables.get(0);

        // on parcourt toutes les valeurs du domaine de la variable choisie
        for (Object value : domainsCopy.get(variableNotInstanciated)) {
            // nouvelle instanciation pour garder le contexte
            Map<Variable, Object> instanciation2 = new HashMap<>(instanciation);
            instanciation2.put(variableNotInstanciated, value);

            // vérifie que l'instanciation est valide
            if (this.isConsistent(instanciation2)) {
                if (instanciation2.keySet().containsAll(this.variables)) {
                    // toutes les variables sont instanciées
                    return instanciation2;
                }
                instanciation2 = this.mac(domainsCopy, instanciation2);
                if (instanciation2 != null) {
                    // solution trouvée
                    return instanciation2;
                }
            }
        }
        return null; // pas de solution
    }
}
