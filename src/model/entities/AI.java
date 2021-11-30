package model.entities;


import model.constants.Token;
import util.Utils;

public class AI {
    private final Token token;

    public AI(Token token) {
        this.token = token;
    }

    public void play(TicTacToe game) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int[] move = new int[2];

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (game.validPlay(row, column)) {
                    game.play(row, column, token.value);
                    double score = minMax(game, 0, token);
                }
            }
        }

    }

    private double minMax(TicTacToe game, int depth, Token token) {
        Token winnerToken = checkWinner(game.getBoard());
        if (winnerToken != null) {
            if (Token.CROSS.equals(winnerToken)) {
                return 10;
            } else if (Token.CIRCLE.equals(winnerToken)) {
                return -10;
            } else {
                return 0;
            }
        }

        if (Token.CROSS.equals(winnerToken)) {
            double bestScore = Double.NEGATIVE_INFINITY;
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++) {
                    if (game.validPlay(row, column)) {
                        game.play(row, column, Token.CROSS.value);
                        double score = minMax(game, depth + 1, Token.CIRCLE);
                        game.removePlay(row, column);
                        bestScore = Double.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            double bestScore = Double.POSITIVE_INFINITY;
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++) {
                    if (game.validPlay(row, column)) {
                        game.play(row, column, Token.CROSS.value);
                        double score = minMax(game, depth + 1, Token.CIRCLE);
                        game.removePlay(row, column);
                        bestScore = Double.min(score, bestScore);
                    }
                }
            }
            return bestScore;

        }

    }

    private Token checkWinner(Boolean[][] board) {
        Boolean winner = null;
        // horizontal
        for (int i = 0; i < 3; i++) {
            if (Utils.isEquals(board[i][0], board[i][1], board[i][2])) {
                winner = board[i][0];
            }
        }

        // Vertical
        for (int i = 0; i < 3; i++) {
            if (Utils.isEquals(board[0][i], board[1][i], board[2][i])) {
                winner = board[0][i];
            }
        }

        // Diagonal
        if (Utils.isEquals(board[0][0], board[1][1], board[2][2])) {
            winner = board[0][0];
        }
        if (Utils.isEquals(board[2][0], board[1][1], board[0][2])) {
            winner = board[2][0];
        }

        if (winner == null) {
            return null;
        } else {
            Token token = (winner) ? Token.CROSS : Token.CIRCLE;
            return token;
        }
    }

}
