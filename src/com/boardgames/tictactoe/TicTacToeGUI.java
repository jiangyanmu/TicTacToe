package com.boardgames.tictactoe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.boardgames.GameSelectionGUI;

public class TicTacToeGUI extends JFrame {

    public enum GameMode {
        PLAYER_VS_PLAYER,
        PLAYER_VS_AI
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final JButton[][] buttons = new JButton[3][3];
    private JLabel statusLabel;
    private final TicTacToeGame game;
    private GameMode gameMode;

    // --- Modern, Simple Color Palette (shadcn/ui inspired) ---
    private final Color COLOR_BACKGROUND = new Color(248, 249, 250); // Off-white
    private final Color COLOR_CARD = Color.WHITE;
    private final Color COLOR_TEXT_PRIMARY = new Color(33, 37, 41); // Dark Gray
    private final Color COLOR_PRIMARY = new Color(73, 80, 87); // Grayish accent
    private final Color COLOR_BORDER = new Color(222, 226, 230); // Light Gray Border
    private final Color COLOR_PLAYER_X = new Color(23, 113, 230); // Blue
    private final Color COLOR_PLAYER_O = new Color(230, 126, 34); // Orange

    // --- Fonts ---
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 72);
    private final Font FONT_LABEL = new Font("微軟正黑體", Font.BOLD, 22);
    private final Font FONT_NEW_GAME = new Font("微軟正黑體", Font.PLAIN, 16);
    private final Font FONT_MODE_TITLE = new Font("微軟正黑體", Font.BOLD, 32);
    private final Font FONT_MODE_BUTTON = new Font("微軟正黑體", Font.BOLD, 18);

