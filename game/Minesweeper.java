package game;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Minesweeper extends JFrame {

    public Minesweeper() {
        JLabel status = new JLabel("");
        add(status, BorderLayout.SOUTH);
        add(new GUI(16, 16, 20));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Minesweeper ms = new Minesweeper();
        ms.setVisible(true);
    }
}