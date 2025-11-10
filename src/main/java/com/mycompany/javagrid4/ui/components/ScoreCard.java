package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Material Design score card component for displaying player information.
 * Features elevated card design with shadows, rounded corners, and clean typography.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class ScoreCard extends JPanel {
    private static final int CARD_HEIGHT = 80;
    private static final int CARD_WIDTH = 200;
    private static final int CORNER_RADIUS = 12;
    private static final int ELEVATION = 4;
    private static final int PROGRESS_BAR_HEIGHT = 6;
    
    private String title;
    private String value;
    private Color accentColor;
    private boolean isActive;
    
    // Progress bar properties
    private int currentScore;
    private int maxScore;
    private float progressPercentage;
    private float animatedProgress; // For smooth animation
    private Timer progressAnimationTimer;
    
    private JLabel titleLabel;
    private JLabel valueLabel;
    
    /**
     * Creates a new score card with title and initial value.
     * 
     * @param title The card title (e.g., "Player 1", "Current Turn")
     * @param value The initial value to display
     * @param accentColor The accent color for the card
     * @param isActive Whether this card should have elevated/active styling
     */
    public ScoreCard(String title, String value, Color accentColor, boolean isActive) {
        this.title = title;
        this.value = value;
        this.accentColor = accentColor;
        this.isActive = isActive;
        
        // Initialize progress tracking (default max score is 100)
        this.currentScore = 0;
        this.maxScore = 100;
        this.progressPercentage = 0.0f;
        this.animatedProgress = 0.0f;
        
        setOpaque(false);
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setLayout(new BorderLayout(10, 5));
        
        initComponents();
    }
    
    /**
     * Initializes the card components (title and value labels).
     */
    private void initComponents() {
        // Container with padding
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title label (smaller, subtle)
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 120));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Value label (larger, bold, colored)
        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add vertical spacing
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalGlue());
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Updates the value displayed on the card.
     * 
     * @param newValue The new value to display
     */
    public void setValue(String newValue) {
        this.value = newValue;
        valueLabel.setText(newValue);
        repaint();
    }
    
    /**
     * Updates the score and animates the progress bar.
     * 
     * @param score The new score value
     * @param maxScore The maximum possible score
     */
    public void updateScore(int score, int maxScore) {
        this.currentScore = score;
        this.maxScore = maxScore;
        
        // Calculate target percentage
        float targetProgress = maxScore > 0 ? (float) score / maxScore : 0.0f;
        targetProgress = Math.max(0.0f, Math.min(1.0f, targetProgress)); // Clamp 0-1
        
        this.progressPercentage = targetProgress;
        
        // Animate progress bar smoothly
        animateProgress(targetProgress);
    }
    
    /**
     * Animates the progress bar from current to target value.
     */
    private void animateProgress(float targetProgress) {
        // Stop existing animation if running
        if (progressAnimationTimer != null && progressAnimationTimer.isRunning()) {
            progressAnimationTimer.stop();
        }
        
        // Create smooth animation
        progressAnimationTimer = new Timer(16, e -> {
            float diff = targetProgress - animatedProgress;
            
            if (Math.abs(diff) < 0.01f) {
                // Close enough, snap to target
                animatedProgress = targetProgress;
                progressAnimationTimer.stop();
            } else {
                // Move towards target (ease-out)
                animatedProgress += diff * 0.15f;
            }
            
            repaint();
        });
        
        progressAnimationTimer.start();
    }
    
    /**
     * Updates the accent color of the card.
     * 
     * @param newColor The new accent color
     */
    public void setAccentColor(Color newColor) {
        this.accentColor = newColor;
        valueLabel.setForeground(newColor);
        repaint();
    }
    
    /**
     * Sets whether this card is active (elevated styling).
     * 
     * @param active True for active/elevated, false for normal
     */
    public void setActive(boolean active) {
        this.isActive = active;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow
        drawShadow(g2d, width, height);
        
        // Draw card background
        drawCardBackground(g2d, width, height);
        
        // Draw progress bar at bottom
        drawProgressBar(g2d, width, height);
        
        // Draw accent border/indicator
        if (isActive) {
            drawAccentBorder(g2d, width, height);
        }
        
        g2d.dispose();
    }
    
    /**
     * Draws the card shadow for depth.
     */
    private void drawShadow(Graphics2D g2d, int width, int height) {
        int elevation = isActive ? ELEVATION + 4 : ELEVATION;
        
        // Multi-layer shadow for depth
        for (int i = 5; i >= 1; i--) {
            int alpha = Math.min((elevation * 12) / (i + 1), 80);
            int offset = elevation + (i * 2) - 2;
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fill(new RoundRectangle2D.Float(
                offset, 
                offset + (i / 2),
                width - offset * 2, 
                height - offset * 2, 
                CORNER_RADIUS + (i / 2), 
                CORNER_RADIUS + (i / 2)
            ));
        }
    }
    
    /**
     * Draws the solid card background.
     */
    private void drawCardBackground(Graphics2D g2d, int width, int height) {
        // White/light background with subtle tint
        Color bgColor;
        if (isActive) {
            // Subtle tint of accent color for active card
            bgColor = new Color(
                Math.min(255, 240 + accentColor.getRed() / 20),
                Math.min(255, 240 + accentColor.getGreen() / 20),
                Math.min(255, 240 + accentColor.getBlue() / 20)
            );
        } else {
            bgColor = new Color(252, 252, 255);
        }
        
        g2d.setColor(bgColor);
        g2d.fill(new RoundRectangle2D.Float(
            0, 0,
            width, 
            height, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
        
        // Subtle border
        g2d.setColor(new Color(220, 220, 230));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Float(
            0.5f, 0.5f,
            width - 1, 
            height - 1, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws the progress bar at the bottom of the card.
     */
    private void drawProgressBar(Graphics2D g2d, int width, int height) {
        int barHeight = PROGRESS_BAR_HEIGHT;
        int barY = height - barHeight - 8; // 8px from bottom
        int barX = 10; // 10px margin
        int barWidth = width - 20; // 10px margin on each side
        
        // Background track (light gray)
        g2d.setColor(new Color(230, 230, 240));
        g2d.fill(new RoundRectangle2D.Float(
            barX, barY,
            barWidth, barHeight,
            barHeight / 2, barHeight / 2 // Fully rounded ends
        ));
        
        // Progress fill (animated)
        if (animatedProgress > 0) {
            int fillWidth = (int) (barWidth * animatedProgress);
            
            // Create gradient for progress bar
            GradientPaint gradient = new GradientPaint(
                barX, barY,
                accentColor,
                barX + fillWidth, barY,
                accentColor.brighter()
            );
            
            g2d.setPaint(gradient);
            g2d.fill(new RoundRectangle2D.Float(
                barX, barY,
                fillWidth, barHeight,
                barHeight / 2, barHeight / 2
            ));
            
            // Add subtle glow on progress bar
            g2d.setColor(new Color(
                accentColor.getRed(),
                accentColor.getGreen(),
                accentColor.getBlue(),
                60
            ));
            g2d.fill(new RoundRectangle2D.Float(
                barX, barY - 1,
                fillWidth, barHeight + 2,
                barHeight / 2, barHeight / 2
            ));
        }
    }
    
    /**
     * Draws accent border for active cards.
     */
    private void drawAccentBorder(Graphics2D g2d, int width, int height) {
        // Top accent bar
        g2d.setColor(accentColor);
        g2d.fill(new RoundRectangle2D.Float(
            0, 0,
            width, 
            4, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        // Stop animation timer when component is removed
        if (progressAnimationTimer != null && progressAnimationTimer.isRunning()) {
            progressAnimationTimer.stop();
        }
    }
}
