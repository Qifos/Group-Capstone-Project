package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L; // To prevent serial warning

    // Define UI sizes
    public static final int CELL_SIZE = 60; // Cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    // Properties
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();

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

    // Inner class to handle input in editable cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                if (numberIn == sourceCell.number) {
                    sourceCell.setStatus(CellStatus.CORRECT_GUESS);
                } else {
                    sourceCell.setStatus(CellStatus.WRONG_GUESS);
                }

                // Check for solved puzzle
                if (isSolved()) {
                    JOptionPane.showMessageDialog(GameBoardPanel.this, "Congratulations! You've solved the puzzle!", "Puzzle Solved", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GameBoardPanel.this, "Invalid input. Please enter a number between 1 and 9.", "Input Error", JOptionPane.ERROR_MESSAGE);
                sourceCell.setText("");
            }
        }
    }
}
