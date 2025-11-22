import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReversiAIPlayer {

    /**
     * Finds the best move for the AI player.
     * This AI chooses the move that flips the most opponent pieces.
     *
     * @param board The current game board.
     * @param player The AI player's symbol ('B' or 'W').
     * @return An array with [row, col] of the chosen move, or null if no move is possible.
     */
    public static int[] findBestMove(ReversiGame game) {
        List<int[]> validMoves = new ArrayList<>();
        int maxFlips = 0;
        
        char[][] board = game.getBoard();
        char player = game.getCurrentPlayer();

        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.isValidMoveForPlayer(i, j, player)) {
                    int flips = countFlips(board, i, j, player);
                    if (flips > maxFlips) {
                        maxFlips = flips;
                        validMoves.clear();
                        validMoves.add(new int[]{i, j});
                    } else if (flips == maxFlips) {
                        validMoves.add(new int[]{i, j});
                    }
                }
            }
        }

        if (validMoves.isEmpty()) {
            return null; // No available moves
        }

        // Choose a random move from the best moves
        Random random = new Random();
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    private static int countFlips(char[][] board, int row, int col, char player) {
        int flips = 0;
        char opponent = (player == 'B') ? 'W' : 'B';
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + dr[i];
            int c = col + dc[i];
            int lineFlips = 0;
            while (r >= 0 && r < board.length && c >= 0 && c < board.length && board[r][c] == opponent) {
                r += dr[i];
                c += dc[i];
                lineFlips++;
            }
            if (lineFlips > 0 && r >= 0 && r < board.length && c >= 0 && c < board.length && board[r][c] == player) {
                flips += lineFlips;
            }
        }
        return flips;
    }
}
