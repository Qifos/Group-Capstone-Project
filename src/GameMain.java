/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    // Define named constants for the drawing graphics
    public static final String TITLE = "Othello";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_BLACK = Color.BLACK;
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message
    private boolean playAgainstAI = false;

    /**
     * Constructor to setup the UI and game components
     */
    public GameMain() {
        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (board.isValidMove(currentPlayer, row, col)) {
                        SoundEffect.EAT_FOOD.play();

                        // Place the disc
                        board.cells[row][col].content = currentPlayer;

                        // Flip opponent's discs
                        board.flipDiscs(currentPlayer, row, col);

                        // Update game state
                        currentState = board.checkGameState(currentPlayer);

                        // Switch players
                        currentPlayer = (currentPlayer == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;

                        // Handle AI move if playing against AI
                        if (playAgainstAI && currentState == State.PLAYING && currentPlayer == Seed.WHITE) {
                            int[] aiMove = getAIMove();
                            if (aiMove[0] != -1 && aiMove[1] != -1) {
                                SoundEffect.EAT_FOOD.play();

                                // Place AI's disc
                                board.cells[aiMove[0]][aiMove[1]].content = currentPlayer;
                                board.flipDiscs(currentPlayer, aiMove[0], aiMove[1]);

                                // Update game state
                                currentState = board.checkGameState(currentPlayer);

                                // Switch back to human player
                                currentPlayer = Seed.BLACK;
                            }
                        }

                        repaint();
                    }
                } else {
                    SoundEffect.DIE.play();
                    showMainMenu();
                }
            }
        });

        // Setup the status bar
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        board = new Board();
        showMainMenu();
    }

    private void showMainMenu() {
        String[] options = {"Player vs AI", "Player vs Player", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, "Choose Game Mode", "Main Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            playAgainstAI = true;
            newGame();
        } else if (choice == 1) {
            playAgainstAI = false;
            newGame();
        } else if (choice == 2) {
            System.exit(0);
        } else {
            System.exit(0);
        }
    }

    /**
     * Reset the game-board contents and the current-state, ready for new game
     */
    public void newGame() {
        board.initGame(); // Use the board's initialization method
        currentPlayer = Seed.BLACK;
        currentState = State.PLAYING;
        repaint();
    }

    /**
     * Custom painting codes on this JPanel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        // Update status bar based on game state
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.BLACK ? "Black" : "White") + "'s Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to return to main menu.");
        } else if (currentState == State.BLACK_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Black Won! Click to return to main menu.");
        } else if (currentState == State.WHITE_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("White Won! Click to return to main menu.");
        }
    }

    // AI Move Selection (Simplified for Othello)
    private int[] getAIMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.isValidMove(currentPlayer, row, col)) {
                    // Simulate the move
                    Board simulatedBoard = copyBoard(board);
                    simulatedBoard.cells[row][col].content = currentPlayer;
                    simulatedBoard.flipDiscs(currentPlayer, row, col);

                    // Evaluate the move (simple heuristic: count of discs)
                    int score = countDiscsOnBoard(simulatedBoard, currentPlayer);

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }
        return bestMove;
    }


    // Helper method to create a deep copy of the board
    private Board copyBoard(Board originalBoard) {
        Board newBoard = new Board();
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                newBoard.cells[row][col].content = originalBoard.cells[row][col].content;
            }
        }
        return newBoard;
    }

    // Helper method to count discs on a specific board
    private int countDiscsOnBoard(Board board, Seed player) {
        int count = 0;
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == player) {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * The entry "main" method
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame .setSize(600, 600);
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true);            // show it
            }
        });
    }
}
