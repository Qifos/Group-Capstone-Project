/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Board {
    // Define board dimensions
    public static final int ROWS = 8;
    public static final int COLS = 8;

    // Canvas dimensions
    public static final int CELL_SIZE = 60; // Adjust as needed
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;

    // Grid line properties
    public static final int GRID_WIDTH = 2;
    public static final Color GRID_COLOR = Color.DARK_GRAY;

    // 2D array to represent the board
    public Cell[][] cells;

    // Constructor
    public Board() {
        initGame();
    }

    // Initialize the game board
    public void initGame() {
        cells = new Cell[ROWS][COLS];

        // Create cells
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }

        // Set up initial Othello board configuration
        cells[3][3].content = Seed.WHITE;
        cells[3][4].content = Seed.BLACK;
        cells[4][3].content = Seed.BLACK;
        cells[4][4].content = Seed.WHITE;
    }

    // Paint method for drawing the board
    public void paint(Graphics g) {
        // Draw grid lines
        g.setColor(GRID_COLOR);
        for (int row = 0; row <= ROWS; row++) {
            g.drawLine(0, row * CELL_SIZE, CANVAS_WIDTH, row * CELL_SIZE);
        }
        for (int col = 0; col <= COLS; col++) {
            g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, CANVAS_HEIGHT);
        }

        // Draw cells
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col].paint(g);
            }
        }
    }

    // Check if a move is valid
    public boolean isValidMove(Seed player, int row, int col) {
        // Check if cell is empty
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS || cells[row][col].content != Seed.NO_SEED)
            return false;

        Seed opponent = (player == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            boolean foundOpponent = false;

            while (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS) {
                if (cells[newRow][newCol].content == opponent) {
                    foundOpponent = true;
                } else if (cells[newRow][newCol].content == player && foundOpponent) {
                    return true;
                } else {
                    break;
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
        return false;
    }

    // Flip discs when a move is made
    public void flipDiscs(Seed player, int row, int col) {
        Seed opponent = (player == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            List<Cell> toFlip = new ArrayList<>();

            while (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS) {
                if (cells[newRow][newCol].content == opponent) {
                    toFlip.add(cells[newRow][newCol]);
                } else if (cells[newRow][newCol].content == player) {
                    // Flip all opponent discs in between
                    for (Cell cell : toFlip) {
                        cell.content = player;
                    }
                    break;
                } else {
                    break;
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
    }

    // Check game state
    public State checkGameState(Seed player) {
        int blackCount = 0;
        int whiteCount = 0;
        boolean hasValidMove = false;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.BLACK) blackCount++;
                if (cells[row][col].content == Seed.WHITE) whiteCount++;

                if (isValidMove(player, row, col)) {
                    hasValidMove = true;
                }
            }
        }

        if (!hasValidMove) {
            // Switch players and check again
            Seed nextPlayer = (player == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (isValidMove(nextPlayer, row, col)) {
                        return State.PLAYING;
                    }
                }
            }

            // No valid moves for either player
            return blackCount > whiteCount ? State.BLACK_WON :
                    (whiteCount > blackCount ? State.WHITE_WON : State.DRAW);
        }

        return State.PLAYING;
    }

    // Optional: Method to count discs for a specific player
    public int countDiscs(Seed player) {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == player) {
                    count++;
                }
            }
        }
        return count;
    }
}