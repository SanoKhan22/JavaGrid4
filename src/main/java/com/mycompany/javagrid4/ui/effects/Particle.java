package com.mycompany.javagrid4.ui.effects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Represents a single confetti particle with physics simulation.
 * Each particle has position, velocity, rotation, and color.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class Particle {
    
    // Position
    private double x;
    private double y;
    
    // Velocity
    private double velocityX;
    private double velocityY;
    
    // Rotation
    private double rotation;
    private double rotationSpeed;
    
    // Appearance
    private final Color color;
    private final int size;
    private final Shape shape;
    
    // Physics
    private static final double GRAVITY = 0.5;
    private static final double AIR_RESISTANCE = 0.99;
    
    // Lifetime
    private double alpha = 1.0;
    private final double fadeRate;
    
    /**
     * Particle shapes for variety.
     */
    public enum Shape {
        RECTANGLE,
        CIRCLE,
        TRIANGLE,
        STAR
    }
    
    /**
     * Creates a new particle with specified properties.
     * 
     * @param x Starting X position
     * @param y Starting Y position
     * @param velocityX Horizontal velocity
     * @param velocityY Vertical velocity
     * @param color Particle color
     * @param size Particle size (width/height)
     * @param shape Particle shape
     * @param rotationSpeed Rotation speed in radians per frame
     * @param fadeRate Alpha reduction per frame
     */
    public Particle(double x, double y, double velocityX, double velocityY, 
                   Color color, int size, Shape shape, double rotationSpeed, double fadeRate) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.size = size;
        this.shape = shape;
        this.rotation = Math.random() * Math.PI * 2; // Random initial rotation
        this.rotationSpeed = rotationSpeed;
        this.fadeRate = fadeRate;
    }
    
    /**
     * Updates the particle's position and physics.
     */
    public void update() {
        // Apply gravity
        velocityY += GRAVITY;
        
        // Apply air resistance
        velocityX *= AIR_RESISTANCE;
        velocityY *= AIR_RESISTANCE;
        
        // Update position
        x += velocityX;
        y += velocityY;
        
        // Update rotation
        rotation += rotationSpeed;
        
        // Fade out
        alpha -= fadeRate;
        if (alpha < 0) alpha = 0;
    }
    
    /**
     * Draws the particle.
     * 
     * @param g2d Graphics2D context
     */
    public void draw(Graphics2D g2d) {
        // Save original transform
        AffineTransform originalTransform = g2d.getTransform();
        
        // Create transparent color
        Color transparentColor = new Color(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            (int) (alpha * 255)
        );
        g2d.setColor(transparentColor);
        
        // Translate and rotate
        g2d.translate(x, y);
        g2d.rotate(rotation);
        
        // Draw based on shape
        int halfSize = size / 2;
        switch (shape) {
            case RECTANGLE:
                g2d.fillRect(-halfSize, -halfSize, size, size);
                break;
                
            case CIRCLE:
                g2d.fillOval(-halfSize, -halfSize, size, size);
                break;
                
            case TRIANGLE:
                int[] xPoints = {0, halfSize, -halfSize};
                int[] yPoints = {-halfSize, halfSize, halfSize};
                g2d.fillPolygon(xPoints, yPoints, 3);
                break;
                
            case STAR:
                drawStar(g2d, 0, 0, halfSize, halfSize / 2, 5);
                break;
        }
        
        // Restore original transform
        g2d.setTransform(originalTransform);
    }
    
    /**
     * Draws a star shape.
     * 
     * @param g2d Graphics2D context
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     * @param outerRadius Outer radius
     * @param innerRadius Inner radius
     * @param points Number of points
     */
    private void drawStar(Graphics2D g2d, int centerX, int centerY, 
                         int outerRadius, int innerRadius, int points) {
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        
        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI / 2 + (i * Math.PI / points);
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            
            xPoints[i] = centerX + (int) (Math.cos(angle) * radius);
            yPoints[i] = centerY + (int) (Math.sin(angle) * radius);
        }
        
        g2d.fillPolygon(xPoints, yPoints, points * 2);
    }
    
    /**
     * Checks if the particle is still alive (visible).
     * 
     * @return true if alpha > 0, false otherwise
     */
    public boolean isAlive() {
        return alpha > 0;
    }
    
    /**
     * Checks if the particle is off-screen.
     * 
     * @param width Screen width
     * @param height Screen height
     * @return true if particle is off-screen
     */
    public boolean isOffScreen(int width, int height) {
        return y > height + size || x < -size || x > width + size;
    }
}
