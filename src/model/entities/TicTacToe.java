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
        int rowSum = 0;
        int columnSum = 0;
        int diagonalPrimarySum = 0;
        int diagonalSecondarySum = 0;
        Boolean[][] winningFieldsRow = new Boolean[3][3];
        Boolean[][] winningFieldsColumn = new Boolean[3][3];
        Boolean[][] winningFieldsPrimaryDiagonal = new Boolean[3][3];
        Boolean[][] winningFieldsSecondaryDiagonal = new Boolean[3][3];
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {

                if (token.equals(board[row][column])) {
                    rowSum++;
                    winningFieldsRow[row][column] = true;
                }
                if (rowSum == 3) {
                    return winningFieldsRow;
                }

                if (token.equals(board[column][row])) {
                    columnSum++;
                    winningFieldsColumn[column][row] = true;
                }
                if (columnSum == 3) {
                    return winningFieldsColumn;
                }

                if (token.equals(board[column][column])) {
                    diagonalPrimarySum++;
                    winningFieldsPrimaryDiagonal[column][column] = true;
                }

                if (diagonalPrimarySum == 3) {
                    return winningFieldsPrimaryDiagonal;
                }
            }

            winningFieldsRow = winningFieldsColumn = winningFieldsPrimaryDiagonal = new Boolean[3][3];
            rowSum = columnSum = diagonalPrimarySum = 0;
        }

        for (int row = 2, column = 0; row >= 0; row--, column++) {
            if (token.equals(board[row][column])) {
                diagonalSecondarySum++;
                winningFieldsSecondaryDiagonal[row][column] = true;
            }
            if (diagonalSecondarySum == 3) {
                return winningFieldsSecondaryDiagonal;
            }
        }

        return new Boolean[0][0];
    }


}
