package game;

// test purposes only
// test purposes only
// test purposes only
// RUN FROM MINESWEEPER.JAVA

public class Main {
    public static void main(String[] args) {
        int rows = 12;
        int cols = 12;
        game.Game ms = new game.Game(rows, cols, 2);
        for (int row = 0; row < rows; row++) {
            System.out.print("\n");
            for (int col = 0; col < cols; col++) {
                int idx = ms.getIndex(row, col);
                if (ms.fields[idx] == game.Game.FieldState.Unflagged_mine) {
                    System.out.print("X");
                } else {
                    System.out.print(ms.fields_mine_surround[idx]);
                }
            }
        }
    }

}