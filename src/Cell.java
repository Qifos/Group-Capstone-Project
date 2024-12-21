/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

import java.awt.*;

public class Cell {
    public static final int SIZE = 60; // Match with Board's CELL_SIZE
    public static final int PADDING = 5;

    Seed content;
    int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.content = Seed.NO_SEED;
    }

    public void paint(Graphics g) {
        if (content == Seed.BLACK || content == Seed.WHITE) {
            int x = col * SIZE + PADDING;
            int y = row * SIZE + PADDING;
            int diameter = SIZE - 2 * PADDING;

            // Set color based on seed
            g.setColor(content == Seed.BLACK ? Color.BLACK : Color.WHITE);
            g.fillOval(x, y, diameter, diameter);

            // Draw border
            g.setColor(Color.DARK_GRAY);
            g.drawOval(x, y, diameter, diameter);
        }
    }
}
