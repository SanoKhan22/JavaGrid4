package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Visual card component for board size selection.
 * Shows a mini grid preview of the actual board size.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class BoardSizeCard extends JPanel {
    
    private final int size;
    private boolean selected;
    private boolean hovered;
    
    private static final Color BORDER_DEFAULT = new Color(180, 180, 200);
    private static final Color BORDER_SELECTED = new Color(70, 120, 200);
    private static final Color BORDER_HOVER = new Color(120, 150, 220);
    private static final Color BG_DEFAULT = Color.WHITE;
    private static final Color BG_SELECTED = new Color(230, 240, 255);
    private static final Color BG_HOVER = new Color(245, 248, 255);
    
    /**
     * Creates a board size card.
     * @param size Board size (3, 5, or 7)
     */
    public BoardSizeCard(int size) {
        this.size = size;
        this.selected = false;
        this.hovered = false;
        
        setPreferredSize(new Dimension(140, 180));
        setMinimumSize(new Dimension(140, 180));
        setMaximumSize(new Dimension(140, 180));
        setOpaque(true);
        setBackground(BG_DEFAULT);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add mouse listeners for hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    
    /**
     * Sets whether this card is selected.
     * @param selected true if selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
    
    /**
     * Checks if this card is selected.
     * @return true if selected
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Gets the board size for this card.
     * @return Board size (3, 5, or 7)
     */
    public int getBoardSize() {
        return size;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Determine background and border colors based on state
        Color bgColor = selected ? BG_SELECTED : (hovered ? BG_HOVER : BG_DEFAULT);
        Color borderColor = selected ? BORDER_SELECTED : (hovered ? BORDER_HOVER : BORDER_DEFAULT);
        int borderWidth = selected ? 4 : 2;
        
        // Fill background with rounded corners
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, width, height, 15, 15);
        
        // Draw border
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRoundRect(borderWidth/2, borderWidth/2, 
            width - borderWidth, height - borderWidth, 15, 15);
        
        // Draw grid preview
        drawGridPreview(g2d, width, height);
        
        // Draw size label
        drawSizeLabel(g2d, width, height);
        
        // Draw selected indicator
        if (selected) {
            drawSelectedIndicator(g2d, width);
        }
        
        g2d.dispose();
    }
    
    /**
     * Draws the mini grid preview.
     */
    private void drawGridPreview(Graphics2D g2d, int width, int height) {
        int gridSize = 90; // Total size of grid preview
        int cellSize = gridSize / size;
        int startX = (width - gridSize) / 2;
        int startY = 30;
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = startX + col * cellSize;
                int y = startY + row * cellSize;
                
                // Draw cell background
                if (selected) {
                    g2d.setColor(new Color(100, 150, 255, 100));
                } else {
                    g2d.setColor(new Color(200, 200, 220));
                }
                g2d.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
                
                // Draw cell border
                g2d.setColor(new Color(150, 150, 170));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRect(x, y, cellSize, cellSize);
            }
        }
    }
    
    /**
     * Draws the size label.
     */
    private void drawSizeLabel(Graphics2D g2d, int width, int height) {
        String label = size + "×" + size;
        String subLabel = getSizeDescription();
        
        // Main label
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelX = (width - labelWidth) / 2;
        int labelY = height - 45;
        
        g2d.setColor(selected ? BORDER_SELECTED : new Color(60, 60, 80));
        g2d.drawString(label, labelX, labelY);
        
        // Sub label
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        fm = g2d.getFontMetrics();
        int subLabelWidth = fm.stringWidth(subLabel);
        int subLabelX = (width - subLabelWidth) / 2;
        int subLabelY = height - 20;
        
        g2d.setColor(new Color(100, 100, 120));
        g2d.drawString(subLabel, subLabelX, subLabelY);
    }
    
    /**
     * Gets the description for this size.
     */
    private String getSizeDescription() {
        switch (size) {
            case 3: return "Quick";
            case 5: return "Classic";
            case 7: return "Expert";
            default: return "";
        }
    }
    
    /**
     * Draws the selected indicator (checkmark).
     */
    private void drawSelectedIndicator(Graphics2D g2d, int width) {
        int checkSize = 24;
        int checkX = width - checkSize - 8;
        int checkY = 8;
        
        // Draw circle background
        g2d.setColor(BORDER_SELECTED);
        g2d.fillOval(checkX, checkY, checkSize, checkSize);
        
        // Draw checkmark
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[] xPoints = {checkX + 6, checkX + 10, checkX + 18};
        int[] yPoints = {checkY + 12, checkY + 16, checkY + 8};
        g2d.drawPolyline(xPoints, yPoints, 3);
    }
}
