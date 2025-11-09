package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom color picker button with visual preview.
 * Displays current color as a filled circle and opens a color selection dialog on click.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class ColorPickerButton extends JPanel {
    
    private Color currentColor;
    private final List<ColorChangeListener> listeners = new ArrayList<>();
    
    // Preset colors for quick selection
    private static final Color[] PRESET_COLORS = {
        new Color(255, 100, 100),  // Red
        new Color(255, 150, 100),  // Orange
        new Color(255, 220, 100),  // Yellow
        new Color(100, 220, 100),  // Green
        new Color(100, 150, 255),  // Blue
        new Color(150, 100, 255),  // Purple
        new Color(255, 100, 200),  // Pink
        new Color(100, 200, 200),  // Cyan
    };
    
    /**
     * Listener interface for color change events.
     */
    public interface ColorChangeListener {
        void colorChanged(Color newColor);
    }
    
    /**
     * Creates a color picker button with default color.
     * @param initialColor Initial color to display
     */
    public ColorPickerButton(Color initialColor) {
        this.currentColor = initialColor;
        setPreferredSize(new Dimension(70, 70));
        setMinimumSize(new Dimension(70, 70));
        setMaximumSize(new Dimension(70, 70));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Click to change color");
        
        // Add click listener to open color picker
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showColorPickerDialog();
            }
        });
    }
    
    /**
     * Gets the current selected color.
     * @return Current color
     */
    public Color getSelectedColor() {
        return currentColor;
    }
    
    /**
     * Sets the current color and repaints.
     * @param color New color
     */
    public void setSelectedColor(Color color) {
        if (color != null && !color.equals(currentColor)) {
            this.currentColor = color;
            repaint();
            notifyListeners(color);
        }
    }
    
    /**
     * Adds a color change listener.
     * @param listener Listener to add
     */
    public void addColorChangeListener(ColorChangeListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Notifies all listeners of color change.
     * @param newColor The new color
     */
    private void notifyListeners(Color newColor) {
        for (ColorChangeListener listener : listeners) {
            listener.colorChanged(newColor);
        }
    }
    
    /**
     * Shows a dialog with preset colors and custom color option.
     */
    private void showColorPickerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Choose Color", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Panel for preset colors
        JPanel presetPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        presetPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        for (Color presetColor : PRESET_COLORS) {
            JButton colorButton = new JButton();
            colorButton.setBackground(presetColor);
            colorButton.setPreferredSize(new Dimension(70, 70));
            colorButton.setOpaque(true);
            colorButton.setBorderPainted(true);
            colorButton.setContentAreaFilled(true);
            colorButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
            colorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            colorButton.addActionListener(e -> {
                setSelectedColor(presetColor);
                dialog.dispose();
            });
            presetPanel.add(colorButton);
        }
        
        // Custom color button
        JButton customButton = new JButton("Custom Color...");
        customButton.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(dialog, 
                "Choose Custom Color", currentColor);
            if (chosen != null) {
                setSelectedColor(chosen);
                dialog.dispose();
            }
        });
        
        dialog.add(presetPanel, BorderLayout.CENTER);
        dialog.add(customButton, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        
        int size = Math.min(getWidth(), getHeight()) - 8;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;
        
        // Draw outer glow/shadow
        g2d.setColor(new Color(0, 0, 0, 30));
        for (int i = 3; i > 0; i--) {
            g2d.fillOval(x - i, y - i, size + i * 2, size + i * 2);
        }
        
        // Draw white background ring
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 4, y - 4, size + 8, size + 8);
        
        // Draw main color circle with gradient
        GradientPaint gradient = new GradientPaint(
            x, y, currentColor.brighter(),
            x + size, y + size, currentColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x, y, size, size);
        
        // Draw inner highlight for 3D effect
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillOval(x + size/4, y + size/4, size/3, size/3);
        
        // Draw thick border
        g2d.setColor(new Color(80, 80, 80));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x, y, size, size);
        
        // Draw outer border
        g2d.setColor(new Color(120, 120, 120));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - 4, y - 4, size + 8, size + 8);
        
        g2d.dispose();
    }
}
