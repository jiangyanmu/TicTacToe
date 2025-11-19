import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class TicTacToeGUI extends JFrame {

    private JButton[][] buttons = new JButton[3][3];
    private JLabel statusLabel;
    private char currentPlayer = 'X';
    private boolean gameEnded = false;

    // --- Modern, Simple Color Palette (shadcn/ui inspired) ---
    private final Color COLOR_BACKGROUND = new Color(248, 249, 250); // Off-white
    private final Color COLOR_CARD = Color.WHITE;
    private final Color COLOR_TEXT_PRIMARY = new Color(33, 37, 41); // Dark Gray
    private final Color COLOR_TEXT_SECONDARY = new Color(108, 117, 125); // Medium Gray
    private final Color COLOR_PRIMARY = new Color(73, 80, 87); // Grayish accent
    private final Color COLOR_BORDER = new Color(222, 226, 230); // Light Gray Border

    private final Color COLOR_PLAYER_X = new Color(23, 113, 230); // Blue
    private final Color COLOR_PLAYER_O = new Color(230, 126, 34); // Orange

    // --- Fonts ---
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 72);
    private final Font FONT_LABEL = new Font("微軟正黑體", Font.BOLD, 22);
    private final Font FONT_NEW_GAME = new Font("微軟正黑體", Font.PLAIN, 16);

    public TicTacToeGUI() {
        // Use system look and feel for better native rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- Main Window ---
        setTitle("井字棋遊戲");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 15)); // Add vertical gap
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BACKGROUND);
        getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding for the whole window

        // --- Header Panel (for status label) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BACKGROUND);
        statusLabel = new JLabel("玩家 X 的回合", SwingConstants.CENTER);
        statusLabel.setFont(FONT_LABEL);
        statusLabel.setForeground(COLOR_TEXT_PRIMARY);
        headerPanel.add(statusLabel, BorderLayout.CENTER);
        
        // --- Board Panel ---
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 10, 10)); // Gaps between buttons
        boardPanel.setBackground(COLOR_BACKGROUND);
        boardPanel.setBorder(BorderFactory.createLineBorder(COLOR_BACKGROUND, 10)); // Padding inside

        Border buttonBorder = BorderFactory.createLineBorder(COLOR_BORDER, 2);
        Border buttonHoverBorder = BorderFactory.createLineBorder(COLOR_PRIMARY, 2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(FONT_BUTTON);
                buttons[i][j].setFocusable(false);
                buttons[i][j].setBackground(COLOR_CARD);
                buttons[i][j].setBorder(buttonBorder);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                
                // Mouse listener for hover effect
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                        JButton button = (JButton) evt.getSource();
                        if (button.isEnabled()) {
                           button.setBorder(buttonHoverBorder);
                        }
                    }
                    public void mouseExited(MouseEvent evt) {
                         JButton button = (JButton) evt.getSource();
                         button.setBorder(buttonBorder);
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        // --- Footer Panel (for new game button) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(COLOR_BACKGROUND);

        JButton newGameButton = new JButton("新遊戲");
        newGameButton.setFont(FONT_NEW_GAME);
        newGameButton.setFocusable(false);
        newGameButton.setBackground(COLOR_CARD);
        newGameButton.setForeground(COLOR_TEXT_PRIMARY);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        newGameButton.addActionListener(e -> resetGame());

        footerPanel.add(newGameButton);

        // --- Add panels to frame ---
        add(headerPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameEnded || !buttons[row][col].getText().equals("")) {
                return;
            }

            JButton clickedButton = buttons[row][col];
            clickedButton.setText(String.valueOf(currentPlayer));
            clickedButton.setForeground(currentPlayer == 'X' ? COLOR_PLAYER_X : COLOR_PLAYER_O);
            clickedButton.setEnabled(false); // Disable after click

            if (checkWin()) {
                statusLabel.setText("恭喜玩家 " + currentPlayer + " 獲勝！");
                gameEnded = true;
                highlightWinningButtons();
            } else if (isBoardFull()) {
                statusLabel.setText("遊戲平局！");
                gameEnded = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                statusLabel.setText("玩家 " + currentPlayer + " 的回合");
            }
        }
    }
    
    private void highlightWinningButtons() {
        String p = String.valueOf(currentPlayer);
        Color winColor = currentPlayer == 'X' ? COLOR_PLAYER_X.brighter() : COLOR_PLAYER_O.brighter();
        
        // Rows
        for (int i = 0; i < 3; i++) {
            if (p.equals(buttons[i][0].getText()) && p.equals(buttons[i][1].getText()) && p.equals(buttons[i][2].getText())) {
                setWinnerColor(winColor, buttons[i][0], buttons[i][1], buttons[i][2]);
                return;
            }
        }
        // Columns
        for (int j = 0; j < 3; j++) {
            if (p.equals(buttons[0][j].getText()) && p.equals(buttons[1][j].getText()) && p.equals(buttons[2][j].getText())) {
                setWinnerColor(winColor, buttons[0][j], buttons[1][j], buttons[2][j]);
                return;
            }
        }
        // Diagonals
        if (p.equals(buttons[0][0].getText()) && p.equals(buttons[1][1].getText()) && p.equals(buttons[2][2].getText())) {
            setWinnerColor(winColor, buttons[0][0], buttons[1][1], buttons[2][2]);
        }
        if (p.equals(buttons[0][2].getText()) && p.equals(buttons[1][1].getText()) && p.equals(buttons[2][0].getText())) {
            setWinnerColor(winColor, buttons[0][2], buttons[1][1], buttons[2][0]);
        }
    }

    private void setWinnerColor(Color color, JButton... winnerButtons) {
        for (JButton button : winnerButtons) {
            button.setBackground(color.brighter());
            button.setOpaque(true); 
        }
         disableAllButtons();
    }
    
    private void disableAllButtons(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private boolean checkWin() {
        String p = String.valueOf(currentPlayer);
        for (int i = 0; i < 3; i++) { // Check rows
            if (p.equals(buttons[i][0].getText()) && p.equals(buttons[i][1].getText()) && p.equals(buttons[i][2].getText())) return true;
        }
        for (int j = 0; j < 3; j++) { // Check columns
            if (p.equals(buttons[0][j].getText()) && p.equals(buttons[1][j].getText()) && p.equals(buttons[2][j].getText())) return true;
        }
        if (p.equals(buttons[0][0].getText()) && p.equals(buttons[1][1].getText()) && p.equals(buttons[2][2].getText())) return true;
        if (p.equals(buttons[0][2].getText()) && p.equals(buttons[1][1].getText()) && p.equals(buttons[2][0].getText())) return true;
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) return false;
            }
        }
        return true;
    }

    private void resetGame() {
        currentPlayer = 'X';
        statusLabel.setText("玩家 X 的回合");
        gameEnded = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(COLOR_CARD);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToeGUI game = new TicTacToeGUI();
            game.setVisible(true);
        });
    }
}
