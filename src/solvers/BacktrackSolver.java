package solvers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit un solveur permettant de retourner en arrière et de
 * tester systématiquement l'ensemble des affectations potentielles du problème.
 */
public class BacktrackSolver extends AbstractSolver {
    /**
     * Constructeur par défaut.
     * 
     * @param variables   ensemble de variables
     * @param constraints ensemble de contraintes
     */
    public BacktrackSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        List<Variable> notInstanciatedVariables = new LinkedList<>(this.variables);
        return backtrack(new HashMap<>(), notInstanciatedVariables);
    }

    /**
     * Algorithme backtrack, Simple Retour Arrière (SRA). C'est un algorithme de
     * recherche de solutions à des problèmes computationnels, notamment pour les
     * problèmes de satisfaction de contraintes.
     * 
     * Cet algorithme permet de créer une instanciation satisfaisant toutes les
     * contraintes possibles du problème donné en fonction des variables et des
     * contraintes du problème. Une instanciation vide peut lui être passé ou même
     * une instanciation partielle. Le but de cet algorithme est de pouvoir repassé
     * à une instanciation plus ancienne en cas de non validité de la nouvelle. Ceci
     * peut être vu comme un parcours en profondeur d'un arbre (d'un graphe plus
     * généralement) visant à s'arrêter à la feuille à laquelle toutes les variables
     * sont instanciées et respectent toutes les contraintes du problème.
     * 
     * @param instanciation            instanciation vide ou partielle
     * @param notInstanciatedVariables liste de variables non instanciées
     * @return une instanciation partielle, totale ou vide (dépendant de l'exécution
     *         de l'agorithme sur les contraintes du problème)
     * @see AbstractSolver#variables
     * @see AbstractSolver#constraints
     */
    private Map<Variable, Object> backtrack(Map<Variable, Object> instanciation,
            List<Variable> notInstanciatedVariables) {
        // vérifie qu'il y a des variables non instanciées dans le problème
        if (notInstanciatedVariables.size() == 0 && this.isConsistent(instanciation)) {
            return instanciation;
        }

        // On crée une deuxième instanciation pour ne pas modifier l'ancien contexte et
        // on prend une variables non instanciée
        Variable chosenVariable = notInstanciatedVariables.get(0);
        List<Variable> newNotInstanciatedVariables = new LinkedList<>(notInstanciatedVariables);
        newNotInstanciatedVariables.remove(0);

        // On parcourt toutes les valeurs du domaine de la variable choisie
        for (Object value : chosenVariable.getDomain()) {
            // nouvelle instanciation pour garder le contexte
            Map<Variable, Object> instanciation2 = new HashMap<>(instanciation);
            instanciation2.put(chosenVariable, value);

            // vérifie que l'instanciation est valide
            if (this.isConsistent(instanciation2)) {
                if (instanciation2.keySet().containsAll(this.variables)) {
                    // toutes les variables sont instanciées
                    return instanciation2;
                }
                instanciation2 = this.backtrack(instanciation2, newNotInstanciatedVariables);
                if (instanciation2 != null) {
                    // solution trouvée
                    return instanciation2;
                }
            }
        }
        return null;
    }
}