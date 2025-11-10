package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Material Design container card for the game grid.
 * Provides elevated background with shadows and rounded corners to group the grid visually.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class GridContainer extends JPanel {
    private static final int CORNER_RADIUS = 16;
    private static final int ELEVATION = 8;
    private static final int PADDING = 20;
    
    /**
     * Creates a new grid container with Material Design styling.
     */
    public GridContainer() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
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
        
        // Draw multi-layer shadow for depth
        drawShadow(g2d, width, height);
        
        // Draw container card background
        drawCardBackground(g2d, width, height);
        
        // Draw subtle top highlight
        drawHighlight(g2d, width, height);
        
        g2d.dispose();
    }
    
    /**
     * Draws multi-layer shadow for elevation effect.
     */
    private void drawShadow(Graphics2D g2d, int width, int height) {
        // Multi-layer shadow (7 layers for smooth depth)
        for (int i = 7; i >= 1; i--) {
            float alpha = (0.12f / 7) * i;
            int offset = (ELEVATION * i) / 7;
            
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fill(new RoundRectangle2D.Float(
                offset, 
                offset + (i / 2), // Slightly offset down
                width - (offset * 2), 
                height - (offset * 2), 
                CORNER_RADIUS + (i / 2), 
                CORNER_RADIUS + (i / 2)
            ));
        }
        
        // Core shadow - darker and more defined
        int shadowAlpha = Math.min(ELEVATION * 12, 100);
        g2d.setColor(new Color(0, 0, 0, shadowAlpha));
        g2d.fill(new RoundRectangle2D.Float(
            ELEVATION / 2, 
            ELEVATION, 
            width - ELEVATION, 
            height - ELEVATION, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws the solid card background.
     */
    private void drawCardBackground(Graphics2D g2d, int width, int height) {
        // White background with very subtle warm tint
        g2d.setColor(new Color(254, 254, 255));
        g2d.fill(new RoundRectangle2D.Float(
            0, 0,
            width, 
            height, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
        
        // Subtle border
        g2d.setColor(new Color(215, 220, 235));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(new RoundRectangle2D.Float(
            0.75f, 0.75f,
            width - 1.5f, 
            height - 1.5f, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws subtle highlight at top for 3D effect.
     */
    private void drawHighlight(Graphics2D g2d, int width, int height) {
        // Top highlight gradient
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 255, 255, 80),
            0, 40, new Color(255, 255, 255, 0)
        );
        
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(
            1, 1,
            width - 2, 
            40, 
            CORNER_RADIUS - 1, 
            CORNER_RADIUS - 1
        ));
    }
    
    /**
     * Gets the recommended padding for content inside the container.
     * 
     * @return The padding value
     */
    public static int getContentPadding() {
        return PADDING;
    }
}
