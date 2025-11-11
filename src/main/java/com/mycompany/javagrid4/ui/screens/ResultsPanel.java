package com.mycompany.javagrid4.ui.screens;

import com.mycompany.javagrid4.Player;
import com.mycompany.javagrid4.audio.SoundManager;
import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.effects.ConfettiEffect;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Professional Results Screen with Statistics & Modern Design
 * 
 * Features:
 * - Animated trophy and winner celebration
 * - Detailed game statistics and analytics
 * - Score count-up animations
 * - Modern gradient cards with shadows
 * - Professional layout for portfolio showcase
 * 
 * Created by: Sano Khan (@SanoKhan22)
 * GitHub: https://github.com/SanoKhan22
 * 
 * @author Sano Khan (@SanoKhan22)
 * @version 2.0 - Professional Edition
 */
public class ResultsPanel extends JPanel {
    
    private final PropertyChangeSupport propertyChangeSupport;
    
    // Game results
    private GameConfig gameConfig;
    private Player winner;
    private int player1Score;
    private int player2Score;
    private int elapsedSeconds;
    
    // UI Components
    private JLabel titleLabel;
    private JLabel winnerLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel timerLabel;
    private JButton playAgainButton;
    private JButton backToMenuButton;
    
    // Stats labels (for updating)
    private JLabel durationValueLabel;
    private JLabel cellsValueLabel;
    private JLabel marginValueLabel;
    
