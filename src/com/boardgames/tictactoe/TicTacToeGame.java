package com.boardgames.tictactoe;

public class TicTacToeGame {

    public enum GameState {
        PLAYING,
        X_WINS,
        O_WINS,
        DRAW
    }

    private char[][] board;
    private char currentPlayer;
    private GameState gameState;

    public TicTacToeGame() {
        board = new char[3][3];
        reset();
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' '; // Use space for empty cell
            }
        }
        currentPlayer = 'X';
        gameState = GameState.PLAYING;
    }

    public boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ' || gameState != GameState.PLAYING) {
            return false; // Invalid move
        }

        board[row][col] = currentPlayer;
        updateGameState();
        if (gameState == GameState.PLAYING) {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
        return true;
    }

    private void updateGameState() {
        if (checkWin('X')) {
            gameState = GameState.X_WINS;
        } else if (checkWin('O')) {
            gameState = GameState.O_WINS;
        } else if (isBoardFull()) {
            gameState = GameState.DRAW;
        }
    }

    private boolean checkWin(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // --- Getters for View ---
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public char getSymbolAt(int row, int col) {
        return board[row][col];
    }

    public char[][] getBoard() {
        // Return a copy to prevent external modification of the internal board state
        char[][] boardCopy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, 3);
        }
        return boardCopy;
    }
}
