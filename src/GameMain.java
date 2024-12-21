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

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
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
                    if (currentPlayer == Seed.CROSS) {
                        if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                                && board.cells[row][col].content == Seed.NO_SEED) {
                            SoundEffect.EAT_FOOD.play();
                            currentState = board.stepGame(currentPlayer, row, col);

                            if (currentState == State.PLAYING) {
                                currentPlayer = playAgainstAI ? Seed.NOUGHT : Seed.NOUGHT;
                            }
                            repaint();
                        }
                    }

                    if (playAgainstAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                        int[] aiMove = getAIMove();
                        if (aiMove[0] != -1 && aiMove[1] != -1) {
                            SoundEffect.EAT_FOOD.play();
                            currentState = board.stepGame(currentPlayer, aiMove[0], aiMove[1]);
                            if (currentState == State.PLAYING) {
                                currentPlayer = Seed.CROSS;
                            }
                            repaint();
                        }
                    }

                    if (!playAgainstAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                        if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                                && board.cells[row][col].content == Seed.NO_SEED) {
                            SoundEffect.EAT_FOOD.play();
                            currentState = board.stepGame(currentPlayer, row, col);
                            if (currentState == State.PLAYING) {
                                currentPlayer = Seed.CROSS;
                            }
                            repaint();
                        }
                    }
                } else {
                    SoundEffect.DIE.play();
                    showMainMenu();
                }
                repaint();
            }
        });


        // Setup the status bar (JLabel) to display status message
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
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
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

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to return to main menu.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to return to main menu.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to return to main menu.");
        }
    }

    // this method is for the AI
    private int[] getAIMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = Seed.NOUGHT;
                    int score = minimax(0, false);
                    board.cells[row][col].content = Seed.NO_SEED;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }
        return bestMove;
    }

    // MinMax
    private int minimax(int depth, boolean isMaximizing) {
        if (board.hasWon(Seed.NOUGHT)) return 10 - depth;
        if (board.hasWon(Seed.CROSS)) return depth - 10;
        if (board.isDraw()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = isMaximizing ? Seed.NOUGHT : Seed.CROSS;
                    int score = minimax(depth + 1, !isMaximizing);
                    board.cells[row][col].content = Seed.NO_SEED;

                    bestScore = isMaximizing
                            ? Math.max(bestScore, score)
                            : Math.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }

    /**
     * The entry "main" method
     */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                // Set the content-pane of the JFrame to an instance of main JPanel
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true);            // show it
            }
        });
    }

}

