/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * This enum is used by:
 * 1. Player: takes value of CROSS or NOUGHT
 * 2. Cell content: takes value of CROSS, NOUGHT, or NO_SEED.
 * <p>
 * We also attach a display image icon (text or image) for the items.
 * and define the related variable/constructor/getter.
 * To draw the image:
 * g.drawImage(content.getImage(), x, y, width, height, null);
 * <p>
 * Ideally, we should define two enums with inheritance, which is,
 * however, not supported.
 */
public enum Seed {
    BLACK("B"),
    WHITE("W"),
    NO_SEED(" ");

    private String displayName;

    private Seed(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
