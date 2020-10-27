package examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import planning.AStarPlanner;
import planning.Action;
import planning.BasicAction;
import planning.BasicGoal;
import planning.Goal;
import planning.Heuristic;
import planning.Planner;
import representation.BooleanVariable;
import representation.Constraint;
import representation.Rule;
import representation.Variable;

/**
 * Démonstration d'un planificateur sur l'exemple de la maison du fil rouge.
 */
public class HousePlannerDemo {
    /**
     * Méthode principale.
     * @param args arguments passés au terminal lors de l'exécution de cette classe
     */
    public static void main(String[] args){
        HouseExample house = new HouseExample(3, 4, 
            new HashSet<>(Arrays.asList("Cuisine 1", "Salle de bain 1", "Salle de bain 2")), 
            new HashSet<>(Arrays.asList("Chambre 1", "Chambre 2", "Chambre 3", "Salle à manger", "Salon"))
        );
        
        // variables de l'exemple
        BooleanVariable dalleCoulee = new BooleanVariable("Dalle coulée");
        BooleanVariable dalleCouleeSeche = new BooleanVariable("Dalle coulée sèche");
        BooleanVariable mursEleves = new BooleanVariable("Murs élevés");
        BooleanVariable toitureTerminee = new BooleanVariable("Toiture terminée");

        // contraintes de l'exemple
        Constraint c1 = new Rule(dalleCouleeSeche, true, dalleCoulee, true);
        Constraint c2 = new Rule(mursEleves, true, dalleCouleeSeche, true);
        Constraint c3 = new Rule(toitureTerminee, true, mursEleves, true);
    
        // on ajoute les vars et les cons au problème
        house.addConstraints(c1, c2, c3);
        house.addVariables(dalleCoulee, dalleCouleeSeche, mursEleves, toitureTerminee);

        // état initial
        Map<Variable, Object> initialState = new HashMap<>();
        initialState.put(dalleCoulee, false);

        // état but
        Map<Variable, Object> instanciationGoal = new HashMap<>();
        instanciationGoal.put(dalleCoulee, true);
        instanciationGoal.put(dalleCouleeSeche, true);
        instanciationGoal.put(mursEleves, true);
        instanciationGoal.put(toitureTerminee, true);
        Goal goal = new BasicGoal(instanciationGoal);

        Set<Action> actions = new HashSet<>();
        Map<Variable, Object> preconditions = new HashMap<>();
        Map<Variable, Object> effects = new HashMap<>();
        effects.put(dalleCoulee, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));
        effects.put(dalleCoulee, true);
        effects.put(dalleCouleeSeche, true);
        effects.put(mursEleves, true);
        effects.put(toitureTerminee, true);
        Action actionToChange = (Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 10);
        Action actionToChange2 = (Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 3);
        actions.add(actionToChange);
        preconditions.put(dalleCoulee, true);
        effects = new HashMap<>();
        effects.put(dalleCouleeSeche, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));
        preconditions.put(dalleCoulee, true);
        preconditions.put(dalleCouleeSeche, true);
        effects.put(mursEleves, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));
        preconditions.put(dalleCoulee, true);
        preconditions.put(dalleCouleeSeche, true);
        preconditions.put(mursEleves, true);
        effects.put(toitureTerminee, true);
        actions.add((Action) new BasicAction(new HashMap<>(preconditions), new HashMap<>(effects), 1));

        Planner planer = new AStarPlanner(initialState, actions, goal, new Heuristic(){
            @Override
            public float estimate(Map<Variable, Object> state) {
                return 0.0f;
            }
        });
        List<Action> plan = planer.plan();
        HousePlannerDemo.printPlan(plan, "Ma super villa");

        System.out.println("\n");

        actions.add(actionToChange2);
        plan = planer.plan();
        HousePlannerDemo.printPlan(plan, "Ma deuxième super villa");
    }

    /**
     * Affiche les caractéristiques du plan trouvé.
     * @param plan plan trouvé
     * @param houseName nom de la maison
     */
    public static void printPlan(List<Action> plan, String houseName){
        System.out.println("Plan pour construire la maison : " + houseName);
        System.out.println("Nombre d'actions à effectuer : " + plan.size());
        int cost = 0;
        for(Action action: plan){
            System.out.println(action);
            cost += action.getCost();
        }
        System.out.println("Coût du plan : " + cost);
    }
}