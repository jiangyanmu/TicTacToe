package com.boardgames.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeAIPlayer {

    /**
     * Finds a move for the AI player.
     * This is a simple AI that just chooses a random empty cell.
     *
     * @param board The current game board.
     * @return An array with [row, col] of the chosen move, or null if no move is possible.
     */
    public static int[] findRandomMove(char[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return null; // No available moves
        }

        Random random = new Random();
        int[] chosenMove = emptyCells.get(random.nextInt(emptyCells.size()));
        return chosenMove;
    }
}
