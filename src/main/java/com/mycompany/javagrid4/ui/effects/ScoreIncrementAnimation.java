package com.mycompany.javagrid4.ui.effects;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Animates a flying "+X" score increment from a cell to the score display.
 * Creates a satisfying visual feedback when cells are claimed.
 * 
 * Features:
 * - Smooth ease-out movement curve
 * - Fade out as it approaches target
 * - Scale animation (bounce effect)
 * - Player-specific colors
 * - Particle trail effect (optional)
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class ScoreIncrementAnimation extends JComponent {
    
    private Point startPos;
    private Point endPos;
    private int scoreValue;
    private Color playerColor;
    private Timer animationTimer;
    private double progress = 0.0;
    
    private static final int TOTAL_FRAMES = 12; // ~200ms (optimized for performance)
    private static final int FRAME_DELAY = 16; // 60fps
    
    // Particle trail - disabled for performance
    private boolean showTrail = false;
    private java.util.List<TrailParticle> trailParticles = new java.util.ArrayList<>();
    
    /**
     * Creates a score increment animation.
     * @param start Starting position (cell center)
     * @param end Ending position (score label center)
     * @param score Score value to display ("+1", "+2", etc.)
     * @param color Player's color for the animation
     */
    public ScoreIncrementAnimation(Point start, Point end, int score, Color color) {
        this.startPos = new Point(start);
        this.endPos = new Point(end);
        this.scoreValue = score;
        this.playerColor = color;
        
        // Make component cover entire parent area
        setBounds(0, 0, 2000, 2000);
        setOpaque(false);
    }
    
    /**
     * Sets whether to show particle trail.
     * @param show true to show trail particles
     * @return this for method chaining
     */
    public ScoreIncrementAnimation setShowTrail(boolean show) {
        this.showTrail = show;
        return this;
    }
    
    /**
     * Starts the animation.
     */
    public void start() {
        progress = 0.0;
        trailParticles.clear();
        
        animationTimer = new Timer(FRAME_DELAY, e -> {
            progress += 1.0 / TOTAL_FRAMES;
            
            // Skip trail particles for performance
            
            if (progress >= 1.0) {
                animationTimer.stop();
                cleanup();
            } else {
                // Optimized repaint - only repaint the affected region
                Point pos = getCurrentPosition();
                repaint(pos.x - 50, pos.y - 50, 100, 100);
            }
        });
        
        animationTimer.start();
    }
    
    /**
     * Starts the animation with a callback.
     * @param onComplete Callback to execute when animation finishes
     */
    public void start(Runnable onComplete) {
        progress = 0.0;
        trailParticles.clear();
        
        animationTimer = new Timer(FRAME_DELAY, e -> {
            progress += 1.0 / TOTAL_FRAMES;
            
            // Skip trail particles for performance
            
            if (progress >= 1.0) {
                animationTimer.stop();
                cleanup();
                if (onComplete != null) {
                    onComplete.run();
                }
            } else {
                // Optimized repaint - only repaint the affected region
                Point pos = getCurrentPosition();
                repaint(pos.x - 50, pos.y - 50, 100, 100);
            }
        });
        
        animationTimer.start();
    }
    
    /**
     * Calculates current position using ease-out curve.
     * @return Current position
     */
    private Point getCurrentPosition() {
        // Ease-out cubic curve for smooth deceleration
        double eased = 1 - Math.pow(1 - progress, 3);
        
        int x = (int) (startPos.x + (endPos.x - startPos.x) * eased);
        int y = (int) (startPos.y + (endPos.y - startPos.y) * eased);
        
        return new Point(x, y);
    }
    
    /**
     * Calculates current alpha (transparency) based on progress.
     * @return Alpha value (0-255)
     */
    private int getCurrentAlpha() {
        // Fade out as it approaches target
        return (int) ((1 - progress) * 255);
    }
    
    /**
     * Calculates current scale based on progress.
     * @return Scale factor
     */
    private float getCurrentScale() {
        // Scale up then down (bounce effect)
        return (float) (1.0 + 0.5 * Math.sin(progress * Math.PI));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw trail particles first (behind the number)
        if (showTrail) {
            drawTrailParticles(g2d);
        }
        
        // Draw main flying number
        drawFlyingNumber(g2d);
        
        g2d.dispose();
    }
    
    /**
     * Draws the trail particles.
     */
    private void drawTrailParticles(Graphics2D g2d) {
        for (TrailParticle particle : trailParticles) {
            particle.age += 0.05;
            
            // Fade out older particles
            float particleAlpha = (float) Math.max(0, 1 - particle.age * 2);
            int alpha = (int) (particleAlpha * 150);
            
            if (alpha > 0) {
                Color particleColor = new Color(
                    playerColor.getRed(),
                    playerColor.getGreen(),
                    playerColor.getBlue(),
                    alpha
                );
                
                g2d.setColor(particleColor);
                
                // Draw small circle
                int size = (int) (4 * (1 - particle.age));
                g2d.fillOval(
                    particle.position.x - size / 2,
                    particle.position.y - size / 2,
                    size,
                    size
                );
            }
        }
    }
    
    /**
     * Draws the flying number with scale and fade effects.
     */
    private void drawFlyingNumber(Graphics2D g2d) {
        Point currentPos = getCurrentPosition();
        int alpha = getCurrentAlpha();
        float scale = getCurrentScale();
        
        // Create color with fade
        Color fadeColor = new Color(
            playerColor.getRed(),
            playerColor.getGreen(),
            playerColor.getBlue(),
            Math.max(0, Math.min(255, alpha))
        );
        
        // Save original transform
        AffineTransform originalTransform = g2d.getTransform();
        
        // Apply scale transform at current position
        g2d.translate(currentPos.x, currentPos.y);
        g2d.scale(scale, scale);
        g2d.translate(-currentPos.x, -currentPos.y);
        
        // Create font
        int fontSize = (int) (28 * scale);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);
        
        String text = "+" + scoreValue;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        
        int textX = currentPos.x - textWidth / 2;
        int textY = currentPos.y + textHeight / 2;
        
        // Draw text shadow for depth
        if (alpha > 100) {
            Color shadowColor = new Color(0, 0, 0, alpha / 3);
            g2d.setColor(shadowColor);
            g2d.drawString(text, textX + 2, textY + 2);
        }
        
        // Draw main text
        g2d.setColor(fadeColor);
        g2d.drawString(text, textX, textY);
        
        // Draw glow effect when alpha is high
        if (alpha > 150) {
            Color glowColor = new Color(
                playerColor.getRed(),
                playerColor.getGreen(),
                playerColor.getBlue(),
                (alpha - 150) / 2
            );
            g2d.setColor(glowColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawString(text, textX - 1, textY);
            g2d.drawString(text, textX + 1, textY);
            g2d.drawString(text, textX, textY - 1);
            g2d.drawString(text, textX, textY + 1);
        }
        
        // Restore transform
        g2d.setTransform(originalTransform);
    }
    
    /**
     * Cleans up resources and removes from parent.
     */
    private void cleanup() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.repaint();
        }
        trailParticles.clear();
    }
    
    /**
     * Stops the animation immediately.
     */
    public void stop() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        cleanup();
    }
    
    /**
     * Inner class representing a trail particle.
     */
    private static class TrailParticle {
        Point position;
        double age;
        
        TrailParticle(Point pos, double initialAge) {
            this.position = new Point(pos);
            this.age = initialAge;
        }
    }
}
