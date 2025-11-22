package com.boardgames;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import com.boardgames.reversi.ReversiGUI;
import com.boardgames.tictactoe.TicTacToeGUI;

public class GameSelectionGUI extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // --- Modern, Simple Color Palette (shadcn/ui inspired) ---
    private final Color COLOR_BACKGROUND = new Color(248, 249, 250); // Off-white
    private final Color COLOR_CARD = Color.WHITE;
    private final Color COLOR_TEXT_PRIMARY = new Color(33, 37, 41); // Dark Gray
    private final Color COLOR_PRIMARY = new Color(73, 80, 87); // Grayish accent
    private final Color COLOR_BORDER = new Color(222, 226, 230); // Light Gray Border

    // --- Fonts ---
    private final Font FONT_MODE_TITLE = new Font("微軟正黑體", Font.BOLD, 32);
    private final Font FONT_MODE_BUTTON = new Font("微軟正黑體", Font.BOLD, 18);

    public GameSelectionGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("遊戲選擇");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel gameSelectionPanel = createGameSelectionPanel();

        mainPanel.add(gameSelectionPanel, "GAME_SELECTION");

        add(mainPanel);
        cardLayout.show(mainPanel, "GAME_SELECTION");
    }

    private JPanel createGameSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("請選擇遊戲", SwingConstants.CENTER);
        titleLabel.setFont(FONT_MODE_TITLE);
        titleLabel.setForeground(COLOR_TEXT_PRIMARY);
        gbc.insets = new Insets(10, 10, 30, 10);
        panel.add(titleLabel, gbc);

        JButton ticTacToeButton = createStyledModeButton("井字棋");
        ticTacToeButton.addActionListener(e -> {
            this.dispose();
            new TicTacToeGUI().setVisible(true);
        });
        gbc.insets = new Insets(10, 40, 10, 40);
        panel.add(ticTacToeButton, gbc);

        JButton reversiButton = createStyledModeButton("黑白棋");
        reversiButton.addActionListener(e -> {
            this.dispose();
            new ReversiGUI().setVisible(true);
        });
        panel.add(reversiButton, gbc);

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
                BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
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
}
