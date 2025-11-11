package com.mycompany.javagrid4.ui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simple and efficient dialog replacement for JOptionPane.
 * Features a clean, modern design without blur effects for better performance.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class SimpleDialog extends JDialog {
    
    private int result = JOptionPane.CANCEL_OPTION;
    
    /**
     * Private constructor - use static methods to show dialogs.
     */
    private SimpleDialog(Frame parent, String title, String message, 
                        String primaryText, String secondaryText) {
        super(parent, title, true);
        setUndecorated(true);
        setLayout(new BorderLayout());
        
        createUI(message, primaryText, secondaryText);
        
        pack();
        setLocationRelativeTo(parent);
        
        // ESC key to close
        getRootPane().registerKeyboardAction(
            e -> {
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    /**
     * Creates the dialog UI.
     */
    private void createUI(String message, String primaryText, String secondaryText) {
        // Main panel with white background and rounded appearance
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // White background with rounded corners
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Subtle border
                g2d.setColor(new Color(220, 220, 220));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Message label
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; width: 320px;'>" + 
                                        message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        messageLabel.setForeground(new Color(60, 60, 60));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);
        
        if (secondaryText != null) {
            // Secondary button (Cancel/No)
            JButton secondaryButton = createButton(secondaryText, false);
            secondaryButton.addActionListener(e -> {
                result = JOptionPane.CANCEL_OPTION;
                dispose();
            });
            buttonPanel.add(secondaryButton);
        }
        
        if (primaryText != null) {
            // Primary button (OK/Yes)
            JButton primaryButton = createButton(primaryText, true);
            primaryButton.addActionListener(e -> {
                result = JOptionPane.OK_OPTION;
                dispose();
            });
            buttonPanel.add(primaryButton);
            getRootPane().setDefaultButton(primaryButton);
        }
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add shadow effect
        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setOpaque(false);
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        shadowPanel.add(mainPanel, BorderLayout.CENTER);
        
        add(shadowPanel, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(0, 0, 0, 0));
    }
    
    /**
     * Creates a styled button.
     */
    private JButton createButton(String text, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Button background
                if (isPrimary) {
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(51, 103, 214)); // Darker blue when pressed
                    } else if (getModel().isRollover()) {
                        g2d.setColor(new Color(66, 133, 244, 230)); // Slightly transparent on hover
                    } else {
                        g2d.setColor(new Color(66, 133, 244)); // Google Blue
                    }
                } else {
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(220, 220, 220));
                    } else if (getModel().isRollover()) {
                        g2d.setColor(new Color(245, 245, 245));
                    } else {
                        g2d.setColor(new Color(240, 240, 240));
                    }
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(isPrimary ? Color.WHITE : new Color(60, 60, 60));
        button.setPreferredSize(new Dimension(100, 36));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Shows a confirmation dialog with Yes/No options.
     * @param parent Parent component
     * @param message Dialog message
     * @param title Dialog title
     * @param yesText Text for Yes button
     * @param noText Text for No button
     * @return JOptionPane.OK_OPTION if Yes, JOptionPane.CANCEL_OPTION if No
     */
    public static int showConfirmDialog(Component parent, String message, String title, 
                                       String yesText, String noText) {
        Frame frame = getParentFrame(parent);
        SimpleDialog dialog = new SimpleDialog(frame, title, message, yesText, noText);
        dialog.setVisible(true);
        return dialog.result;
    }
    
    /**
     * Shows a message dialog with OK button.
     * @param parent Parent component
     * @param message Dialog message
     * @param title Dialog title
     */
    public static void showMessageDialog(Component parent, String message, String title) {
        Frame frame = getParentFrame(parent);
        SimpleDialog dialog = new SimpleDialog(frame, title, message, "OK", null);
        dialog.setVisible(true);
    }
    
    /**
     * Gets the parent Frame for the dialog.
     */
    private static Frame getParentFrame(Component parent) {
        while (parent != null) {
            if (parent instanceof Frame) {
                return (Frame) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
