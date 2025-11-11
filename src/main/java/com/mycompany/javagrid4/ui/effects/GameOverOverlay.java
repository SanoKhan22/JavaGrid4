package com.mycompany.javagrid4.ui.effects;

import com.mycompany.javagrid4.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Game Over overlay that displays over the final board state.
 * Shows winner, final scores, and animates in before transitioning to results.
 */
public class GameOverOverlay extends JComponent {
    private final String winnerText;
    private final String player1Name;
    private final String player2Name;
    private final int player1Score;
    private final int player2Score;
    private final Color player1Color;
    private final Color player2Color;
    private final Runnable onComplete;
    
    private Timer animationTimer;
    private double fadeProgress = 0.0;
    private double scaleProgress = 0.0;
    private static final int FADE_FRAMES = 10;  // 0.16 seconds (optimized)
    private static final int HOLD_FRAMES = 60;  // 1.0 second (optimized)
    private static final int TOTAL_FRAMES = FADE_FRAMES + HOLD_FRAMES;
    private int frameCount = 0;
    
    public GameOverOverlay(Player winner, 
                          String p1Name, String p2Name,
                          int p1Score, int p2Score,
                          Color p1Color, Color p2Color,
                          Runnable onComplete) {
        this.player1Name = p1Name;
        this.player2Name = p2Name;
        this.player1Score = p1Score;
        this.player2Score = p2Score;
        this.player1Color = p1Color;
        this.player2Color = p2Color;
        this.onComplete = onComplete;
        
        if (winner == null) {
            this.winnerText = "It's a Tie!";
        } else if (winner == Player.PLAYER_ONE) {
            this.winnerText = p1Name + " Wins!";
        } else {
            this.winnerText = p2Name + " Wins!";
        }
        
        setOpaque(false);
    }
    
    public void start() {
        animationTimer = new Timer(16, e -> {
            frameCount++;
            
            // Fade in phase (first FADE_FRAMES)
            if (frameCount <= FADE_FRAMES) {
                fadeProgress = frameCount / (double) FADE_FRAMES;
                scaleProgress = fadeProgress; // Scale up with fade
            }
            
            // Hold phase (wait for player to see results)
            if (frameCount >= TOTAL_FRAMES) {
                animationTimer.stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
            
            repaint();
        });
        animationTimer.start();
    }
    
    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Semi-transparent dark overlay background
        int bgAlpha = (int) (180 * fadeProgress);
        g2d.setColor(new Color(0, 0, 0, bgAlpha));
        g2d.fillRect(0, 0, width, height);
        
        // Calculate card dimensions
        int cardWidth = Math.min(500, width - 100);
        int cardHeight = 280;
        int cardX = (width - cardWidth) / 2;
        int cardY = (height - cardHeight) / 2;
        
        // Apply scale animation (subtle bounce)
        double scale = 0.7 + (0.3 * scaleProgress); // Scale from 0.7 to 1.0
        g2d.translate(width / 2, height / 2);
        g2d.scale(scale, scale);
        g2d.translate(-width / 2, -height / 2);
        
        // Draw card background with rounded corners
        int cardAlpha = (int) (255 * fadeProgress);
        g2d.setColor(new Color(255, 255, 255, cardAlpha));
        RoundRectangle2D card = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, 30, 30);
        g2d.fill(card);
        
        // Draw card border (subtle shadow effect)
        g2d.setColor(new Color(0, 0, 0, (int) (50 * fadeProgress)));
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(card);
        
        // Draw "GAME OVER" text at top
        g2d.setColor(new Color(100, 100, 120, cardAlpha));
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        String gameOverText = "GAME OVER";
        FontMetrics fmGameOver = g2d.getFontMetrics();
        int gameOverX = cardX + (cardWidth - fmGameOver.stringWidth(gameOverText)) / 2;
        int gameOverY = cardY + 50;
        g2d.drawString(gameOverText, gameOverX, gameOverY);
        
        // Draw winner text (larger, centered)
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        Color winnerColor = getWinnerColor();
        g2d.setColor(new Color(winnerColor.getRed(), winnerColor.getGreen(), 
                               winnerColor.getBlue(), cardAlpha));
        FontMetrics fmWinner = g2d.getFontMetrics();
        int winnerX = cardX + (cardWidth - fmWinner.stringWidth(winnerText)) / 2;
        int winnerY = cardY + 110;
        
        // Draw winner text shadow
        g2d.setColor(new Color(0, 0, 0, (int) (30 * fadeProgress)));
        g2d.drawString(winnerText, winnerX + 2, winnerY + 2);
        
        // Draw winner text
        g2d.setColor(new Color(winnerColor.getRed(), winnerColor.getGreen(), 
                               winnerColor.getBlue(), cardAlpha));
        g2d.drawString(winnerText, winnerX, winnerY);
        
        // Draw divider line
        g2d.setColor(new Color(200, 200, 210, cardAlpha));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(cardX + 50, cardY + 135, cardX + cardWidth - 50, cardY + 135);
        
        // Draw final scores - side by side with better layout
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        int scoreY = cardY + 180;
        
        // Player 1 score
        String p1Text = player1Name + ": " + player1Score;
        FontMetrics fmScore = g2d.getFontMetrics();
        int p1X = cardX + (cardWidth / 2) - fmScore.stringWidth(p1Text) - 30;
        g2d.setColor(new Color(player1Color.getRed(), player1Color.getGreen(), 
                               player1Color.getBlue(), cardAlpha));
        g2d.drawString(p1Text, p1X, scoreY);
        
        // VS text
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(new Color(150, 150, 160, cardAlpha));
        String vsText = "vs";
        FontMetrics fmVs = g2d.getFontMetrics();
        int vsX = cardX + (cardWidth - fmVs.stringWidth(vsText)) / 2;
        g2d.drawString(vsText, vsX, scoreY - 2);
        
        // Player 2 score
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String p2Text = player2Name + ": " + player2Score;
        int p2X = cardX + (cardWidth / 2) + 30;
        g2d.setColor(new Color(player2Color.getRed(), player2Color.getGreen(), 
                               player2Color.getBlue(), cardAlpha));
        g2d.drawString(p2Text, p2X, scoreY);
        
        // Draw footer text
        g2d.setFont(new Font("Arial", Font.ITALIC, 16));
        g2d.setColor(new Color(120, 120, 140, cardAlpha));
        String footerText = "Showing results in a moment...";
        FontMetrics fmFooter = g2d.getFontMetrics();
        int footerX = cardX + (cardWidth - fmFooter.stringWidth(footerText)) / 2;
        int footerY = cardY + cardHeight - 30;
        g2d.drawString(footerText, footerX, footerY);
    }
    
    private Color getWinnerColor() {
        if (winnerText.contains(player1Name)) {
            return player1Color;
        } else if (winnerText.contains(player2Name)) {
            return player2Color;
        } else {
            // Tie - use purple blend
            return new Color(120, 80, 180);
        }
    }
}
