package org.cs342;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;


public class GameScreenController implements Initializable {
	static final int[][] puzzles = {{1, 2, 3, 0, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
									{0, 14, 13, 12, 15, 9, 5, 8, 11, 7, 4, 1, 3, 10, 6, 2},
									{2, 6, 10, 3, 1, 4, 7, 11, 8, 5, 9, 15, 12, 13, 14, 0},
									{14, 9, 13, 12, 11, 15, 5, 8, 7, 10, 4, 1, 0, 3, 6, 2},
									{1, 2, 6, 3, 4, 5, 7, 11, 8, 15, 0, 14, 12, 9, 13, 10},
									{2, 6, 11, 10, 1, 7, 5, 3, 8, 4, 9, 15, 12, 13, 0, 14},
									{6, 7, 9, 11, 2, 0, 5, 3, 1, 8, 10, 15, 12, 4, 13, 14},
									{4, 0, 3, 11, 1, 6, 10, 9, 2, 8, 14, 7, 12, 5, 13, 15},
									{11, 14, 13, 12, 9, 1, 0, 5, 7, 15, 10, 8, 3, 6, 4, 2},
									{7, 1, 13, 12, 4, 8, 0, 10, 11, 6, 14, 5, 3, 9, 15, 2}}; // preset puzzles that get chosen from at random when the user presses "New org.cs342.Game"
	
	@FXML
	GridPane gameBoard;
	
	@FXML
	Button h1SolverButton;
	
	@FXML
	Button h2SolverButton;
	
	@FXML
	Button seeTheSolutionButton;
	
	@FXML
	Label messageText;
	
	@FXML
	HBox gamePlayPanel;
	
	@FXML
	HBox winPanel;
	
	GameButton[][] gridOfGameButtons = new GameButton[4][4];
	
	Game theGame;
	
	int[] curPuzzleStartState;
	
	ExecutorService executor = Executors.newFixedThreadPool(2);
	
	Future<?> solverFuture;
	
	ArrayList<Node> solverResult;
	
	int currMoveIndexInSolverResult;
	
	boolean isCurrentlyShowingSolution = false; // reflects whether the app is currently in the process of showing the solution to the user
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		EventHandler<ActionEvent> gameButtonAction = e -> {
			GameButton buttonPressed = (GameButton) e.getSource();
			
			if (theGame.movePiece(buttonPressed.getX(), buttonPressed.getY())) {
				updateButtons();
				if (isCurrentlyShowingSolution) {
					messageText.setText("Showing the moves was interrupted when you moved\na piece of the puzzle.");
				} else {
					messageText.setText("");
				}
				makeReadyToSolve();
			}
			if (theGame.goalTest()) {
				gameWon();
			}
		};
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				GameButton b = new GameButton(i, j);
				b.setPrefWidth(30);
				b.setOnAction(gameButtonAction);
				gridOfGameButtons[i][j] = b;
				gameBoard.add(b, i, j);
			}
		}
		
		newGame();
		
		winPanel.setVisible(false);
		winPanel.setManaged(false);
		
		seeTheSolutionButton.setDisable(true);
		
		currMoveIndexInSolverResult = -1;
	}
	
	private void gameWon() {
		gamePlayPanel.setVisible(false);
		gamePlayPanel.setManaged(false);
		
		winPanel.setVisible(true);
		winPanel.setManaged(true);
		
		messageText.setText("");
		
	}
	
	public void newGame() {
		Random random = new Random();
		curPuzzleStartState = puzzles[random.nextInt(10)];
		theGame = new Game(curPuzzleStartState);
		isCurrentlyShowingSolution = false;
		messageText.setText("");
		updateButtons();
		makeReadyToSolve();
	}
	
	public void newGameFromWin() {
		winPanel.setVisible(false);
		winPanel.setManaged(false);
		
		gamePlayPanel.setVisible(true);
		gamePlayPanel.setManaged(true);
		
		newGame();
	}
	
	public void exitGame() {
		Platform.exit();
	}
	
	public void resetGame() {
		theGame = new Game(curPuzzleStartState);
		isCurrentlyShowingSolution = false;
		messageText.setText("");
		updateButtons();
		makeReadyToSolve();
	}
	
	public void solveWithH1() {
		messageText.setText("Solving the puzzle with heuristic 1, \"Tiles out of place\".\nThis may take a while.");
		solveWithHeuristic("heuristicOne");
		h1SolverButton.setDisable(true);
		h2SolverButton.setDisable(true);
	}
	
	public void solveWithH2() {
		messageText.setText("Solving the puzzle with heuristic 2, \"Manhattan Distance\".\nThis may take a while.");
		solveWithHeuristic("heuristicTwo");
		h2SolverButton.setDisable(true);
		h1SolverButton.setDisable(true);
	}
	
	private void solveWithHeuristic(String heuristic) {
		if (solverFuture != null) {
			solverFuture.cancel(true); // cancel any ongoing solution attempts
		}

		solverFuture = executor.submit(new Callable<Void>() {
			public Void call() {
				
				DB_Solver2 solver = new DB_Solver2(new Node(theGame.getPuzzle()), heuristic);
				Node finalNode = solver.findSolutionPath();
				if (finalNode == null) {
					System.out.println("No solution found - puzzle was corrupted."); // This should never happen
						// because all my preset puzzles are solvable.
					return null;
				}
				ArrayList<Node> sR = solver.getSolutionPath(finalNode);
				Platform.runLater(() -> {makeReadyToShowSolution(sR);});
				return null;
			}
		});
		seeTheSolutionButton.setDisable(true);
	}
	
	private void makeReadyToShowSolution(ArrayList<Node> sR) {
		if ((solverFuture != null) && (!solverFuture.isCancelled())) {
			try {
				solverResult = sR;
				currMoveIndexInSolverResult = 1;
				seeTheSolutionButton.setDisable(false);
				messageText.setText("The solution is available, and can be shown, 10 moves at a time,\nby pressing the \"See the Solution\" button.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void seeTheSolution() {
		if (solverResult != null) {
			messageText.setText("Showing 10 moves of the solution...");
			isCurrentlyShowingSolution = true;
			seeTheSolutionButton.setDisable(true);
			showNMovesOfSolution(10);
		} else {
			System.out.println("Solver result was null");
		}
	}
	
	//
	// This method is called whenever the program should be put into a state where the user should be able to initiate a solver.
	// This happens when the game board changes state.
	//
	private void makeReadyToSolve() {
		isCurrentlyShowingSolution = false; // stop the solution from being shown if it is currently being shown
		if (solverFuture != null) {
			solverFuture.cancel(true); // cancel any ongoing solution attempts because they will no longer be valid since the game board just changed.
		}
		seeTheSolutionButton.setDisable(true);
		h1SolverButton.setDisable(false);
		h2SolverButton.setDisable(false);
		currMoveIndexInSolverResult = -1;
	}
	
	private void updateButtons() {
		int[] puzzle = theGame.getPuzzle();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				gridOfGameButtons[i][j].setNumber(puzzle[(j * 4) + i]);
			}
		}
		
		
	}
	
	public void showNMovesOfSolution(int n) {
		if (!isCurrentlyShowingSolution) {
			return;
		}
		if ((n == 0) || (currMoveIndexInSolverResult < 0) || (currMoveIndexInSolverResult >= solverResult.size())) {
			isCurrentlyShowingSolution = false;
			return;
		}
		theGame.setPuzzle(solverResult.get(currMoveIndexInSolverResult++).getKey());
		updateButtons();
		if (currMoveIndexInSolverResult == solverResult.size()) {
			isCurrentlyShowingSolution = false;
			gameWon();
			return;
		}
		if (n == 1) { // the last of a set of 10 moves, but there are still moves in the solution to show on subsequent button presses
			isCurrentlyShowingSolution = false;
			seeTheSolutionButton.setDisable(false); // allow the user to show the next 10 moves
			messageText.setText("Showed you 10 moves of the solution, but more can be seen\nby pressing the \"See the Solution\" button.\nOr, you can attempt to solve the puzzle on your own from here.");
			return;
		}
		PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
		pause.setOnFinished(e -> showNMovesOfSolution(n-1));
		pause.play();
	}

}
