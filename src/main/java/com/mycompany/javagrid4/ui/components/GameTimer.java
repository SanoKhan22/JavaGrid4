package com.mycompany.javagrid4.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Game timer component that displays elapsed game time.
 * Shows time in MM:SS format with a clock icon.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class GameTimer extends JPanel {
    
    private JLabel timerLabel;
    private Timer updateTimer;
    private long startTime;
    private long pausedTime;
    private boolean isPaused;
    private int elapsedSeconds;
    
    /**
     * Creates a new game timer.
     */
    public GameTimer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        setOpaque(false);
        
        initComponents();
    }
    
    /**
     * Initializes timer components.
     */
    private void initComponents() {
        // Clock icon label
        JLabel iconLabel = new JLabel("⏱️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        // Timer display label
        timerLabel = new JLabel("00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(new Color(80, 80, 100));
        
        add(iconLabel);
        add(timerLabel);
    }
    
    /**
     * Starts the timer.
     */
    public void start() {
        if (updateTimer != null && updateTimer.isRunning()) {
            return; // Already running
        }
        
        startTime = System.currentTimeMillis();
        isPaused = false;
        elapsedSeconds = 0;
        
        updateTimer = new Timer(1000, e -> updateDisplay());
        updateTimer.start();
        updateDisplay();
    }
    
    /**
     * Pauses the timer.
     */
    public void pause() {
        if (updateTimer != null && updateTimer.isRunning()) {
            updateTimer.stop();
            pausedTime = System.currentTimeMillis();
            isPaused = true;
        }
    }
    
    /**
     * Resumes the timer after pause.
     */
    public void resume() {
        if (isPaused && updateTimer != null) {
            long pauseDuration = System.currentTimeMillis() - pausedTime;
            startTime += pauseDuration;
            isPaused = false;
            updateTimer.start();
        }
    }
    
    /**
     * Stops the timer.
     */
    public void stop() {
        if (updateTimer != null) {
            updateTimer.stop();
            updateTimer = null;
        }
    }
    
    /**
     * Resets the timer to 00:00.
     */
    public void reset() {
        stop();
        elapsedSeconds = 0;
        timerLabel.setText("00:00");
        timerLabel.setForeground(new Color(80, 80, 100));
    }
    
    /**
     * Gets the elapsed time in seconds.
     * @return Total elapsed seconds
     */
    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
    
    /**
     * Updates the timer display.
     */
    private void updateDisplay() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        elapsedSeconds = (int) elapsed;
        
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        
        // Color coding based on time (subtle)
        if (elapsedSeconds < 60) {
            timerLabel.setForeground(new Color(70, 150, 70)); // Green for < 1 min
        } else if (elapsedSeconds < 300) {
            timerLabel.setForeground(new Color(80, 80, 100)); // Normal for < 5 min
        } else {
            timerLabel.setForeground(new Color(150, 100, 70)); // Orange for long games
        }
    }
    
    /**
     * Gets formatted time string (MM:SS).
     * @return Formatted time string
     */
    public String getFormattedTime() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * Sets custom timer label color.
     * @param color Color for the timer text
     */
    public void setTimerColor(Color color) {
        timerLabel.setForeground(color);
    }
}
