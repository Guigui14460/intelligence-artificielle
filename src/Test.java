import planningtests.*;
import representationtests.*;
import solvertests.*;

/**
 * Cette classe permet de lancer les tests sur les différentes classes de notre application.
 */
public class Test {
    /**
     * Méthode principale à lancer.
     * @param args arguments passés dans le terminal
     */
    public static void main(String[] args) {
        // tester sur notre exemple avec comparaison des algorithmes (solveurs et de planification)
        // faire un test pour la maison
        
        boolean ok = true;

        // Partie 1 : Modélisation variable-valeur
        ok = ok && VariableTests.testEquals();
        ok = ok && VariableTests.testHashCode();
        ok = ok && BooleanVariableTests.testConstructor();
        ok = ok && RuleTests.testGetScope();
        ok = ok && RuleTests.testIsSatisfiedBy();
        ok = ok && DifferenceConstraintTests.testGetScope();
        ok = ok && DifferenceConstraintTests.testIsSatisfiedBy();
        ok = ok && BinaryExtensionConstraintTests.testGetScope();
        ok = ok && BinaryExtensionConstraintTests.testIsSatisfiedBy();

        // Partie 2 : Solveurs
        ok = ok && AbstractSolverTests.testIsConsistent();
        ok = ok && BacktrackSolverTests.testSolve();
        ok = ok && ArcConsistencyTests.testFilter();
        ok = ok && ArcConsistencyTests.testEnforce();
        ok = ok && ArcConsistencyTests.testEnforceArcConsistency();
        // ok = ok && MACSolverTests.testSolve(); // à implémenter
        // ok = ok && HeuristicMACSolverTests.testSolve(); // à implémenter
        ok = ok && NbConstraintsVariableHeuristicTests.testBest();
        ok = ok && DomainSizeVariableHeuristicTests.testBest();
        ok = ok && RandomValueHeuristicTests.testOrdering();

        // Partie 3 : Planification
        ok = ok && BasicActionTests.testIsApplicable();
        ok = ok && BasicActionTests.testSuccessor();
        ok = ok && BasicActionTests.testGetCost();
        ok = ok && BasicGoalTests.testIsSatisfiedBy();
        ok = ok && DFSPlannerTests.testPlan();
        ok = ok && BFSPlannerTests.testPlan();
        ok = ok && DijkstraPlannerTests.testPlan();
        ok = ok && AStarPlannerTests.testPlan();

        System.out.println(ok ? "All tests passed" : "At least one test failed");
    }
}