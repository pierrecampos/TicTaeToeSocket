package model.entities;

public class TicTacToe {
    Boolean[][] board;

    public TicTacToe() {
        board = new Boolean[3][3];
    }

    public void play(int row, int column, boolean token){
        board[row][column] = token;
    }

    public boolean validPlay(int row, int column){
        return board[row][column] == null;
    }

    public Boolean[][] getBoard(){
        return board;
    }




}
