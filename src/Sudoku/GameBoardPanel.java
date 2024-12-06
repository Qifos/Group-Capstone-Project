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
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();

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

    // Helper method to reset background colors
    private void resetCellBackgrounds() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                // Only reset non-blank cells to their original color (keep yellow for non-conflicting guesses)
                if (puzzle.numbers[row][col] == 0) {
                    cells[row][col].setBackground(Color.WHITE); // Reset blank cells to white
                } else if (cells[row][col].status == CellStatus.CORRECT_GUESS) {
                    cells[row][col].setBackground(Color.GREEN); // Keep correct guesses green
                }
            }
        }
    }

    // Helper method to highlight cells with a specific color
    private void highlightCellsWithValue(int value, Color color) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (puzzle.numbers[row][col] == value && puzzle.isGiven[row][col] != true) { // Exclude given numbers
                    cells[row][col].setBackground(color); // Highlight cells with the guessed value
                }
            }
        }
    }

    // Helper method to highlight conflicting cells
    private void highlightConflictingCells(int row, int col, int value) {
        // Reset previous highlights
        resetCellBackgrounds();

        // Clear previous highlights
        for (Cell highlightedCell : lastHighlightedCells) {
            highlightedCell.setBackground(Color.WHITE); // Reset previous highlighted cells
        }
        lastHighlightedCells.clear();  // Clear the previous conflicting cells list

        boolean hasConflict = false;

        // Check for conflicts in the same row and column only
        for (int i = 0; i < SudokuConstants.GRID_SIZE; ++i) {
            if (puzzle.numbers[row][i] == value && i != col && puzzle.isGiven[row][i]) { // Highlight only filled cells with conflict
                cells[row][i].setBackground(Color.RED); // Highlight conflicting cell in row
                lastHighlightedCells.add(cells[row][i]); // Add to the conflict list
                hasConflict = true;
            }
        }

        for (int i = 0; i < SudokuConstants.GRID_SIZE; ++i) {
            if (puzzle.numbers[i][col] == value && i != row && puzzle.isGiven[i][col]) { // Highlight only filled cells with conflict
                cells[i][col].setBackground(Color.RED); // Highlight conflicting cell in column
                lastHighlightedCells.add(cells[i][col]); // Add to the conflict list
                hasConflict = true;
            }
        }

        // If no conflict was found, highlight all cells with the same value
        if (!hasConflict) {
            highlightCellsWithValue(value, Color.YELLOW); // Highlight all cells with the same value
        }
    }


    // Inner class to handle input in editable cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                if (numberIn == puzzle.numbers[sourceCell.getRow()][sourceCell.getCol()]) { // Check against the value in puzzle
                    sourceCell.setStatus(CellStatus.CORRECT_GUESS);
                    sourceCell.updateAppearance(); // Update appearance immediately
                    sourceCell.repaint(); // Force repaint
                    sourceCell.revalidate(); // Force revalidate
                    SoundEffect.CORRECT_GUESS.play(); // Play sound for correct guess
                    highlightConflictingCells(sourceCell.getRow(), sourceCell.getCol(), numberIn); // Highlight same value and conflicts
                } else {
                    sourceCell.setStatus(CellStatus.WRONG_GUESS);
                    sourceCell.updateAppearance(); // Update appearance immediately
                    sourceCell.repaint(); // Force repaint
                    sourceCell.revalidate(); // Force revalidate
                    SoundEffect.WRONG_GUESS.play(); // Play sound for wrong guess
                    highlightConflictingCells(sourceCell.getRow(), sourceCell.getCol(), numberIn); // Highlight same value and conflicts
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

