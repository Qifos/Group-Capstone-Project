/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L; // To prevent serial warning

    // Define UI sizes
    public static final int CELL_SIZE = 60; // Cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    // Properties
    Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    Puzzle puzzle = new Puzzle();

    // Keep track of the conflicting cells
    private List<Cell> conflictingCells = new ArrayList<>();
    private List<Cell> lastHighlightedCells = new ArrayList<>();

    // Constructor
    public GameBoardPanel() {
        setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        // Create the grid of cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                add(cells[row][col]);
            }
        }

        // Add a common listener to all editable cells
        CellInputListener listener = new CellInputListener();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    // Start a new game
    public void newGame(int difficultyLevel) {
        // Generate a new puzzle based on difficulty level (e.g., number of blank cells)
        puzzle.newPuzzle(difficultyLevel);

        // Initialize all the 9x9 cells based on the generated puzzle
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    // Check if the puzzle is solved
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // reset background color
    private void resetCellBackgrounds() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (puzzle.numbers[row][col] == 0) {
                    cells[row][col].setBackground(Color.WHITE);
                } else if (cells[row][col].status == CellStatus.CORRECT_GUESS) {
                    cells[row][col].setBackground(Color.GREEN);
                }
            }
        }
    }

    // highlight conflict cell
    private void highlightConflictingCells(int row, int col, int value) {
        resetCellBackgrounds();

        for (Cell highlightedCell : lastHighlightedCells) {
            highlightedCell.setBackground(Color.WHITE); // Reset previous highlighted cells
        }
        lastHighlightedCells.clear();  // Clear the previous conflicting cells list

        boolean hasConflict = false;

        // Check for conflict
        for (int i = 0; i < SudokuConstants.GRID_SIZE; ++i) {
            if (puzzle.numbers[row][i] == value && i != col && puzzle.isGiven[row][i]) {
                cells[row][i].setBackground(Color.RED);
                lastHighlightedCells.add(cells[row][i]);
                hasConflict = true;
            }
        }

        for (int i = 0; i < SudokuConstants.GRID_SIZE; ++i) {
            if (puzzle.numbers[i][col] == value && i != row && puzzle.isGiven[i][col]) {
                cells[i][col].setBackground(Color.RED);
                lastHighlightedCells.add(cells[i][col]);
                hasConflict = true;
            }
        }
    }


    // Inner class to handle input in editable cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                if (numberIn == puzzle.numbers[sourceCell.getRow()][sourceCell.getCol()]) {
                    sourceCell.setStatus(CellStatus.CORRECT_GUESS);
                    sourceCell.updateAppearance();
                    SoundEffect.CORRECT_GUESS.play();
                    highlightConflictingCells(sourceCell.getRow(), sourceCell.getCol(), numberIn);
                } else {
                    sourceCell.setStatus(CellStatus.WRONG_GUESS);
                    sourceCell.updateAppearance();
                    SoundEffect.WRONG_GUESS.play();
                    highlightConflictingCells(sourceCell.getRow(), sourceCell.getCol(), numberIn);
                }

                // Check for solved puzzle
                if (isSolved()) {
                    JOptionPane.showMessageDialog(GameBoardPanel.this, "Congratulations! You've solved the puzzle!", "Puzzle Solved", JOptionPane.INFORMATION_MESSAGE);
                }
                } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GameBoardPanel.this, "Invalid input. Please enter a number between 1 and 9.", "Input Error", JOptionPane.ERROR_MESSAGE);
                sourceCell.setText(""); // Clear the input if invalid
            }
        }
    }
}

