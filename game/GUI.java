package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JPanel {
    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private final int N_ROWS;
    private final int N_COLS;

    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final int N_MINES;
    private Image[] img;
    Game minesweeper;
    public GUI(JLabel status, int rows, int cols, int mines) {
        this.N_ROWS = rows;
        this.N_COLS = cols;
        this.BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;
        this.BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
        this.N_MINES = mines;
        this.minesweeper = new Game(this.N_ROWS, this.N_COLS, this.N_MINES);
        initBoard();
    }
    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "src/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                int row_clicked = y / CELL_SIZE;
                int col_clicked = x / CELL_SIZE;

                boolean doRepaint = false;


                if ((col_clicked < N_COLS && col_clicked >= 0) && (row_clicked < N_ROWS && row_clicked >= 0)) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        handleLeftClick(row_clicked, col_clicked);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        handleRightClick(row_clicked, col_clicked);
                    }
                }
                repaint();
            }
        });
    }
    public void handleLeftClick(int row, int col) {
        this.minesweeper.handleLeftClick(row, col);
    }
    public void handleRightClick(int row, int col) {
        this.minesweeper.handleRightClick(row, col);
    }
    @Override
    public void paintComponent(Graphics g) {

        for (int i = 0; i < N_ROWS; i++) {

            for (int j = 0; j < N_COLS; j++) {
                int idx = this.minesweeper.getIndex(i, j);
                Game.FieldState cell = this.minesweeper.fields[idx];
                int cell_img = this.minesweeper.fields_mine_surround[idx];

                if (this.minesweeper.lost) {

                    if (cell == Game.FieldState.Unflagged_mine) {
                        cell_img = DRAW_MINE;
                    } else if (cell == Game.FieldState.Flagged_mine) {
                        cell_img = DRAW_MARK;
                    } else if (cell == Game.FieldState.Flagged_empty) {
                        cell_img = DRAW_WRONG_MARK;
                    } else if (cell == Game.FieldState.Unflagged_empty) {
                        cell_img = DRAW_COVER;
                    }

                } else {

                    if (cell == Game.FieldState.Flagged_mine) {
                        cell_img = DRAW_MARK;
                    } else if (cell == Game.FieldState.Unflagged_mine) {
                        cell_img = DRAW_COVER;
                    } else if (cell == Game.FieldState.Unflagged_empty) {
                        cell_img = DRAW_COVER;
                    } else if (cell == Game.FieldState.Flagged_empty) {
                        cell_img = DRAW_MARK;
                    }
                }

                g.drawImage(img[cell_img], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }
    }
}
