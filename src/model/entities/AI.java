package model.entities;


import model.constants.Token;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AI {
    private final Token token;

    public AI(Token token) {
        this.token = token;
    }

    public void play(TicTacToe game) {
        List<int[]> possibleMoves = possibleMoves(game);
        double best = Double.NEGATIVE_INFINITY;
        int[] bestPlay = null;

        for (int[] x : possibleMoves) {
            Boolean[][] move = testPlay(game.getBoard(), x, Token.CROSS);
//            miniMax(move, );
        }
    }

    public ArrayList<int[]> possibleMoves(TicTacToe game) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (game.validPlay(row, column)) {
                    possibleMoves.add(new int[]{row, column});
                }
            }
        }
        return possibleMoves;
    }

    public void miniMax(Boolean[][] board, Token turnPlayer, Token player) {

    }

    public Boolean[][] testPlay(Boolean[][] board, int[] pos, Token playerToken) {
        Boolean[][] copyBoard = Utils.copyMatrix(board);
        copyBoard[pos[0]][pos[1]] = playerToken.value;
        return copyBoard;
    }

}