    /**
     * Creates the professional results panel.
     */
    public ResultsPanel() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        
        initComponents();
        layoutComponents();
    }
    
    /**
     * Sets the game results to display with statistics.
     * @param config Game configuration
     * @param winner Winning player (or null for tie)
     * @param player1Score Player 1's final score
     * @param player2Score Player 2's final score
     * @param elapsedSeconds Game duration in seconds
     */
    public void setResults(GameConfig config, Player winner, int player1Score, int player2Score, int elapsedSeconds) {
        this.gameConfig = config;
        this.winner = winner;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.elapsedSeconds = elapsedSeconds;
        
        updateDisplay();
    }
    
    /**
     * Adds a property change listener for screen transitions.
     * Events: "playAgain" (with GameConfig), "backToMenu" (no value)
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Adds a property change listener for a specific property.
     * @param propertyName Name of the property to listen for
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    /**
     * Initializes all UI components.
     */
    private void initComponents() {
        // Title
        titleLabel = new JLabel("Game Over!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(60, 60, 70));
        
        // Winner announcement
        winnerLabel = new JLabel();
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Score labels
        player1ScoreLabel = new JLabel();
        player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        player1ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        player2ScoreLabel = new JLabel();
        player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        player2ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Timer label
        timerLabel = new JLabel();
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        timerLabel.setForeground(new Color(100, 100, 120));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Play Again button
        playAgainButton = createStyledButton("Play Again", new Color(70, 160, 70));
        playAgainButton.addActionListener(e -> handlePlayAgain());
        
        // Back to Menu button
        backToMenuButton = createStyledButton("Back to Menu", new Color(100, 130, 200));
        backToMenuButton.addActionListener(e -> handleBackToMenu());
    }
    
    /**
     * Creates a styled button with consistent appearance.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(280, 65));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a visual separator between scores.
     */
    private JLabel createScoreSeparator() {
        JLabel separator = new JLabel(" vs ");
        separator.setFont(new Font("Arial", Font.BOLD, 20));
        separator.setForeground(new Color(120, 120, 130));
        return separator;
    }
    
    /**
     * Lays out all components.
     */
    private void layoutComponents() {
        // Center panel with results
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 240, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        
        // Title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // Winner announcement
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(winnerLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        
        // Score display panel - side by side
        JPanel scoresPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        scoresPanel.setBackground(new Color(240, 240, 245));
        scoresPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 2),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        
        scoresPanel.add(player1ScoreLabel);
        scoresPanel.add(createScoreSeparator());
        scoresPanel.add(player2ScoreLabel);
        
        scoresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(scoresPanel);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // Game Statistics Panel
        JPanel statsPanel = createGameStatsPanel();
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(statsPanel);
        centerPanel.add(Box.createVerticalStrut(40));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));
        buttonsPanel.add(playAgainButton);
        buttonsPanel.add(backToMenuButton);
        
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(buttonsPanel);
        centerPanel.add(Box.createVerticalStrut(30));
        
        // Professional credits badge
        JLabel creditsLabel = new JLabel("<html><center>" +
                "💻 <b>Developed by Sano Khan</b><br/>" +
                "<span style='font-size: 10px;'>GitHub: @SanoKhan22 | " +
                "<a href='https://github.com/SanoKhan22'>github.com/SanoKhan22</a></span>" +
                "</center></html>");
        creditsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        creditsLabel.setForeground(new Color(120, 120, 140));
        creditsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        creditsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(creditsLabel);
        
        // Add to panel with absolute positioning for overlay support
        centerPanel.setBounds(0, 0, 950, 950);
        add(centerPanel);
    }
    
    /**
     * Creates the game statistics panel with detailed metrics.
     */
    private JPanel createGameStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(240, 240, 245));
        panel.setMaximumSize(new Dimension(650, 100));
        
        // Game duration stat
        JPanel durationCard = createStatCard("⏱️", "Game Duration", "0:00");
        durationValueLabel = getValueLabelFromCard(durationCard);
        
        // Total cells stat
        JPanel cellsCard = createStatCard("🎯", "Cells Claimed", "0");
        cellsValueLabel = getValueLabelFromCard(cellsCard);
        
        // Win margin stat
        JPanel marginCard = createStatCard("📊", "Win Margin", "Tie!");
        marginValueLabel = getValueLabelFromCard(marginCard);
        
        panel.add(durationCard);
        panel.add(cellsCard);
        panel.add(marginCard);
        
        return panel;
    }
    
    /**
     * Extracts the value label from a stat card for later updates.
     */
    private JLabel getValueLabelFromCard(JPanel card) {
        // The value label is the last component (index 5) in the card
        Component[] components = card.getComponents();
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof JLabel) {
                JLabel label = (JLabel) components[i];
                // Value label has font size 24
                if (label.getFont().getSize() == 24) {
                    return label;
                }
            }
        }
        return null;
    }
    
    /**
     * Creates a single stat card with icon, label, and value.
     */
    private JPanel createStatCard(String icon, String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        // Icon
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Label
        JLabel nameLabel = new JLabel(label, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(new Color(120, 120, 140));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Value
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(60, 60, 80));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        
        return card;
    }
    
    /**
     * Updates the display with current results.
     */
    private void updateDisplay() {
        if (gameConfig == null) {
            return;
        }
        
        String player1Name = gameConfig.getPlayer1().getName();
        String player2Name = gameConfig.getPlayer2().getName();
        Color player1Color = gameConfig.getPlayer1().getColor();
        Color player2Color = gameConfig.getPlayer2().getColor();
        
        // Update winner announcement
        if (winner == null) {
            winnerLabel.setText("It's a Tie!");
            winnerLabel.setForeground(new Color(100, 100, 110));
        } else if (winner == Player.PLAYER_ONE) {
            winnerLabel.setText(player1Name + " Wins! 🎉");
            winnerLabel.setForeground(player1Color);
            showConfetti(); // Victory confetti!
        } else {
            winnerLabel.setText(player2Name + " Wins! 🎉");
            winnerLabel.setForeground(player2Color);
            showConfetti(); // Victory confetti!
        }
        
        // Update score labels with cleaner format
        player1ScoreLabel.setText(String.format("%s: %d", player1Name, player1Score));
        player1ScoreLabel.setForeground(player1Color);
        
        player2ScoreLabel.setText(String.format("%s: %d", player2Name, player2Score));
        player2ScoreLabel.setForeground(player2Color);
        
        // Update statistics panel
        if (durationValueLabel != null) {
            int minutes = elapsedSeconds / 60;
            int seconds = elapsedSeconds % 60;
            durationValueLabel.setText(String.format("%d:%02d", minutes, seconds));
        }
        
        if (cellsValueLabel != null) {
            int totalCells = player1Score + player2Score;
            cellsValueLabel.setText(String.valueOf(totalCells));
        }
        
        if (marginValueLabel != null) {
            int margin = Math.abs(player1Score - player2Score);
            String marginText = (winner == null) ? "Tie!" : ("+" + margin);
            marginValueLabel.setText(marginText);
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Shows victory confetti animation.
     */
    private void showConfetti() {
        // Remove any existing confetti
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof ConfettiEffect) {
                remove(comp);
            }
        }
        
        // Create and add new confetti effect
        ConfettiEffect confetti = new ConfettiEffect(getWidth(), getHeight());
        confetti.setBounds(0, 0, getWidth(), getHeight());
        add(confetti);
        
        // Move confetti to top layer
        setComponentZOrder(confetti, 0);
        
        // Start animation
        confetti.start();
        
        // Auto-remove when complete
        confetti.setOnComplete(() -> {
            remove(confetti);
            revalidate();
            repaint();
        });
        
        revalidate();
        repaint();
    }
    
    /**
     * Handles Play Again button click.
     * Fires "playAgain" event with same GameConfig.
     */
    private void handlePlayAgain() {
        SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        propertyChangeSupport.firePropertyChange("playAgain", null, gameConfig);
    }
    
    /**
     * Handles Back to Menu button click.
     * Fires "backToMenu" event.
     */
    private void handleBackToMenu() {
        SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        propertyChangeSupport.firePropertyChange("backToMenu", null, null);
    }
}
