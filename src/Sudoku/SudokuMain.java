package Sudoku;

import javax.swing.*;
import java.awt.*;

/**
 * The main entry point for the Sudoku game application.
 */
public class SudokuMain {
    public static void main(String[] args) {
        // Create the main application frame
        JFrame frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the game board panel
        GameBoardPanel gameBoardPanel = new GameBoardPanel();

        // Add the game board panel to the frame
        frame.add(gameBoardPanel, BorderLayout.CENTER);

        // Create a control panel with buttons
        JPanel controlPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        JButton exitButton = new JButton("Exit");

        // Add action listeners for the buttons
        newGameButton.addActionListener(e -> {
            int difficultyLevel = JOptionPane.showOptionDialog(
                    frame,
                    "Select Difficulty Level",
                    "New Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Easy", "Medium", "Hard"},
                    "Easy"
            );

            // Translate difficulty selection to blank cell count
            int blanks = switch (difficultyLevel) {
                case 0 -> 20; // Easy
                case 1 -> 40; // Medium
                case 2 -> 60; // Hard
                default -> 20; // Default to Easy
            };

            gameBoardPanel.newGame(blanks);
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the control panel
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);

        // Add the control panel to the frame
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Set frame properties
        frame.pack(); // Size the frame to fit its components
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);

        // Start a new game with default difficulty (Easy)
        gameBoardPanel.newGame(20); // Easy level with 20 blank cells
    }
}
