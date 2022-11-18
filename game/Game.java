package game;

import java.util.Random;

public class Game {
    private final int ROWS;
    private final int COLUMNS;
    private final int N_MINES;
    private final int N_FIELDS;
    public enum FieldState {
        Flagged_mine, Unflagged_mine,  Flagged_empty, Unflagged_empty, Uncovered
    }
    public FieldState[] fields;
    public int[] fields_mine_surround;
    public boolean lost;
    public Game(int rows, int columns, int mines) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.N_MINES = mines;
        this.N_FIELDS = ROWS * COLUMNS;
        this.fields =  new FieldState[N_FIELDS];
        this.fields_mine_surround = new int[N_FIELDS];
        this.lost = false;
        this.fillFields();
        this.randomizeMines();
    }
    private int randIntInRange(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
    private int getRow(int idx) {
        return idx / COLUMNS;
    }
    private int getColumn(int idx) {
        return idx % COLUMNS;
    }
    public int getIndex(int row, int col) {
        return (row * COLUMNS) + col;
    }
    private void fillFields() {
        for (int i = 0; i < N_FIELDS; i++) {
            this.fields[i] = FieldState.Unflagged_empty;
        }
    }
    public void randomizeMines() {
        int i = 0;
        while (i < N_MINES) {
            int random_index = randIntInRange(N_FIELDS);
            if (fields[random_index] != FieldState.Unflagged_mine) {
                fields[random_index] = FieldState.Unflagged_mine;
                i++;
                int mine_row = getRow(random_index);
                int mine_col = getColumn(random_index);
                for (int delta_row = -1; delta_row < 2; delta_row++) {
                    for (int delta_col = -1; delta_col < 2; delta_col++) {
                        if (delta_row == 0 && delta_col == 0) {
                            continue;
                        }
                        int row = mine_row + delta_row;
                        int column = mine_col + delta_col;
                        if ((row >=0 && row < ROWS) && (column >=0 && column < COLUMNS)) {
                            int idx = getIndex(row, column);
                            fields_mine_surround[idx]++;
                        }
                    }
                }
            }
        }
    }

    private void uncover(int row, int col) {
        int idx = getIndex(row, col);
        this.fields[idx] = FieldState.Uncovered;
        if (this.fields_mine_surround[idx] == 0) {
        // flood fill, check all neighbours, reveal them if not a bomb and recursion if mines_surround == 0
            for (int delta_row = -1; delta_row < 2; delta_row++) {
                for (int delta_col = -1; delta_col < 2; delta_col++) {
                    if (delta_row == 0 && delta_col == 0) {
                        continue;
                    }
                    int neighbor_row = row + delta_row;
                    int neighbor_col = col + delta_col;
                    if ((neighbor_row >=0 && neighbor_row < ROWS) && (neighbor_col >=0 && neighbor_col < COLUMNS)) {
                        int neighbor_idx = getIndex(neighbor_row, neighbor_col);
                        if (this.fields[neighbor_idx] == Game.FieldState.Flagged_empty || this.fields[neighbor_idx] == Game.FieldState.Unflagged_empty) {
                            uncover(neighbor_row, neighbor_col);
                        }
                    }
                }
            }
        }
    }
    public void handleLeftClick(int row, int col) {
        int idx = getIndex(row, col);
        FieldState field_state = this.fields[idx];
        if (field_state == FieldState.Unflagged_mine) {
            this.lost = true;
        } else if (field_state == FieldState.Unflagged_empty) {
            uncover(row, col);
        }
    }
    public void handleRightClick(int row, int col) {
        int idx = getIndex(row, col);
        FieldState field_state = this.fields[idx];
        if (field_state == FieldState.Flagged_empty) {
            this.fields[idx] = FieldState.Unflagged_empty;
        } else if (field_state == FieldState.Flagged_mine) {
            this.fields[idx] = FieldState.Unflagged_mine;
        } else if (field_state == FieldState.Unflagged_mine) {
            this.fields[idx] = FieldState.Flagged_mine;
        } else if (field_state == FieldState.Unflagged_empty) {
            this.fields[idx] = FieldState.Flagged_empty;
        }
    }
}
