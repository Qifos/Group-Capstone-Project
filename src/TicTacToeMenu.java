/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #12
 * 1 - 5026231096 - Muhammad Fiqih Soetam Putra
 * 2 - 5026231164 - Bagus Subekti
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeMenu extends JFrame {
    public TicTacToeMenu() {
        setTitle("Tic Tac Toe");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create buttons
        JButton btnPvAI = new JButton("Player vs AI");
        JButton btnPvP = new JButton("Player vs Player");
        JButton btnExit = new JButton("Exit");

        // Set layout
        setLayout(new GridLayout(3, 1));
        add(btnPvAI);
        add(btnPvP);
        add(btnExit);

        // Add action listeners
        btnPvAI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Initialize Player vs AI game
                // TicTacToeGame game = new TicTacToeGame("AI");
                // game.start();
                System.out.println("Player vs AI selected");
            }
        });

        btnPvP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Initialize Player vs Player game
                // TicTacToeGame game = new TicTacToeGame("PVP");
                // game.start();
                System.out.println("Player vs Player selected");
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TicTacToeMenu().setVisible(true);
            }
        });
    }
}