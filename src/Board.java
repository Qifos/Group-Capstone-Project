/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

import java.awt.*;

/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 3;  // ROWS x COLS cells
    public static final int COLS = 3;
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /**
     * Composes of 2D array of ROWS-by-COLS Cell instances
     */
    Cell[][] cells;

    /**
     * Constructor to initialize the game board
     */
    public Board() {
        initGame();
    }

    /**
     * Initialize the game objects (run once)
     */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /**
     * Reset the game board, ready for new game
     */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     * The given player makes a move on (selectedRow, selectedCol).
     * Update cells[selectedRow][selectedCol]. Compute and return the
     * new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        // Update game board
        cells[selectedRow][selectedCol].content = player;

        // Check if the player has won or if it's a draw
        if (hasWon(player)) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else if (isDraw()) {
            return State.DRAW; // no empty cell, it's a draw
        } else {
            return State.PLAYING; // Game is still ongoing
        }
    }

    /**
     * Paint itself on the graphics canvas, given the Graphics context
     */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }

    // had to separate hasWon and isDraw method in order for the AI to work

    /**
     * Check if the given player has won the game.
     *
     * @param player The player to check (CROSS or NOUGHT).
     * @return true if the player has won, otherwise false.
     */
    public boolean hasWon(Seed player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (cells[i][0].content == player && cells[i][1].content == player && cells[i][2].content == player)
                return true;
            if (cells[0][i].content == player && cells[1][i].content == player && cells[2][i].content == player)
                return true;
        }
        // Check diagonals
        if (cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player)
            return true;
        if (cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player)
            return true;

        return false;
    }

    /**
     * Check if the game is a draw (no empty cells and no winner).
     *
     * @return true if the game is a draw, otherwise false.
     */
    public boolean isDraw() {
        // Check for any empty cells
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (cells[row][col].content == Seed.NO_SEED) return false; // Empty cell means not a draw
            }
        }
        return true; // No empty cells, so it's a draw
    }
}

