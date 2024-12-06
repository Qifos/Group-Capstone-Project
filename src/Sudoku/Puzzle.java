package Sudoku;

import java.util.Random;

public class Puzzle {
    public static final int GRID_SIZE = 9; // 9x9 Sudoku
    public int[][] numbers; // Complete solution
    public boolean[][] isGiven; // Indicates whether a cell is a given number
    private Random random = new Random();

    public Puzzle() {
        numbers = new int[GRID_SIZE][GRID_SIZE];
        isGiven = new boolean[GRID_SIZE][GRID_SIZE];
        generateSolution();
    }

    private void generateSolution() {
        // Generate a complete and valid Sudoku solution
        // For simplicity, we use a predefined solution template
        int[][] baseSolution = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        // Copy the solution into the numbers array
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(baseSolution[i], 0, numbers[i], 0, GRID_SIZE);
        }
    }

    public void newPuzzle(int blanks) {
        // Reset the puzzle
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                isGiven[row][col] = true; // All cells start as given
            }
        }

        // Randomly remove numbers to create blanks
        int blankCount = 0;
        while (blankCount < blanks) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);

            if (isGiven[row][col]) {
                isGiven[row][col] = false; // Mark as a blank
                blankCount++;
            }
        }
    }
}