    public TicTacToeGUI() {
        this.game = new TicTacToeGame();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("井字棋遊戲");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create Mode Selection Panel
        JPanel modeSelectionPanel = createModeSelectionPanel();

        // Create Game Panel
        JPanel gamePanel = createGamePanel();

        // Add panels to the main panel with CardLayout
        mainPanel.add(modeSelectionPanel, "MODE_SELECTION");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel);
        cardLayout.show(mainPanel, "MODE_SELECTION");
    }

    private JPanel createModeSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("請選擇遊戲模式", SwingConstants.CENTER);
        titleLabel.setFont(FONT_MODE_TITLE);
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        gbc.insets = new Insets(10, 10, 30, 10);
        panel.add(titleLabel, gbc);

        JButton pvpButton = createStyledModeButton("玩家 vs. 玩家");
        pvpButton.addActionListener(e -> startGame(GameMode.PLAYER_VS_PLAYER));
        gbc.insets = new Insets(10, 40, 10, 40);
        panel.add(pvpButton, gbc);

        JButton pvaButton = createStyledModeButton("玩家 vs. 電腦");
        pvaButton.addActionListener(e -> startGame(GameMode.PLAYER_VS_AI));
        panel.add(pvaButton, gbc);

        JButton backButton = new JButton("返回遊戲選擇");
        backButton.setFont(FONT_NEW_GAME);
        backButton.addActionListener(e -> {
            this.dispose();
            new GameSelectionGUI().setVisible(true);
        });
        gbc.insets = new Insets(20, 40, 10, 40);
        panel.add(backButton, gbc);

        return panel;
    }

    private JButton createStyledModeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_MODE_BUTTON);
        button.setBackground(COLOR_CARD);
        button.setForeground(COLOR_TEXT_PRIMARY);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 2),
                BorderFactory.createEmptyBorder(15, 30, 15, 30)));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(COLOR_PRIMARY);
                button.setForeground(COLOR_CARD);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_CARD);
                button.setForeground(COLOR_TEXT_PRIMARY);
            }
        });
        return button;
    }

    private JPanel createGamePanel() {
        JPanel gamePanel = new JPanel(new BorderLayout(0, 15));
        gamePanel.setBackground(COLOR_BACKGROUND);
        gamePanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BACKGROUND);
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(FONT_LABEL);
        statusLabel.setForeground(COLOR_TEXT_PRIMARY);
        headerPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        boardPanel.setBackground(COLOR_BACKGROUND);
        boardPanel.setBorder(BorderFactory.createLineBorder(COLOR_BACKGROUND, 10));

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
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                        JButton button = (JButton) evt.getSource();
                        if (button.isEnabled())
                            button.setBorder(buttonHoverBorder);
                    }

                    public void mouseExited(MouseEvent evt) {
                        JButton button = (JButton) evt.getSource();
                        button.setBorder(buttonBorder);
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(COLOR_BACKGROUND);
        JButton newGameButton = new JButton("新遊戲");
        newGameButton.setFont(FONT_NEW_GAME);
        newGameButton.setFocusable(false);
        newGameButton.setBackground(COLOR_CARD);
        newGameButton.setForeground(COLOR_TEXT_PRIMARY);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "MODE_SELECTION"));
        footerPanel.add(newGameButton);

        gamePanel.add(headerPanel, BorderLayout.NORTH);
        gamePanel.add(boardPanel, BorderLayout.CENTER);
        gamePanel.add(footerPanel, BorderLayout.SOUTH);
        return gamePanel;
    }

    private void startGame(GameMode mode) {
        this.gameMode = mode;
        game.reset();
        updateView();
        cardLayout.show(mainPanel, "GAME");
    }

    private class ButtonClickListener implements ActionListener {
        private final int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getGameState() != TicTacToeGame.GameState.PLAYING)
                return;
            if (game.makeMove(row, col)) {
                updateView();
                if (gameMode == GameMode.PLAYER_VS_AI &&
                        game.getGameState() == TicTacToeGame.GameState.PLAYING &&
                        game.getCurrentPlayer() == 'O') {
                    handleAITurn();
                }
            }
        }
    }

    private void handleAITurn() {
        for (JButton[] row : buttons)
            for (JButton button : row)
                button.setEnabled(false);
        Timer timer = new Timer(500, e -> {
            int[] aiMove = TicTacToeAIPlayer.findRandomMove(game.getBoard());
            if (aiMove != null) {
                game.makeMove(aiMove[0], aiMove[1]);
                updateView();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateView() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char symbol = game.getSymbolAt(i, j);
                buttons[i][j].setText(String.valueOf(symbol).trim());
                buttons[i][j].setForeground(symbol == 'X' ? COLOR_PLAYER_X : COLOR_PLAYER_O);
                boolean enableButton = (symbol == ' ' && game.getGameState() == TicTacToeGame.GameState.PLAYING);
                if (gameMode == GameMode.PLAYER_VS_AI && game.getCurrentPlayer() == 'O' && enableButton) {
                    enableButton = false;
                }
                buttons[i][j].setEnabled(enableButton);
                buttons[i][j].setBackground(COLOR_CARD);
            }
        }

        TicTacToeGame.GameState state = game.getGameState();
        switch (state) {
            case PLAYING:
                statusLabel.setText("玩家 " + game.getCurrentPlayer() + " 的回合");
                break;
            case X_WINS:
                statusLabel.setText("恭喜玩家 X 獲勝！");
                highlightWinningButtons('X');
                break;
            case O_WINS:
                statusLabel.setText("恭喜玩家 O 獲勝！");
                highlightWinningButtons('O');
                break;
            case DRAW:
                statusLabel.setText("遊戲平局！");
                break;
        }
    }

    private void highlightWinningButtons(char winner) {
        Color winColor = (winner == 'X' ? COLOR_PLAYER_X : COLOR_PLAYER_O).brighter();
        int[][] winLines = {
                { 0, 0, 0, 1, 0, 2 }, { 1, 0, 1, 1, 1, 2 }, { 2, 0, 2, 1, 2, 2 }, // rows
                { 0, 0, 1, 0, 2, 0 }, { 0, 1, 1, 1, 2, 1 }, { 0, 2, 1, 2, 2, 2 }, // cols
                { 0, 0, 1, 1, 2, 2 }, { 0, 2, 1, 1, 2, 0 } // diags
        };
        for (int[] line : winLines) {
            if (game.getSymbolAt(line[0], line[1]) == winner &&
                    game.getSymbolAt(line[2], line[3]) == winner &&
                    game.getSymbolAt(line[4], line[5]) == winner) {
                setWinnerColor(winColor, buttons[line[0]][line[1]], buttons[line[2]][line[3]],
                        buttons[line[4]][line[5]]);
                return;
            }
        }
    }

    private void setWinnerColor(Color color, JButton... winnerButtons) {
        for (JButton button : winnerButtons) {
            button.setBackground(color.brighter());
            button.setOpaque(true);
        }
    }

}
