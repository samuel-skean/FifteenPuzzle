package skean.sam.cs342.fifteenpuzzleapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class MyTest {

	DB_Solver2 solver;
	int[] firstPuzzle = {14, 9, 13, 12, 11, 15, 5, 8, 7, 10, 4, 1, 0, 3, 6, 2};
	int[] secondPuzzle = {1, 2, 6, 3, 4, 5, 7, 11, 8, 15, 0, 14, 12, 9, 13, 10};
	int[] thirdPuzzle = {2, 6, 11, 10, 1, 7, 5, 3, 8, 4, 9, 15, 12, 13, 0, 14};
	int[] fourthPuzzle = {1, 2, 3, 0, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	
	Game g;
	
	private ArrayList<Node> runSolver(int[] puzzle, String heuristic) {
		solver = new DB_Solver2(new Node(puzzle), heuristic);
		Node finalState = solver.findSolutionPath();
		
		return solver.getSolutionPath(finalState);
	}
	
	@Test
	void testAISolverH1One() {
		
		assertEquals(224, runSolver(firstPuzzle, "heuristicOne").size());
	}
	
	@Test
	void testAISolverH2One() {
		assertEquals(312, runSolver(firstPuzzle, "heuristicTwo").size());
	}
	
	@Test
	void testAISolverH1Two() {
		assertEquals(175, runSolver(secondPuzzle, "heuristicOne").size());
	}
	
	@Test
	void testAISolverH2Two() {
		ArrayList<Node> solution = runSolver(secondPuzzle, "heuristicTwo");
		assertEquals(15, runSolver(secondPuzzle, "heuristicTwo").size());
		
		int[] expected13thStepInSolution = {1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		assertArrayEquals(expected13thStepInSolution, solution.get(13).getKey());
	}
	
	@Test
	void testAISolverH1Three() {
		assertEquals(96, runSolver(thirdPuzzle, "heuristicOne").size());
	}
	
	@Test
	void testAISolverH2Three() {
		assertEquals(15, runSolver(secondPuzzle, "heuristicTwo").size());
	}
	
	@Test
	void testMakingOutOfBoundsMovesOnGame() {
		Game g = new Game(thirdPuzzle);
		
		assertEquals(false, g.movePiece(0, -1));
		assertEquals(false, g.movePiece(4, 5));
		assertEquals(false, g.movePiece(0, -30));
		assertEquals(false, g.movePiece(6, 3));
		assertEquals(false, g.movePiece(-9834, -700));
		assertEquals(false, g.movePiece(20, 667));
	}
	
	@Test
	void testMakingMovesOnGameOne() {
		g = new Game(firstPuzzle);
		
		assertEquals(true, g.movePiece(0, 2));
		assertEquals(false, g.movePiece(0, 2));
		assertEquals(false, g.movePiece(3, 3));
		assertEquals(false, g.movePiece(0, 0));
		assertEquals(false, g.goalTest());
		assertEquals(false, g.movePiece(2, 2));
		assertEquals(false, g.movePiece(1, 3));
		assertEquals(true, g.movePiece(0, 3));
		assertEquals(true, g.movePiece(1, 3));
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(2, 3));
		
		int[] expectedPuzzle = {14, 9, 13, 12, 11, 15, 5, 8, 7, 10, 4, 1, 3, 6, 0, 2};
		assertArrayEquals(expectedPuzzle, g.getPuzzle());
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(3, 3));
		assertEquals(false, g.goalTest());
		
	}
	
	@Test
	void testMakingMovesOnGameTwo() {
		g = new Game(secondPuzzle);
		
		assertEquals(true, g.movePiece(2,3)); 
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(1,3));
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(0,3));
		assertEquals(true, g.movePiece(0,2));
		assertEquals(false, g.movePiece(0,0));
		assertEquals(true, g.movePiece(1,2));
		assertEquals(false, g.movePiece(3,2));
		assertEquals(true, g.movePiece(1,1));
		assertEquals(false, g.goalTest());

		int[] expectedPuzzle = {1, 2, 6, 3, 4, 0, 7, 11, 15, 5, 13, 14, 8, 12, 9, 10};
		assertArrayEquals(expectedPuzzle, g.getPuzzle());
	}
	
	@Test
	void testMakingMovesOnGameThree() {
		g = new Game(thirdPuzzle);
		
		assertEquals(false, g.movePiece(0,0));
		assertEquals(false, g.movePiece(2,0));
		assertEquals(true, g.movePiece(2,2));
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(2, 1));
		assertEquals(true, g.movePiece(3,1));
		assertEquals(true, g.movePiece(3,0));
		assertEquals(false, g.goalTest());
		
		int[] expectedPuzzle = {2, 6, 11, 0, 1, 7, 3, 10, 8, 4, 5, 15, 12, 13, 9, 14};
		assertArrayEquals(expectedPuzzle, g.getPuzzle());
	}
	
	@Test
	void testMakingMovesAndWinningOnGameFour() {
		g = new Game(fourthPuzzle);
		
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(2,0));
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(1,0));
		assertEquals(false, g.goalTest());
		assertEquals(true, g.movePiece(0,0));
		assertEquals(true, g.goalTest());
	}

}
