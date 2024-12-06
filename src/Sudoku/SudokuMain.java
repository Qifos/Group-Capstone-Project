/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

package Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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
            updateStatusBar(frame, gameBoardPanel); // Update status bar after new game
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the control panel
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);

        // Add the control panel to the frame
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(e -> {
            newGameButton.doClick();
        });
        JMenuItem resetGameMenuItem = new JMenuItem("Reset Game");
        resetGameMenuItem.addActionListener(e -> {
            gameBoardPanel.newGame(20);
            updateStatusBar(frame, gameBoardPanel);
        });
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(newGameMenuItem);
        fileMenu.add(resetGameMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu optionsMenu = new JMenu("Options");
        JMenuItem toggleSoundMenuItem = new JMenuItem("Sound");
        toggleSoundMenuItem.addActionListener(new ActionListener() {
            private boolean soundOn = true; // Track sound state

            @Override
            public void actionPerformed(ActionEvent e) {
                soundOn = !soundOn;
                SoundEffect.volume = soundOn ? SoundEffect.Volume.MEDIUM : SoundEffect.Volume.MUTE;
                String message = soundOn ? "Sound is ON" : "Sound is OFF";
                JOptionPane.showMessageDialog(frame, message, "Sound", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        optionsMenu.add(toggleSoundMenuItem);
        menuBar.add(optionsMenu);

        // Create Help
        JMenu helpMenu = new JMenu("Help");
        JMenuItem fillRandomCellMenuItem = new JMenuItem("Fill Random Cell");
        fillRandomCellMenuItem.addActionListener(e -> fillRandomCellWithCorrectNumber(gameBoardPanel));
        helpMenu.add(fillRandomCellMenuItem);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);

        // Create status bar
        JTextField statusBar = new JTextField("Welcome to Sudoku!");
        statusBar.setEditable(false);
        statusBar.setBackground(Color.LIGHT_GRAY);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameBoardPanel.newGame(20);
        updateStatusBar(frame, gameBoardPanel);
    }

    // update status bar with number of cell
    private static void updateStatusBar(JFrame frame, GameBoardPanel gameBoardPanel) {
        JTextField statusBar = (JTextField) frame.getContentPane().getComponent(2);
        int remainingCells = 0;
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                if (gameBoardPanel.cells[row][col].getStatus() == CellStatus.TO_GUESS) {
                    remainingCells++;
                }
            }
        }
        statusBar.setText("Cells remaining: " + remainingCells);
    }

    // fill random cell
    private static void fillRandomCellWithCorrectNumber(GameBoardPanel gameBoardPanel) {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(SudokuConstants.GRID_SIZE);
            col = random.nextInt(SudokuConstants.GRID_SIZE);
        } while (gameBoardPanel.cells[row][col].getStatus() != CellStatus.TO_GUESS);

        int correctNumber = gameBoardPanel.puzzle.numbers[row][col];
        gameBoardPanel.cells[row][col].setText(Integer.toString(correctNumber));
        gameBoardPanel.cells[row][col].setStatus(CellStatus.CORRECT_GUESS);
        gameBoardPanel.cells[row][col].updateAppearance();
        updateStatusBar((JFrame) SwingUtilities.getWindowAncestor(gameBoardPanel), gameBoardPanel);
    }
}