package com.mycompany.javagrid4.ui.components;

import com.mycompany.javagrid4.Player;
import com.mycompany.javagrid4.ui.effects.CellClaimAnimation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom grid cell component with advanced Graphics2D rendering.
 * Features:
 * - Gradient backgrounds
 * - Smooth rounded corners
 * - Hover glow effects
 * - Shadow and depth
 * - Animated transitions
 * - Claim animations (pop, pulse, burst)
 * 
 * Replaces JButton-based GridCell for superior visual quality.
 * 
 * @author JavaGrid4 Team
 * @version 1.1
 */
public class CustomGridCell extends JComponent {
    private static final int CELL_SIZE = 80;
    private static final int FONT_SIZE = 28;
    private static final int CORNER_RADIUS = 15;
    private static final int SHADOW_OFFSET = 3;
    
    // Colors
    private static final Color NEUTRAL_COLOR = new Color(220, 220, 225);
    private static final Color HOVER_COLOR = new Color(240, 240, 245);
    private static final Color BORDER_COLOR = new Color(180, 180, 190);
    
    private final int row;
    private final int column;
    private int cellValue;
    private Player owner;
    
    // Custom player colors
    private Color player1Color;
    private Color player2Color;
    
    // State
    private boolean isHovered;
    private boolean isPressed;
    
    // Animation properties
    private double animationScale = 1.0;
    private float glowIntensity = 0.0f;
    private CellClaimAnimation.BurstParticle[] burstParticles = null;
    private boolean isAnimating = false;
    
    /**
     * Creates a CustomGridCell with custom player colors.
     * @param row The row index in the grid
     * @param column The column index in the grid
     * @param player1Color Color for Player 1
     * @param player2Color Color for Player 2
     */
    public CustomGridCell(int row, int column, Color player1Color, Color player2Color) {
        this.row = row;
        this.column = column;
        this.cellValue = 0;
        this.owner = null;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.isHovered = false;
        this.isPressed = false;
        
        initializeComponent();
    }
    
    /**
     * Initializes component properties and listeners.
     */
    private void initializeComponent() {
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setOpaque(false); // For custom painting
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add mouse listeners for hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing for smooth graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Apply scale transformation if animating
        if (animationScale != 1.0) {
            int centerX = width / 2;
            int centerY = height / 2;
            g2d.translate(centerX, centerY);
            g2d.scale(animationScale, animationScale);
            g2d.translate(-centerX, -centerY);
        }
        
        // Draw shadow for depth
        drawShadow(g2d, width, height);
        
        // Draw cell background with gradient
        drawBackground(g2d, width, height);
        
        // Draw glow effect when hovered or animated
        if ((isHovered && owner == null) || glowIntensity > 0) {
            drawHoverGlow(g2d, width, height);
        }
        
        // Draw animated glow for claim animation
        if (glowIntensity > 0) {
            drawClaimGlow(g2d, width, height);
        }
        
        // Draw border
        drawBorder(g2d, width, height);
        
        // Draw cell value
        drawValue(g2d, width, height);
        
        // Draw burst particles if present
        if (burstParticles != null) {
            drawBurstParticles(g2d, width, height);
        }
        
        g2d.dispose();
    }
    
