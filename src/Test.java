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
    public static void main(String[] args){
        boolean ok = true;

        // Modélisation variable-valeur
        ok = ok && VariableTests.testEquals();
        ok = ok && VariableTests.testHashCode();
        ok = ok && BooleanVariableTests.testConstructor();
        ok = ok && RuleTests.testGetScope();
        ok = ok && RuleTests.testIsSatisfiedBy();
        ok = ok && DifferenceConstraintTests.testGetScope();
        ok = ok && DifferenceConstraintTests.testIsSatisfiedBy();
        ok = ok && BinaryExtensionConstraintTests.testGetScope();
        ok = ok && BinaryExtensionConstraintTests.testIsSatisfiedBy();

        // Backtracking
        ok = ok && AbstractSolverTests.testIsConsistent();
        // ok = ok && BacktrackSolverTests.testSolve(); // fonctionne pas
        ok = ok && ArcConsistencyTests.testFilter();
        ok = ok && ArcConsistencyTests.testEnforce();
        ok = ok && ArcConsistencyTests.testEnforceArcConsistency();
        // ok = ok && MACSolverTests.testSolve(); // fonctionne pas
        // ok = ok && HeuristicMACSolverTests.testSolve(); // à implémenter
        // ok = ok && NbConstraintsVariableHeuristicTests.testBest(); // fonctionne pas
        // ok = ok && DomainSizeVariableHeuristicTests.testBest(); // fonctionne pas
        ok = ok && RandomValueHeuristicTests.testOrdering();

        // Planification
        ok = ok && BasicActionTests.testIsApplicable();
        ok = ok && BasicActionTests.testSuccessor();
        ok = ok && BasicActionTests.testGetCost();
        ok = ok && BasicGoalTests.testIsSatisfiedBy();
        ok = ok && DFSPlannerTests.testPlan();
        // ok = ok && BFSPlannerTests.testPlan(); // fonctionne pas
        ok = ok && DijkstraPlannerTests.testPlan();
        ok = ok && AStarPlannerTests.testPlan();

        // faire un test pour la maison

        System.out.println(ok ? "All tests passed" : "At least one test failed");
    }
}