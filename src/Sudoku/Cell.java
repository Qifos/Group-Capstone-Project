/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

package Sudoku;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

/**
 * The Cell class models the cells of the Sudoku puzzle by subclassing JTextField.
 */
public class Cell extends JTextField {
    private static final long serialVersionUID = 1L; // To prevent serial warning

    // Define colors and fonts for the cell
    public static final Color BG_GIVEN = new Color(240, 240, 240); // Light gray
    public static final Color FG_GIVEN = Color.BLACK;
    public static final Color FG_NOT_GIVEN = Color.GRAY;
    public static final Color BG_TO_GUESS = Color.YELLOW;
    public static final Color BG_CORRECT = new Color(0, 216, 0); // Green
    public static final Color BG_WRONG = new Color(216, 0, 0);   // Red
    public static final Font FONT_NUMBERS = new Font("OCR A Extended", Font.PLAIN, 28);

    // Properties of the Cell
    private int row, col;             // Row and column indices of this cell
    int number;                       // The number assigned to this cell
    public CellStatus status;         // The status of this cell (e.g., GIVEN, TO_GUESS)

    // Constructor
    public Cell(int row, int col) {
        super(); // Call JTextField constructor
        this.row = row;
        this.col = col;

        // Beautify all cells
        setHorizontalAlignment(JTextField.CENTER);
        setFont(FONT_NUMBERS);
    }

    // Initialize this cell for a new game
    public void newGame(int number, boolean isGiven) {
        this.number = number;
        this.status = isGiven ? CellStatus.GIVEN : CellStatus.TO_GUESS;
        updateAppearance();
    }

    // Update the appearance of the cell based on its status
    public void updateAppearance() {
        if (status == CellStatus.GIVEN) {
            setText(Integer.toString(number));
            setEditable(false);
            setBackground(BG_GIVEN);
            setForeground(FG_GIVEN);
        } else if (status == CellStatus.TO_GUESS) {
            setText("");
            setEditable(true);
            setBackground(BG_TO_GUESS);
            setForeground(FG_NOT_GIVEN);
        } else if (status == CellStatus.CORRECT_GUESS) {
            setText(Integer.toString(number));
            setEditable(false);
            setBackground(BG_CORRECT);
            setForeground(FG_GIVEN);
        } else if (status == CellStatus.WRONG_GUESS) {
            setBackground(BG_WRONG);
        }
        repaint();
        revalidate();
    }



    // Getter for row
    public int getRow() {
        return row;
    }

    // Getter for column
    public int getCol() {
        return col;
    }

    // Getter for status
    public CellStatus getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(CellStatus status) {
        this.status = status;
        updateAppearance();
    }
}
