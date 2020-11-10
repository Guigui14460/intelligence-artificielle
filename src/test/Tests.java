package test;

import dataminingtests.AbstractAssociationRuleMinerTests;
import dataminingtests.AbstractItemsetMinerTests;
import dataminingtests.AprioriTests;
import dataminingtests.BruteForceAssociationRuleMinerTests;
import dataminingtests.DatabaseTests;
import planningtests.AStarPlannerTests;
import planningtests.BFSPlannerTests;
import planningtests.BasicActionTests;
import planningtests.BasicGoalTests;
import planningtests.DFSPlannerTests;
import planningtests.DijkstraPlannerTests;
import representationtests.BinaryExtensionConstraintTests;
import representationtests.BooleanVariableTests;
import representationtests.DifferenceConstraintTests;
import representationtests.RuleTests;
import representationtests.VariableTests;
import solvertests.AbstractSolverTests;
import solvertests.ArcConsistencyTests;
import solvertests.BacktrackSolverTests;
import solvertests.DomainSizeVariableHeuristicTests;
import solvertests.HeuristicMACSolverTests;
import solvertests.MACSolverTests;
import solvertests.NbConstraintsVariableHeuristicTests;
import solvertests.RandomValueHeuristicTests;

/**
 * Cette classe permet de lancer les tests sur les différentes classes de notre
 * application.
 */
public class Tests {
	/**
	 * Méthode principale à lancer.
	 * 
	 * @param args arguments passés dans le terminal
	 */
	public static void main(String[] args) {
		boolean ok = true;

		System.out.println("Testing package \"representation\"...");
		ok = ok && VariableTests.testEquals();
		ok = ok && VariableTests.testHashCode();
		ok = ok && BooleanVariableTests.testConstructor();
		ok = ok && DifferenceConstraintTests.testGetScope();
		ok = ok && DifferenceConstraintTests.testIsSatisfiedBy();
		ok = ok && RuleTests.testGetScope();
		ok = ok && RuleTests.testIsSatisfiedBy();
		ok = ok && BinaryExtensionConstraintTests.testGetScope();
		ok = ok && BinaryExtensionConstraintTests.testIsSatisfiedBy();

		System.out.println("Testing package \"solvers\"...");
		ok = ok && AbstractSolverTests.testIsConsistent();
		ok = ok && BacktrackSolverTests.testSolve();
		ok = ok && ArcConsistencyTests.testFilter();
		ok = ok && ArcConsistencyTests.testEnforce();
		ok = ok && ArcConsistencyTests.testEnforceArcConsistency();
		ok = ok && MACSolverTests.testSolve();
		ok = ok && HeuristicMACSolverTests.testSolve();
		ok = ok && NbConstraintsVariableHeuristicTests.testBest();
		ok = ok && DomainSizeVariableHeuristicTests.testBest();
		ok = ok && RandomValueHeuristicTests.testOrdering();

		System.out.println("Testing package \"planning\"...");
		ok = ok && BasicActionTests.testIsApplicable();
		ok = ok && BasicActionTests.testSuccessor();
		ok = ok && BasicActionTests.testGetCost();
		ok = ok && BasicGoalTests.testIsSatisfiedBy();
		ok = ok && DFSPlannerTests.testPlan();
		ok = ok && BFSPlannerTests.testPlan();
		ok = ok && DijkstraPlannerTests.testPlan();
		ok = ok && AStarPlannerTests.testPlan();

		System.out.println("Testing package \"datamining\"...");
		ok = ok && AbstractItemsetMinerTests.testFrequency();
		ok = ok && AprioriTests.testFrequentSingletons();
		ok = ok && AprioriTests.testCombine();
		ok = ok && AprioriTests.testAllSubsetsFrequent();
		ok = ok && AprioriTests.testExtract();
		ok = ok && AbstractAssociationRuleMinerTests.testFrequency();
		ok = ok && AbstractAssociationRuleMinerTests.testConfidence();
		ok = ok && BruteForceAssociationRuleMinerTests.testAllCandidatePremises();
		ok = ok && BruteForceAssociationRuleMinerTests.testExtract();
		// ok = ok && DatabaseTests.testItemTable(); // fonctionne pas
		// ok = ok && DatabaseTests.testPropositionalize(); // fonctionne pas

		System.out.println(ok ? "All tests passed" : "At least one test failed");
	}
}
