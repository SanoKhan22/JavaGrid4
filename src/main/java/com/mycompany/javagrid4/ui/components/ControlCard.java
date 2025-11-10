package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Material Design control button card for game actions.
 * Features elevated card design with icon, label, shadows, and hover effects.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class ControlCard extends JPanel {
    private static final int CARD_SIZE = 90;
    private static final int CORNER_RADIUS = 12;
    private static final int ELEVATION_NORMAL = 2;
    private static final int ELEVATION_HOVER = 6;
    private static final int ELEVATION_DISABLED = 1;
    
    private String label;
    private String icon;
    private Color accentColor;
    private boolean isHovered;
    private boolean isPressed;
    private Runnable action;
    
    /**
     * Creates a new control card.
     * 
     * @param label The label text (e.g., "Restart")
     * @param icon The icon text (e.g., "↻", "⟲", "?")
     * @param accentColor The accent color for hover state
     * @param action The action to execute on click
     */
    public ControlCard(String label, String icon, Color accentColor, Runnable action) {
        this.label = label;
        this.icon = icon;
        this.accentColor = accentColor;
        this.action = action;
        this.isHovered = false;
        this.isPressed = false;
        
        setOpaque(false);
        setPreferredSize(new Dimension(CARD_SIZE, CARD_SIZE));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListeners();
    }
    
    /**
     * Adds mouse listeners for hover and click effects.
     */
    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = false;
                    repaint();
                    
                    // Execute action if released inside component
                    if (contains(e.getPoint()) && action != null) {
                        action.run();
                    }
                }
            }
        });
    }
    
    /**
     * Updates the icon displayed on this card.
     * 
     * @param icon The new icon text to display
     */
    public void setIcon(String icon) {
        this.icon = icon;
        repaint();
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
        
        // Draw shadow
        drawShadow(g2d, width, height);
        
        // Draw card background
        drawCardBackground(g2d, width, height);
        
        // Draw icon
        drawIcon(g2d, width, height);
        
        // Draw label
        drawLabel(g2d, width, height);
        
        g2d.dispose();
    }
    
    /**
     * Draws the card shadow for depth.
     */
    private void drawShadow(Graphics2D g2d, int width, int height) {
        if (!isEnabled()) return;
        
        int elevation = isPressed ? ELEVATION_DISABLED : 
                       (isHovered ? ELEVATION_HOVER : ELEVATION_NORMAL);
        
        // Multi-layer shadow for depth
        for (int i = 5; i >= 1; i--) {
            int alpha = Math.min((elevation * 12) / (i + 1), 70);
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
        // Background color
        Color bgColor;
        if (!isEnabled()) {
            bgColor = new Color(240, 240, 245);
        } else if (isPressed) {
            bgColor = new Color(245, 245, 250);
        } else if (isHovered) {
            // Subtle tint of accent color
            bgColor = new Color(
                Math.min(255, 250 + accentColor.getRed() / 30),
                Math.min(255, 250 + accentColor.getGreen() / 30),
                Math.min(255, 250 + accentColor.getBlue() / 30)
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
        
        // Border
        Color borderColor = isEnabled() ? 
            (isHovered ? accentColor : new Color(220, 220, 230)) :
            new Color(230, 230, 240);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(isHovered ? 2 : 1));
        g2d.draw(new RoundRectangle2D.Float(
            0.5f, 0.5f,
            width - 1, 
            height - 1, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws the icon at the top of the card.
     */
    private void drawIcon(Graphics2D g2d, int width, int height) {
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        
        Color iconColor = isEnabled() ? 
            (isHovered ? accentColor : new Color(80, 80, 100)) :
            new Color(180, 180, 190);
        g2d.setColor(iconColor);
        
        FontMetrics fm = g2d.getFontMetrics();
        int iconWidth = fm.stringWidth(icon);
        int iconX = (width - iconWidth) / 2;
        int iconY = (height / 2) - 5;
        
        g2d.drawString(icon, iconX, iconY);
    }
    
    /**
     * Draws the label at the bottom of the card.
     */
    private void drawLabel(Graphics2D g2d, int width, int height) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        
        Color labelColor = isEnabled() ? 
            new Color(100, 100, 120) :
            new Color(180, 180, 190);
        g2d.setColor(labelColor);
        
        FontMetrics fm = g2d.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelX = (width - labelWidth) / 2;
        int labelY = height - 15;
        
        g2d.drawString(label, labelX, labelY);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        repaint();
    }
}
