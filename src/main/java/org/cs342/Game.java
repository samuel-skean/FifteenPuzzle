package org.cs342;

import java.util.Arrays;

public class Game {
	
	static final int[] goalState = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	private int[] puzzle;
	
	Game(int[] p) {
		setPuzzle(p);
	}
	
	public synchronized int[] getPuzzle() {
		return copyArray(puzzle); // technically allows the puzzle to be modified, but lets not worry about that.
	}
	
	public synchronized void setPuzzle(int[] p) {
		puzzle = copyArray(p);
	}
	
	// Returns true if the move affected the state of the board, false otherwise.
	public boolean movePiece(int x, int y) { // X starts at 0 on the left, y starts at 0 at the top
		if ((x < 0) || (x > 3) || (y < 0) || (y > 3)) {
			return false; // these coordinates were out of bounds
		}
		
		int index = (y * 4) + x;
		
		if ((y != 0) && (puzzle[index - 4] == 0)) { // Empty space is above clicked space.
			moveUp(index);
		} else if ((x != 3) && puzzle[index + 1] == 0) { // Empty space is to the right of clicked space.
			moveRight(index);
		} else if ((y != 3) && (puzzle[index + 4] == 0)) { // Empty space is below clicked space.
			moveDown(index);
		} else if ((x != 0) && (puzzle[index - 1] == 0)) { // Empty space is to the left of clicked space.
			moveLeft(index);
		} else { // Empty space is not in an adjacent spot.
			return false;
		}
		return true;
	}
	
	public boolean goalTest() {
		return Arrays.equals(puzzle, goalState);
	}
	
	// Moves the number at index up, presuming the zero is already confirmed to be above it.
	private void moveUp(int index) {
		moveZero(index - 4, index);
	}
	

	// Moves the number at index right, presuming the zero is already confirmed to be to its right.
	private void moveRight(int index) {
		moveZero(index + 1, index);
	}
	
	// Moves the number at index down, presuming the zero is already confirmed to be below it.
	private void moveDown(int index) {
		moveZero(index + 4, index);
	}
	
	private void moveLeft(int index) {
		moveZero(index - 1, index);
	}
	
	private void moveZero(int zeroIndex, int moveToIndex) {
		puzzle[zeroIndex] = puzzle[moveToIndex];
		puzzle[moveToIndex] = 0;
	}
	
	private int[] copyArray(int[] arrayToCopy) {
		int[] copy = new int[arrayToCopy.length];
		
		for(int i= 0; i<copy.length; i++)
			copy[i] = arrayToCopy[i];
		
		return copy;
		
	}
}
