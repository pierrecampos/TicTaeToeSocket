package model.entities;

public class TicTacToe {
    Boolean[][] board;

    public TicTacToe() {
        board = new Boolean[3][3];
    }

    public void play(int row, int column, boolean token) {
        board[row][column] = token;
    }

    public boolean validPlay(int row, int column) {
        return board[row][column] == null;
    }

    public Boolean[][] getBoard() {
        return board;
    }


    public Boolean[][] isWinner(Boolean token) {
        int sumRow = 0;
        int sumColumn = 0;
        Boolean[][] winningFields = new Boolean[3][3];
        Boolean[][] winningFields2 = new Boolean[3][3];
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {

                if (token.equals(board[row][column])) {
                    sumRow++;
                    winningFields[row][column] = token;
                }
                if (sumRow == 3) {
                    return winningFields;
                }

                if (token.equals(board[column][row])) {
                    sumColumn++;
                    winningFields2[column][row] = token;
                }
                if (sumColumn == 3) {
                    return winningFields2;
                }
            }
            winningFields = new Boolean[3][3];
            winningFields2 = new Boolean[3][3];
            sumRow = 0;
            sumColumn = 0;
        }

        return new Boolean[0][0];
    }


}
