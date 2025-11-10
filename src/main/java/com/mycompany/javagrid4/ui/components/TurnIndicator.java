package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Material Design turn indicator with animated pulse effect.
 * Displays the current player's turn with color coding and smooth animations.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class TurnIndicator extends JPanel {
    private static final int CARD_HEIGHT = 70;
    private static final int CARD_WIDTH = 280;
    private static final int CORNER_RADIUS = 16;
    private static final int ELEVATION = 4;
    
    private String playerName;
    private Color playerColor;
    private float pulseAlpha;
    private boolean pulseIncreasing;
    private Timer pulseTimer;
    
    /**
     * Creates a new turn indicator.
     * 
     * @param playerName The name of the current player
     * @param playerColor The color representing the current player
     */
    public TurnIndicator(String playerName, Color playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.pulseAlpha = 0.3f;
        this.pulseIncreasing = true;
        
        setOpaque(false);
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        
        // Start pulse animation
        startPulseAnimation();
    }
    
    /**
     * Updates the turn indicator with new player information.
     * 
     * @param playerName The name of the current player
     * @param playerColor The color representing the current player
     */
    public void updatePlayer(String playerName, Color playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        repaint();
    }
    
    /**
     * Starts the pulse animation effect.
     */
    private void startPulseAnimation() {
        pulseTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pulseIncreasing) {
                    pulseAlpha += 0.02f;
                    if (pulseAlpha >= 0.6f) {
                        pulseAlpha = 0.6f;
                        pulseIncreasing = false;
                    }
                } else {
                    pulseAlpha -= 0.02f;
                    if (pulseAlpha <= 0.2f) {
                        pulseAlpha = 0.2f;
                        pulseIncreasing = true;
                    }
                }
                repaint();
            }
        });
        pulseTimer.start();
    }
    
    /**
     * Stops the pulse animation.
     */
    public void stopAnimation() {
        if (pulseTimer != null) {
            pulseTimer.stop();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw multi-layer shadow
        drawShadow(g2d, width, height);
        
        // Draw card background
        drawCardBackground(g2d, width, height);
        
        // Draw pulse effect
        drawPulseEffect(g2d, width, height);
        
        // Draw player indicator dot
        drawPlayerDot(g2d, width, height);
        
        // Draw text
        drawText(g2d, width, height);
        
        g2d.dispose();
    }
    
    /**
     * Draws multi-layer shadow for elevation effect.
     */
    private void drawShadow(Graphics2D g2d, int width, int height) {
        int shadowLayers = 5;
        for (int i = shadowLayers; i > 0; i--) {
            float alpha = (0.15f / shadowLayers) * i;
            int offset = (ELEVATION * i) / shadowLayers;
            
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fill(new RoundRectangle2D.Float(
                offset, 
                offset, 
                width - (offset * 2), 
                height - (offset * 2), 
                CORNER_RADIUS, 
                CORNER_RADIUS
            ));
        }
    }
    
    /**
     * Draws the card background.
     */
    private void drawCardBackground(Graphics2D g2d, int width, int height) {
        // White background
        g2d.setColor(new Color(252, 252, 255));
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
        
        // Colored border
        g2d.setColor(playerColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(new RoundRectangle2D.Float(1.5f, 1.5f, width - 3, height - 3, CORNER_RADIUS, CORNER_RADIUS));
    }
    
    /**
     * Draws the animated pulse effect.
     */
    private void drawPulseEffect(Graphics2D g2d, int width, int height) {
        Color pulseColor = new Color(
            playerColor.getRed(),
            playerColor.getGreen(),
            playerColor.getBlue(),
            (int)(pulseAlpha * 255)
        );
        
        g2d.setColor(pulseColor);
        g2d.fill(new RoundRectangle2D.Float(3, 3, width - 6, height - 6, CORNER_RADIUS - 2, CORNER_RADIUS - 2));
    }
    
    /**
     * Draws the player indicator dot.
     */
    private void drawPlayerDot(Graphics2D g2d, int width, int height) {
        int dotSize = 18;
        int dotX = 20;
        int dotY = (height - dotSize) / 2;
        
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(dotX + 2, dotY + 2, dotSize, dotSize);
        
        // Colored dot
        g2d.setColor(playerColor);
        g2d.fillOval(dotX, dotY, dotSize, dotSize);
        
        // Highlight
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.fillOval(dotX + 3, dotY + 3, dotSize / 3, dotSize / 3);
    }
    
    /**
     * Draws the player name text.
     */
    private void drawText(Graphics2D g2d, int width, int height) {
        // "Current Turn" label
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        g2d.setColor(new Color(100, 100, 120));
        String label = "CURRENT TURN";
        FontMetrics fmLabel = g2d.getFontMetrics();
        int labelWidth = fmLabel.stringWidth(label);
        g2d.drawString(label, (width - labelWidth) / 2, height / 2 - 8);
        
        // Player name
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(playerColor);
        FontMetrics fmName = g2d.getFontMetrics();
        int nameWidth = fmName.stringWidth(playerName);
        g2d.drawString(playerName, (width - nameWidth) / 2, height / 2 + 16);
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        stopAnimation();
    }
}
