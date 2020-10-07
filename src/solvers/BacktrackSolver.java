package solvers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Cette classe décrit un solveur permettant de retourner en arrière et de tester systématiquement l'ensemble des affectations potentielles du problème.
 */
public class BacktrackSolver extends AbstractSolver {
    /**
     * Constructeur par défaut.
     * @param variables ensemble de variables
     * @param constraints ensemble de contraintes
     */
    public BacktrackSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public boolean isConsistent(Map<Variable, Object> affectation){
        for(Constraint constraint: this.constraints){
            // vérifie que toutes les variables sont instanciées
            if(constraint.getScope().equals(affectation.keySet())){
                if(!constraint.isSatisfiedBy(affectation)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Map<Variable, Object> solve() {
        List<Variable> notInstanciatedVariables = new LinkedList<>();
        notInstanciatedVariables.addAll(this.variables);
        return backtrack(new HashMap<>(), notInstanciatedVariables);
    }
    
    /**
     * Algorithme backtrack, Simple Retour Arrière (SRA).
     * C'est un algorithme de recherche de solutions à des problèmes computationnels, 
     * notamment pour les problèmes de satisfaction de contraintes.
     * 
     * Cet algorithme permet de créer une instanciation satisfaisant toutes les 
     * contraintes possibles du problème donné en fonction des variables et des 
     * contraintes du problème. Une instanciation vide peut lui être passé ou 
     * même une instanciation partielle. Le but de cet algorithme est de pouvoir
     * repassé à une instanciation plus ancienne en cas de non validité de la nouvelle.
     * Ceci peut être vu comme un parcours en profondeur d'un arbre (d'un graphe
     * plus généralement) visant à s'arrêter à la feuille à laquelle toutes les 
     * variables sont instanciées et respectent toutes les contraintes du problème.
     * 
     * @param instanciation instanciation vide ou partielle
     * @param notInstanciatedVariables liste de variables non instanciées
     * @return une instanciation partielle, totale ou vide (dépendant de l'exécution 
     * de l'agorithme sur les contraintes du problème)
     * @see AbstractSolver#variables
     * @see AbstractSolver#constraints
     */
    public Map<Variable, Object> backtrack(Map<Variable, Object> instanciation, List<Variable> notInstanciatedVariables){
        // vérifie qu'il y a des variables non instanciées dans le problème
        if(notInstanciatedVariables.size() == 0){
            return instanciation;
        }
        // choisi la première variable parmi celle non instanciées
        Variable chosenVariable = notInstanciatedVariables.remove(0);

        // On crée une deuxième instanciation pour ne pas modifier l'ancien contexte
        
        // On parcourt toutes les valeurs du domaine de la variable choisie
        for(Object value: chosenVariable.getDomain()){
            Map<Variable, Object> instanciation2 = new HashMap<>();
            instanciation2.putAll(instanciation);
            instanciation2.put(chosenVariable, value);
            // vérifie que l'instanciation est valide
            if(isConsistent(instanciation2)){
                if(this.variables.size() == instanciation2.keySet().size()){
                    // toutes les variables sont instanciées
                    return instanciation2;
                }
                else {
                    instanciation2 = backtrack(instanciation2, notInstanciatedVariables);
                    if(instanciation2 != null){
                        // on la prend
                        return instanciation2;
                    }
                    // on la rejette
                }
            }
        }
        // on remet la variable choisie pour ne pas la perdre
        notInstanciatedVariables.add(chosenVariable);
        return null;
    }
}
