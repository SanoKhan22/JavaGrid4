package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Optimized pause overlay modal - clickable to resume, no blur for performance.
 * Displays a centered modal card over a semi-transparent backdrop when game is paused.
 * Click anywhere to resume the game.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class PauseOverlay extends JPanel {
    private static final int MODAL_WIDTH = 400;
    private static final int MODAL_HEIGHT = 280;
    private static final int CORNER_RADIUS = 20;
    private static final int ELEVATION = 24;
    
    private float backdropAlpha;
    private float modalScale;
    private Timer fadeInTimer;
    private boolean isVisible;
    private Runnable onResumeCallback;
    
    /**
     * Creates a new pause overlay modal.
     */
    public PauseOverlay() {
        this.backdropAlpha = 0.0f;
        this.modalScale = 0.85f;
        this.isVisible = false;
        
        setOpaque(false);
        setVisible(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Make clickable to resume
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isVisible && onResumeCallback != null) {
                    onResumeCallback.run();
                }
            }
        });
    }
    
    /**
     * Sets the callback to run when user clicks to resume.
     * @param callback Runnable to execute on resume click
     */
    public void setOnResumeCallback(Runnable callback) {
        this.onResumeCallback = callback;
    }
    
    /**
     * Shows the pause overlay with optimized fade-in animation.
     */
    public void showOverlay() {
        if (isVisible) return;
        
        isVisible = true;
        setVisible(true);
        
        // Animate fade in
        if (fadeInTimer != null && fadeInTimer.isRunning()) {
            fadeInTimer.stop();
        }
        
        fadeInTimer = new Timer(16, new ActionListener() {
            private int frameCount = 0;
            private static final int MAX_FRAMES = 8; // ~130ms
            
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                
                // Linear interpolation for performance
                backdropAlpha = Math.min(0.85f, frameCount / (float) MAX_FRAMES * 0.85f);
                modalScale = Math.min(1.0f, 0.85f + (frameCount / (float) MAX_FRAMES * 0.15f));
                
                if (frameCount >= MAX_FRAMES) {
                    backdropAlpha = 0.85f;
                    modalScale = 1.0f;
                    fadeInTimer.stop();
                }
                
                repaint();
            }
        });
        
        fadeInTimer.start();
    }
    
    /**
     * Hides the pause overlay with optimized fade-out animation.
     */
    public void hideOverlay() {
        if (!isVisible) return;
        
        // Animate fade out
        if (fadeInTimer != null && fadeInTimer.isRunning()) {
            fadeInTimer.stop();
        }
        
        fadeInTimer = new Timer(16, new ActionListener() {
            private int frameCount = 0;
            private static final int MAX_FRAMES = 6; // ~100ms
            
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                
                // Linear fade out for performance
                backdropAlpha = Math.max(0.0f, 0.85f - (frameCount / (float) MAX_FRAMES * 0.85f));
                modalScale = Math.max(0.85f, 1.0f - (frameCount / (float) MAX_FRAMES * 0.15f));
                
                // Stop and hide when animation complete
                if (frameCount >= MAX_FRAMES) {
                    backdropAlpha = 0.0f;
                    modalScale = 0.85f;
                    fadeInTimer.stop();
                    setVisible(false);
                    isVisible = false;
                }
                
                repaint();
            }
        });
        
        fadeInTimer.start();
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
        
        // Draw semi-transparent backdrop
        drawBackdrop(g2d, width, height);
        
        // Calculate modal position (centered)
        int modalX = (width - MODAL_WIDTH) / 2;
        int modalY = (height - MODAL_HEIGHT) / 2;
        
        // Apply scale transformation for animation
        if (modalScale != 1.0f) {
            int centerX = width / 2;
            int centerY = height / 2;
            g2d.translate(centerX, centerY);
            g2d.scale(modalScale, modalScale);
            g2d.translate(-centerX, -centerY);
        }
        
        // Draw modal card
        drawModalShadow(g2d, modalX, modalY);
        drawModalCard(g2d, modalX, modalY);
        drawModalContent(g2d, modalX, modalY);
        
        g2d.dispose();
    }
    
    /**
     * Draws the semi-transparent backdrop.
     */
    private void drawBackdrop(Graphics2D g2d, int width, int height) {
        int alpha = (int) (255 * backdropAlpha);
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, width, height);
    }
    
    /**
     * Draws the modal shadow for depth.
     */
    private void drawModalShadow(Graphics2D g2d, int x, int y) {
        // Multi-layer shadow for dramatic elevation
        for (int i = 10; i >= 1; i--) {
            int alpha = Math.min((ELEVATION * 8) / (i + 1), 100);
            int offset = (ELEVATION * i) / 10;
            
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fill(new RoundRectangle2D.Float(
                x + offset - ELEVATION, 
                y + offset, 
                MODAL_WIDTH + ELEVATION * 2, 
                MODAL_HEIGHT + ELEVATION * 2, 
                CORNER_RADIUS + offset, 
                CORNER_RADIUS + offset
            ));
        }
    }
    
    /**
     * Draws the modal card background.
     */
    private void drawModalCard(Graphics2D g2d, int x, int y) {
        // White background
        g2d.setColor(new Color(255, 255, 255));
        g2d.fill(new RoundRectangle2D.Float(
            x, y, 
            MODAL_WIDTH, MODAL_HEIGHT, 
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        // Subtle border
        g2d.setColor(new Color(220, 220, 230));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Float(
            x + 0.5f, y + 0.5f, 
            MODAL_WIDTH - 1, MODAL_HEIGHT - 1, 
            CORNER_RADIUS, CORNER_RADIUS
        ));
    }
    
    /**
     * Draws the content inside the modal.
     */
    private void drawModalContent(Graphics2D g2d, int x, int y) {
        int centerX = x + MODAL_WIDTH / 2;
        
        // Draw pause icon (large, centered)
        drawPauseIcon(g2d, centerX, y + 60);
        
        // Draw "PAUSED" text
        g2d.setFont(new Font("Arial", Font.BOLD, 42));
        g2d.setColor(new Color(60, 60, 80));
        String pausedText = "PAUSED";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(pausedText);
        g2d.drawString(pausedText, centerX - textWidth / 2, y + 150);
        
        // Draw instruction text - click or press P
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(new Color(120, 120, 140));
        String instructionText = "Click anywhere or press P to resume";
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(instructionText);
        g2d.drawString(instructionText, centerX - textWidth / 2, y + 190);
        
        // Draw decorative line
        g2d.setColor(new Color(200, 200, 220));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(centerX - 80, y + 210, centerX + 80, y + 210);
        
        // Draw additional hint
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(new Color(150, 150, 170));
        String hintText = "ESC for Menu";
        fm = g2d.getFontMetrics();
        textWidth = fm.stringWidth(hintText);
        g2d.drawString(hintText, centerX - textWidth / 2, y + 240);
    }
    
    /**
     * Draws a large pause icon (two vertical bars).
     */
    private void drawPauseIcon(Graphics2D g2d, int centerX, int centerY) {
        int iconSize = 50;
        int barWidth = 14;
        int barHeight = iconSize;
        int barSpacing = 16;
        
        // Left bar - shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fill(new RoundRectangle2D.Float(
            centerX - barSpacing / 2 - barWidth + 3, 
            centerY - barHeight / 2 + 3, 
            barWidth, barHeight, 
            6, 6
        ));
        
        // Right bar - shadow
        g2d.fill(new RoundRectangle2D.Float(
            centerX + barSpacing / 2 + 3, 
            centerY - barHeight / 2 + 3, 
            barWidth, barHeight, 
            6, 6
        ));
        
        // Left bar - with gradient
        GradientPaint gradient = new GradientPaint(
            centerX - barSpacing / 2 - barWidth, centerY - barHeight / 2,
            new Color(100, 150, 255),
            centerX - barSpacing / 2 - barWidth, centerY + barHeight / 2,
            new Color(80, 120, 220)
        );
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(
            centerX - barSpacing / 2 - barWidth, 
            centerY - barHeight / 2, 
            barWidth, barHeight, 
            6, 6
        ));
        
        // Right bar - with gradient
        gradient = new GradientPaint(
            centerX + barSpacing / 2, centerY - barHeight / 2,
            new Color(100, 150, 255),
            centerX + barSpacing / 2, centerY + barHeight / 2,
            new Color(80, 120, 220)
        );
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(
            centerX + barSpacing / 2, 
            centerY - barHeight / 2, 
            barWidth, barHeight, 
            6, 6
        ));
        
        // Highlights on bars
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fill(new RoundRectangle2D.Float(
            centerX - barSpacing / 2 - barWidth + 3, 
            centerY - barHeight / 2 + 3, 
            barWidth / 3, barHeight / 3, 
            3, 3
        ));
        g2d.fill(new RoundRectangle2D.Float(
            centerX + barSpacing / 2 + 3, 
            centerY - barHeight / 2 + 3, 
            barWidth / 3, barHeight / 3, 
            3, 3
        ));
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (fadeInTimer != null && fadeInTimer.isRunning()) {
            fadeInTimer.stop();
        }
    }
}
