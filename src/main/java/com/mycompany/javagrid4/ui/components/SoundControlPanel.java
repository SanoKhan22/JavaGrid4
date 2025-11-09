package com.mycompany.javagrid4.ui.components;

import com.mycompany.javagrid4.audio.SoundManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;

/**
 * Sound controls panel with volume slider and mute button.
 * Provides user interface for adjusting game audio settings.
 * 
 * Features:
 * - Volume slider (0-100%)
 * - Mute/Unmute button with icon
 * - Compact horizontal layout
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class SoundControlPanel extends JPanel {
    
    private JSlider volumeSlider;
    private JButton muteButton;
    private boolean muted = false;
    private ImageIcon soundOnIcon;
    private ImageIcon soundOffIcon;
    
    /**
     * Creates a new sound control panel.
     */
    public SoundControlPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBackground(new Color(240, 240, 245));
        
        loadIcons();
        initComponents();
    }
    
    /**
     * Loads the custom icons from resources.
     */
    private void loadIcons() {
        try {
            // Load icons from resources
            Image soundImg = ImageIO.read(getClass().getResourceAsStream("/icons/sound.png"));
            Image muteImg = ImageIO.read(getClass().getResourceAsStream("/icons/mute.png"));
            
            // Scale to appropriate size (32x32 for better visibility)
            soundOnIcon = new ImageIcon(soundImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH));
            soundOffIcon = new ImageIcon(muteImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            System.err.println("Failed to load sound icons: " + e.getMessage());
            // Fallback to emoji if icons fail to load
            soundOnIcon = null;
            soundOffIcon = null;
        }
    }
    
    /**
     * Initializes all components.
     */
    private void initComponents() {
        // Mute/Unmute button with custom icon
        muteButton = new JButton();
        
        // Set icon or fallback to emoji
        if (soundOnIcon != null) {
            muteButton.setIcon(soundOnIcon);
        } else {
            muteButton.setText("🔊");
            muteButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }
        
        muteButton.setPreferredSize(new Dimension(45, 35));
        muteButton.setFocusPainted(false);
        muteButton.setContentAreaFilled(false);
        muteButton.setBorderPainted(false);
        muteButton.setToolTipText("Mute/Unmute (M)");
        muteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        muteButton.addActionListener(e -> toggleMute());
        
        // Volume label
        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Volume slider (0-100)
        volumeSlider = new JSlider(0, 100, 70);
        volumeSlider.setPreferredSize(new Dimension(120, 35));
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setBackground(new Color(240, 240, 245));
        volumeSlider.setToolTipText("Adjust volume");
        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) {
                updateVolume();
            }
        });
        
        // Add components
        add(muteButton);
        add(volumeLabel);
        add(volumeSlider);
    }
    
    /**
     * Toggles mute state.
     */
    private void toggleMute() {
        muted = !muted;
        
        if (muted) {
            SoundManager.getInstance().mute();
            
            // Update icon or text
            if (soundOffIcon != null) {
                muteButton.setIcon(soundOffIcon);
            } else {
                muteButton.setText("🔇");
            }
            
            muteButton.setToolTipText("Unmute (M)");
            volumeSlider.setEnabled(false);
        } else {
            SoundManager.getInstance().unmute();
            
            // Update icon or text
            if (soundOnIcon != null) {
                muteButton.setIcon(soundOnIcon);
            } else {
                muteButton.setText("🔊");
            }
            
            muteButton.setToolTipText("Mute (M)");
            volumeSlider.setEnabled(true);
            SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        }
    }
    
    /**
     * Updates the volume based on slider value.
     */
    private void updateVolume() {
        float volume = volumeSlider.getValue() / 100.0f;
        SoundManager.getInstance().setVolume(volume);
        
        // Play a test sound
        if (!muted) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_CLICK);
        }
    }
    
    /**
     * Sets up keyboard shortcut for mute (M key).
     */
    public void setupKeyboardShortcut() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "toggleMute");
        actionMap.put("toggleMute", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMute();
            }
        });
    }
}
