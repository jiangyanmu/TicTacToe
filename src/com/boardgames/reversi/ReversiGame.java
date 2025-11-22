package com.boardgames.reversi;

public class ReversiGame {

    public enum GameState {
        PLAYING,
        BLACK_WINS,
        WHITE_WINS,
        DRAW
    }

    private final int BOARD_SIZE = 8;
    private char[][] board;
    private char currentPlayer;
    private GameState gameState;

    public ReversiGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        reset();
    }

    public void reset() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
        // Initial setup
        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
        currentPlayer = 'B'; // Black always starts
        gameState = GameState.PLAYING;
    }

    public boolean makeMove(int row, int col) {
        if (!isValidMove(row, col)) {
            return false;
        }

        board[row][col] = currentPlayer;
        flipDiscs(row, col);

        if (isGameOver()) {
            updateFinalGameState();
        } else {
            switchPlayer();
            if (!hasValidMove(currentPlayer)) {
                // If the new player has no valid moves, skip their turn
                switchPlayer();
                if (!hasValidMove(currentPlayer)) {
                    // If the other player also has no moves, the game is over
                    updateFinalGameState();
                }
            }
        }

        return true;
    }

    private void flipDiscs(int row, int col) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + dr[i];
            int c = col + dc[i];
            boolean foundOpponent = false;
            while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == getOpponent()) {
                r += dr[i];
                c += dc[i];
                foundOpponent = true;
            }
            if (foundOpponent && r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == currentPlayer) {
                int curR = row + dr[i];
                int curC = col + dc[i];
                while (curR != r || curC != c) {
                    board[curR][curC] = currentPlayer;
                    curR += dr[i];
                    curC += dc[i];
                }
            }
        }
    }
    
    public boolean isValidMove(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != ' ') {
            return false;
        }

        // Check all 8 directions
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int r = row + dr;
                int c = col + dc;
                boolean foundOpponent = false;
                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == getOpponent()) {
                    r += dr;
                    c += dc;
                    foundOpponent = true;
                }
                if (foundOpponent && r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == currentPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasValidMove(char player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isValidMoveForPlayer(i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidMoveForPlayer(int row, int col, char player) {
        if (board[row][col] != ' ') {
            return false;
        }
        char originalPlayer = currentPlayer;
        currentPlayer = player;
        boolean isValid = isValidMove(row, col);
        currentPlayer = originalPlayer;
        return isValid;
    }
    
    private void switchPlayer() {
        currentPlayer = getOpponent();
    }

    private char getOpponent() {
        return (currentPlayer == 'B') ? 'W' : 'B';
    }

    private boolean isGameOver() {
        return !hasValidMove('B') && !hasValidMove('W');
    }

    private void updateFinalGameState() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 'B') {
                    blackCount++;
                } else if (board[i][j] == 'W') {
                    whiteCount++;
                }
            }
        }
        if (blackCount > whiteCount) {
            gameState = GameState.BLACK_WINS;
        } else if (whiteCount > blackCount) {
            gameState = GameState.WHITE_WINS;
        } else {
            gameState = GameState.DRAW;
        }
    }
    
    public int[] getScore() {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 'B') {
                    blackCount++;
                } else if (board[i][j] == 'W') {
                    whiteCount++;
                }
            }
        }
        return new int[]{blackCount, whiteCount};
    }

    // --- Getters ---
    public int getBoardSize() {
        return BOARD_SIZE;
    }

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
        char[][] boardCopy = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, BOARD_SIZE);
        }
        return boardCopy;
    }
}
