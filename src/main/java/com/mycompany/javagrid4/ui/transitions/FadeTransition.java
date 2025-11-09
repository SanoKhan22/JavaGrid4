package com.mycompany.javagrid4.ui.transitions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fade transition effect for smooth screen changes.
 * Implements fade-out, action, fade-in sequence.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class FadeTransition {
    
    private static final int FADE_DURATION = 300; // milliseconds
    private static final int FRAME_DELAY = 20; // milliseconds per frame
    private static final int TOTAL_FRAMES = FADE_DURATION / FRAME_DELAY;
    
    private final JComponent component;
    private Timer fadeTimer;
    private float currentAlpha = 1.0f;
    private boolean isFadingOut = true;
    private Runnable onComplete;
    private Runnable midTransition;
    
    /**
     * Creates a fade transition for the given component.
     * @param component Component to apply fade effect to
     */
    public FadeTransition(JComponent component) {
        this.component = component;
    }
    
    /**
     * Performs fade-out, executes action, then fade-in.
     * @param action Action to execute at the middle of transition
     * @param onComplete Action to execute when transition completes
     */
    public void fadeTransition(Runnable action, Runnable onComplete) {
        this.midTransition = action;
        this.onComplete = onComplete;
        this.isFadingOut = true;
        this.currentAlpha = 1.0f;
        
        startFade();
    }
    
    /**
     * Starts the fade animation.
     */
    private void startFade() {
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
        }
        
        fadeTimer = new Timer(FRAME_DELAY, new ActionListener() {
            private int frameCount = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                
                if (isFadingOut) {
                    // Fade out
                    currentAlpha = 1.0f - ((float) frameCount / TOTAL_FRAMES);
                    
                    if (frameCount >= TOTAL_FRAMES) {
                        currentAlpha = 0.0f;
                        frameCount = 0;
                        
                        // Execute mid-transition action
                        if (midTransition != null) {
                            midTransition.run();
                        }
                        
                        // Switch to fade in
                        isFadingOut = false;
                    }
                } else {
                    // Fade in
                    currentAlpha = (float) frameCount / TOTAL_FRAMES;
                    
                    if (frameCount >= TOTAL_FRAMES) {
                        currentAlpha = 1.0f;
                        fadeTimer.stop();
                        
                        // Execute completion action
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                }
                
                // Update component alpha
                updateComponentAlpha(currentAlpha);
                component.repaint();
            }
        });
        
        fadeTimer.start();
    }
    
    /**
     * Updates the component's alpha (transparency).
     * @param alpha Alpha value (0.0 = transparent, 1.0 = opaque)
     */
    private void updateComponentAlpha(float alpha) {
        // Clamp alpha between 0 and 1
        alpha = Math.max(0.0f, Math.min(1.0f, alpha));
        
        // Apply alpha to all child components recursively
        setAlphaRecursive(component, alpha);
    }
    
    /**
     * Recursively sets alpha on component and its children.
     */
    private void setAlphaRecursive(Component comp, float alpha) {
        if (comp instanceof JComponent) {
            JComponent jcomp = (JComponent) comp;
            
            // Set alpha composite for custom painting
            jcomp.putClientProperty("transition.alpha", alpha);
            
            // Update background with alpha
            Color bg = jcomp.getBackground();
            if (bg != null) {
                jcomp.setBackground(new Color(
                    bg.getRed(), 
                    bg.getGreen(), 
                    bg.getBlue(), 
                    (int)(255 * alpha)
                ));
            }
            
            // Update foreground with alpha
            Color fg = jcomp.getForeground();
            if (fg != null) {
                jcomp.setForeground(new Color(
                    fg.getRed(), 
                    fg.getGreen(), 
                    fg.getBlue(), 
                    (int)(255 * alpha)
                ));
            }
        }
        
        // Apply to children
        if (comp instanceof Container) {
            Container container = (Container) comp;
            for (Component child : container.getComponents()) {
                setAlphaRecursive(child, alpha);
            }
        }
    }
    
    /**
     * Stops any running fade animation.
     */
    public void stop() {
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
        }
    }
    
    /**
     * Simple fade out only.
     * @param onComplete Action when fade out completes
     */
    public void fadeOut(Runnable onComplete) {
        this.onComplete = onComplete;
        this.midTransition = null;
        this.isFadingOut = true;
        this.currentAlpha = 1.0f;
        
        fadeTimer = new Timer(FRAME_DELAY, new ActionListener() {
            private int frameCount = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                currentAlpha = 1.0f - ((float) frameCount / TOTAL_FRAMES);
                
                if (frameCount >= TOTAL_FRAMES) {
                    currentAlpha = 0.0f;
                    fadeTimer.stop();
                    updateComponentAlpha(0.0f);
                    
                    if (FadeTransition.this.onComplete != null) {
                        FadeTransition.this.onComplete.run();
                    }
                }
                
                updateComponentAlpha(currentAlpha);
                component.repaint();
            }
        });
        
        fadeTimer.start();
    }
    
    /**
     * Simple fade in only.
     * @param onComplete Action when fade in completes
     */
    public void fadeIn(Runnable onComplete) {
        this.onComplete = onComplete;
        this.currentAlpha = 0.0f;
        updateComponentAlpha(0.0f);
        
        fadeTimer = new Timer(FRAME_DELAY, new ActionListener() {
            private int frameCount = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                currentAlpha = (float) frameCount / TOTAL_FRAMES;
                
                if (frameCount >= TOTAL_FRAMES) {
                    currentAlpha = 1.0f;
                    fadeTimer.stop();
                    updateComponentAlpha(1.0f);
                    
                    if (FadeTransition.this.onComplete != null) {
                        FadeTransition.this.onComplete.run();
                    }
                }
                
                updateComponentAlpha(currentAlpha);
                component.repaint();
            }
        });
        
        fadeTimer.start();
    }
}
