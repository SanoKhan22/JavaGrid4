package com.mycompany.javagrid4.ui.screens;

import com.mycompany.javagrid4.Player;
import com.mycompany.javagrid4.models.GameConfig;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Results screen displaying game outcome.
 * Features:
 * - Winner announcement with player colors
 * - Final score display
 * - Play Again button (new game with same config)
 * - Back to Menu button (return to player setup)
 * - Professional layout matching menu style
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class ResultsPanel extends JPanel {
    
    private final PropertyChangeSupport propertyChangeSupport;
    
    // Game results
    private GameConfig gameConfig;
    private Player winner;
    private int player1Score;
    private int player2Score;
    
    // UI Components
    private JLabel titleLabel;
    private JLabel winnerLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JButton playAgainButton;
    private JButton backToMenuButton;
    
    /**
     * Creates the results panel.
     */
    public ResultsPanel() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));
        
        initComponents();
        layoutComponents();
    }
    
    /**
     * Sets the game results to display.
     * @param config Game configuration
     * @param winner Winning player (or null for tie)
     * @param player1Score Player 1's final score
     * @param player2Score Player 2's final score
     */
    public void setResults(GameConfig config, Player winner, int player1Score, int player2Score) {
        this.gameConfig = config;
        this.winner = winner;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        
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
        
        // Score display panel
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setBackground(new Color(240, 240, 245));
        scoresPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 2),
            BorderFactory.createEmptyBorder(20, 40, 20, 40)
        ));
        
        player1ScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoresPanel.add(player1ScoreLabel);
        scoresPanel.add(Box.createVerticalStrut(15));
        
        player2ScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoresPanel.add(player2ScoreLabel);
        
        scoresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(scoresPanel);
        centerPanel.add(Box.createVerticalStrut(50));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(new Color(240, 240, 245));
        buttonsPanel.add(playAgainButton);
        buttonsPanel.add(backToMenuButton);
        
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(buttonsPanel);
        
        add(centerPanel, BorderLayout.CENTER);
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
        } else {
            winnerLabel.setText(player2Name + " Wins! 🎉");
            winnerLabel.setForeground(player2Color);
        }
        
        // Update score labels
        player1ScoreLabel.setText(String.format("%s: %d points", player1Name, player1Score));
        player1ScoreLabel.setForeground(player1Color);
        
        player2ScoreLabel.setText(String.format("%s: %d points", player2Name, player2Score));
        player2ScoreLabel.setForeground(player2Color);
        
        revalidate();
        repaint();
    }
    
    /**
     * Handles Play Again button click.
     * Fires "playAgain" event with same GameConfig.
     */
    private void handlePlayAgain() {
        propertyChangeSupport.firePropertyChange("playAgain", null, gameConfig);
    }
    
    /**
     * Handles Back to Menu button click.
     * Fires "backToMenu" event.
     */
    private void handleBackToMenu() {
        propertyChangeSupport.firePropertyChange("backToMenu", null, null);
    }
}
