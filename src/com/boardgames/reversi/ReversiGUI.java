package com.boardgames.reversi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.boardgames.GameSelectionGUI;

public class ReversiGUI extends JFrame {
    public enum GameMode {
        PLAYER_VS_PLAYER,
        PLAYER_VS_AI
    }

    private static final int BOARD_SIZE = 8;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private final ReversiGame game;
    private GameMode gameMode;

    // --- Modern, Simple Color Palette (shadcn/ui inspired) ---
    private final Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private final Color COLOR_BOARD = new Color(34, 139, 34); // A nice green
    private final Color COLOR_CARD = Color.WHITE;
    private final Color COLOR_TEXT_PRIMARY = new Color(33, 37, 41);
    private final Color COLOR_TEXT_SECONDARY = new Color(108, 117, 125);
    private final Color COLOR_PRIMARY = new Color(73, 80, 87);
    private final Color COLOR_BORDER = new Color(222, 226, 230);

    // --- Fonts ---
    private final Font FONT_LABEL = new Font("微軟正黑體", Font.BOLD, 20);
    private final Font FONT_SCORE = new Font("微軟正黑體", Font.BOLD, 16);
    private final Font FONT_NEW_GAME = new Font("微軟正黑體", Font.PLAIN, 16);
    private final Font FONT_MODE_TITLE = new Font("微軟正黑體", Font.BOLD, 32);
    private final Font FONT_MODE_BUTTON = new Font("微軟正黑體", Font.BOLD, 18);

    public ReversiGUI() {
        this.game = new ReversiGame();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("黑白棋遊戲");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel modeSelectionPanel = createModeSelectionPanel();
        JPanel gamePanel = createGamePanel();

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

        // Header Panel for status and score
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_BACKGROUND);
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(FONT_LABEL);
        statusLabel.setForeground(COLOR_TEXT_PRIMARY);
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(FONT_SCORE);
        scoreLabel.setForeground(COLOR_TEXT_SECONDARY);
        headerPanel.add(statusLabel, BorderLayout.NORTH);
        headerPanel.add(scoreLabel, BorderLayout.SOUTH);

        // Board Panel
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE, 2, 2));
        boardPanel.setBackground(new Color(30, 130, 30));
        boardPanel.setBorder(BorderFactory.createLineBorder(COLOR_BOARD, 5));

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFocusable(false);
                if ((i + j) % 2 == 0) {
                    buttons[i][j].setBackground(new Color(34, 139, 34));
                } else {
                    buttons[i][j].setBackground(new Color(30, 130, 30));
                }
                buttons[i][j].setBorder(null);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                final int r = i;
                final int c = j;
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                        if (game.isValidMove(r, c)) {
                            if ((r + c) % 2 == 0) {
                                buttons[r][c].setBackground(new Color(34, 139, 34).brighter());
                            } else {
                                buttons[r][c].setBackground(new Color(30, 130, 30).brighter());
                            }
                        }
                    }

                    public void mouseExited(MouseEvent evt) {
                        if ((r + c) % 2 == 0) {
                            buttons[r][c].setBackground(new Color(34, 139, 34));
                        } else {
                            buttons[r][c].setBackground(new Color(30, 130, 30));
                        }
                    }
                });
                boardPanel.add(buttons[i][j]);
            }
        }

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(COLOR_BACKGROUND);
        JButton newGameButton = new JButton("新遊戲");
        newGameButton.setFont(FONT_NEW_GAME);
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

    private void handleAITurn() {
        for (JButton[] row : buttons)
            for (JButton button : row)
                button.setEnabled(false);
        Timer timer = new Timer(1000, e -> {
            int[] aiMove = ReversiAIPlayer.findBestMove(game);
            if (aiMove != null) {
                game.makeMove(aiMove[0], aiMove[1]);
                updateView();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private class ButtonClickListener implements ActionListener {
        private final int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getGameState() != ReversiGame.GameState.PLAYING)
                return;

            if (game.makeMove(row, col)) {
                updateView();
                if (gameMode == GameMode.PLAYER_VS_AI &&
                        game.getGameState() == ReversiGame.GameState.PLAYING &&
                        game.getCurrentPlayer() == 'W') {
                    handleAITurn();
                }
            }
        }
    }

    private void updateView() {
        char currentPlayer = game.getCurrentPlayer();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                char symbol = game.getSymbolAt(i, j);
                buttons[i][j].setText(""); // Clear text
                buttons[i][j].setIcon(getIconForSymbol(symbol, i, j));
                buttons[i][j].setDisabledIcon(getIconForSymbol(symbol, i, j));
                buttons[i][j].setEnabled(game.getGameState() == ReversiGame.GameState.PLAYING);
            }
        }

        // Highlight valid moves
        if (game.getGameState() == ReversiGame.GameState.PLAYING) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (game.isValidMoveForPlayer(i, j, currentPlayer)) {
                        buttons[i][j].setIcon(getIconForSymbol('+', i, j));
                    }
                }
            }
        }

        ReversiGame.GameState state = game.getGameState();
        switch (state) {
            case PLAYING:
                statusLabel.setText("玩家 " + (currentPlayer == 'B' ? "黑棋" : "白棋") + " 的回合");
                break;
            case BLACK_WINS:
                statusLabel.setText("恭喜黑棋獲勝！");
                break;
            case WHITE_WINS:
                statusLabel.setText("恭喜白棋獲勝！");
                break;
            case DRAW:
                statusLabel.setText("遊戲平局！");
                break;
        }

        int[] score = game.getScore();
        scoreLabel.setText(String.format("黑棋: %d, 白棋: %d", score[0], score[1]));
    }

    private Icon getIconForSymbol(char symbol, int row, int col) {
        int size = buttons[row][col].getWidth();
        if (size == 0)
            size = 50; // Default size

        Color primaryColor;
        boolean fill = true;
        switch (symbol) {
            case 'B':
                primaryColor = Color.BLACK;
                break;
            case 'W':
                primaryColor = Color.WHITE;
                break;
            case '+': // Valid move hint
                primaryColor = new Color(255, 255, 255, 120);
                fill = false;
                break;
            default:
                return null;
        }

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (fill) {
            // Use a gradient for a more 3D look
            Color lighterColor = primaryColor.brighter();
            if (primaryColor == Color.BLACK) {
                lighterColor = new Color(80, 80, 80);
            }
            if (primaryColor == Color.WHITE) {
                lighterColor = new Color(200, 200, 200);
            }
            Point2D center = new Point2D.Float(size / 2.0f, size / 2.0f);
            float radius = size / 2.0f;
            float[] dist = { 0.0f, 1.0f };
            Point2D focus = new Point2D.Float(size / 2.0f - 2, size / 2.0f - 2);
            Color[] colors = { lighterColor, primaryColor };
            RadialGradientPaint p = new RadialGradientPaint(center, radius, focus, dist, colors,
                    MultipleGradientPaint.CycleMethod.NO_CYCLE);
            g2d.setPaint(p);
            g2d.fillOval(2, 2, size - 5, size - 5);
        } else {
            g2d.setColor(primaryColor);
            g2d.drawOval(size / 2 - 5, size / 2 - 5, 10, 10);
        }

        g2d.dispose();
        return new ImageIcon(image);
    }
}
