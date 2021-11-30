package model.entities;

public class TicTacToe {
    Boolean[][] board;
    private int rounds;

    public TicTacToe() {
        rounds = 0;
        board = new Boolean[3][3];
    }

    public void play(int row, int column, boolean token) {
        board[row][column] = token;
        rounds++;
    }

    public void removePlay(int row, int column){
        board[row][column] = null;
        rounds--;
    }

    public boolean validPlay(int row, int column) {
        return board[row][column] == null;
    }

    public Boolean[][] getBoard() {
        return board;
    }

    public int getRounds(){
        return rounds;
    }

    //Logica do jogo da velha
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

                // analisa as colunas
                if (token.equals(board[row][column])) {
                    rowSum++;
                    winningFieldsRow[row][column] = true;
                }
                if (rowSum == 3) {
                    return winningFieldsRow;
                }

                //analisa as linhas
                if (token.equals(board[column][row])) {
                    columnSum++;
                    winningFieldsColumn[column][row] = true;
                }
                if (columnSum == 3) {
                    return winningFieldsColumn;
                }

                //analisa a diagonal principal
                if (token.equals(board[column][column])) {
                    diagonalPrimarySum++;
                    winningFieldsPrimaryDiagonal[column][column] = true;
                }

                if (diagonalPrimarySum == 3) {
                    return winningFieldsPrimaryDiagonal;
                }
            }

            winningFieldsRow = new Boolean[3][3];
            winningFieldsColumn = new Boolean[3][3];
            winningFieldsPrimaryDiagonal = new Boolean[3][3];
            rowSum = 0;
            columnSum = 0;
            diagonalPrimarySum = 0;
        }

        //analisa a diagonal secundaria
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