    /**
     * Draws subtle shadow for 3D depth effect.
     */
    private void drawShadow(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fill(new RoundRectangle2D.Float(
            SHADOW_OFFSET, 
            SHADOW_OFFSET, 
            width - SHADOW_OFFSET, 
            height - SHADOW_OFFSET, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws cell background with gradient based on owner.
     */
    private void drawBackground(Graphics2D g2d, int width, int height) {
        Color baseColor = getBaseColor();
        
        // Create gradient from lighter to darker
        Color topColor = brighten(baseColor, 0.2f);
        Color bottomColor = darken(baseColor, 0.1f);
        
        // Apply pressed effect
        if (isPressed) {
            topColor = darken(topColor, 0.15f);
            bottomColor = darken(bottomColor, 0.15f);
        }
        
        GradientPaint gradient = new GradientPaint(
            0, 0, topColor,
            0, height, bottomColor
        );
        
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Float(
            0, 0, 
            width - SHADOW_OFFSET, 
            height - SHADOW_OFFSET, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws glow effect on hover.
     */
    private void drawHoverGlow(Graphics2D g2d, int width, int height) {
        // Outer glow
        g2d.setColor(new Color(100, 150, 255, 40));
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(new RoundRectangle2D.Float(
            2, 2, 
            width - SHADOW_OFFSET - 4, 
            height - SHADOW_OFFSET - 4, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws cell border.
     */
    private void drawBorder(Graphics2D g2d, int width, int height) {
        g2d.setColor(BORDER_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(new RoundRectangle2D.Float(
            1, 1, 
            width - SHADOW_OFFSET - 2, 
            height - SHADOW_OFFSET - 2, 
            CORNER_RADIUS, 
            CORNER_RADIUS
        ));
    }
    
    /**
     * Draws the cell value (0-4) centered.
     */
    private void drawValue(Graphics2D g2d, int width, int height) {
        String text = String.valueOf(cellValue);
        g2d.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        
        // Calculate centered position
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int x = (width - SHADOW_OFFSET - textWidth) / 2;
        int y = (height - SHADOW_OFFSET + textHeight) / 2 - 2;
        
        // Draw text shadow for depth
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawString(text, x + 1, y + 1);
        
        // Draw main text
        g2d.setColor(getTextColor());
        g2d.drawString(text, x, y);
    }
    
    /**
     * Gets the base color based on cell owner.
     */
    private Color getBaseColor() {
        if (owner == null) {
            return isHovered ? HOVER_COLOR : NEUTRAL_COLOR;
        } else if (owner == Player.PLAYER_ONE) {
            return player1Color;
        } else {
            return player2Color;
        }
    }
    
    /**
     * Gets appropriate text color for contrast.
     */
    private Color getTextColor() {
        if (owner == null) {
            return new Color(60, 60, 70);
        } else {
            // Use white text on dark backgrounds, dark on light
            Color baseColor = getBaseColor();
            int brightness = (baseColor.getRed() + baseColor.getGreen() + baseColor.getBlue()) / 3;
            return brightness > 150 ? new Color(40, 40, 50) : Color.WHITE;
        }
    }
    
    /**
     * Brightens a color by a factor.
     */
    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() * (1 + factor)));
        int g = Math.min(255, (int) (color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int) (color.getBlue() * (1 + factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Darkens a color by a factor.
     */
    private Color darken(Color color, float factor) {
        int r = Math.max(0, (int) (color.getRed() * (1 - factor)));
        int g = Math.max(0, (int) (color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int) (color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Draws animated glow effect for cell claim.
     */
    private void drawClaimGlow(Graphics2D g2d, int width, int height) {
        if (glowIntensity <= 0) return;
        
        // Get player color for glow
        Color glowColor = getBaseColor();
        int alpha = (int) (150 * glowIntensity);
        Color glowWithAlpha = new Color(
            glowColor.getRed(), 
            glowColor.getGreen(), 
            glowColor.getBlue(), 
            Math.min(255, alpha)
        );
        
        // Draw multiple glow layers for intensity
        for (int i = 0; i < 3; i++) {
            int offset = (int) (5 + i * 3 * glowIntensity);
            g2d.setColor(new Color(
                glowWithAlpha.getRed(),
                glowWithAlpha.getGreen(),
                glowWithAlpha.getBlue(),
                alpha / (i + 2)
            ));
            g2d.setStroke(new BasicStroke(3 - i));
            g2d.draw(new RoundRectangle2D.Float(
                -offset, -offset, 
                width - SHADOW_OFFSET + offset * 2, 
                height - SHADOW_OFFSET + offset * 2, 
                CORNER_RADIUS + offset, 
                CORNER_RADIUS + offset
            ));
        }
    }
    
    /**
     * Draws burst particles for cell claim animation.
     */
    private void drawBurstParticles(Graphics2D g2d, int width, int height) {
        if (burstParticles == null) return;
        
        int centerX = (width - SHADOW_OFFSET) / 2;
        int centerY = (height - SHADOW_OFFSET) / 2;
        
        Color particleColor = getBaseColor();
        
        for (CellClaimAnimation.BurstParticle particle : burstParticles) {
            double x = centerX + particle.getX();
            double y = centerY + particle.getY();
            double size = particle.getSize();
            float alpha = particle.getAlpha();
            
            // Draw particle as small circle with fade
            int alphaInt = (int) (255 * alpha);
            Color particleWithAlpha = new Color(
                particleColor.getRed(),
                particleColor.getGreen(),
                particleColor.getBlue(),
                Math.max(0, Math.min(255, alphaInt))
            );
            
            g2d.setColor(particleWithAlpha);
            g2d.fill(new Ellipse2D.Double(
                x - size / 2,
                y - size / 2,
                size,
                size
            ));
            
            // Add glow around particle
            Color glowColor = new Color(
                particleColor.getRed(),
                particleColor.getGreen(),
                particleColor.getBlue(),
                Math.max(0, Math.min(100, alphaInt / 2))
            );
            g2d.setColor(glowColor);
            g2d.fill(new Ellipse2D.Double(
                x - size,
                y - size,
                size * 2,
                size * 2
            ));
        }
    }
    
    // ===== Animation Control Methods =====
    
    /**
     * Sets the animation scale for pop effect.
     * @param scale Scale factor (1.0 = normal)
     */
    public void setAnimationScale(double scale) {
        this.animationScale = scale;
    }
    
    /**
     * Sets the glow intensity for pulse effect.
     * @param intensity Glow intensity (0.0 to 1.0)
     */
    public void setGlowIntensity(float intensity) {
        this.glowIntensity = Math.max(0.0f, Math.min(1.0f, intensity));
    }
    
    /**
     * Sets burst particles for burst animation.
     * @param particles Array of particles, or null to clear
     */
    public void setBurstParticles(CellClaimAnimation.BurstParticle[] particles) {
        this.burstParticles = particles;
    }
    
    /**
     * Plays the claim animation when cell reaches value 4.
     * Uses combo animation (pop + pulse) by default.
     */
    public void playClaimAnimation() {
        if (isAnimating) return;
        isAnimating = true;
        
        CellClaimAnimation animation = new CellClaimAnimation(this);
        animation.setAnimationType(CellClaimAnimation.AnimationType.COMBO);
        animation.play(() -> {
            isAnimating = false;
        });
    }
    
    /**
     * Plays claim animation with specific type.
     * @param type The animation type to play
     */
    public void playClaimAnimation(CellClaimAnimation.AnimationType type) {
        if (isAnimating) return;
        isAnimating = true;
        
        CellClaimAnimation animation = new CellClaimAnimation(this);
        animation.setAnimationType(type);
        animation.play(() -> {
            isAnimating = false;
        });
    }
    
    /**
     * Checks if cell is currently animating.
     * @return true if animation is in progress
     */
    public boolean isAnimating() {
        return isAnimating;
    }
    
    // ===== Getters and Setters =====
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    public int getCellValue() {
        return cellValue;
    }
    
    public void setCellValue(int value) {
        this.cellValue = value;
        repaint();
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void setOwner(Player owner) {
        this.owner = owner;
        repaint();
    }
    
    public void resetCell() {
        this.cellValue = 0;
        this.owner = null;
        repaint();
    }
}
