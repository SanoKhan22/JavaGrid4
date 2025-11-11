package com.mycompany.javagrid4.ui.effects;

import com.mycompany.javagrid4.ui.components.CustomGridCell;
import javax.swing.Timer;

/**
 * Animates a cell when it gets claimed (reaches value 4).
 * Provides multiple animation types:
 * - Pop: Scale effect (cell grows and shrinks)
 * - Pulse: Glow effect (pulsing aura)
 * - Burst: Particle explosion
 * 
 * Enhances visual feedback for successful cell claims.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class CellClaimAnimation {
    
    /**
     * Animation type enum.
     */
    public enum AnimationType {
        POP,        // Scale animation
        PULSE,      // Glow pulse
        BURST,      // Particle burst
        COMBO       // Combination of effects
    }
    
    private final CustomGridCell cell;
    private Timer animationTimer;
    private int frameCount = 0;
    private static final int TOTAL_FRAMES = 10; // ~160ms (optimized for performance)
    private AnimationType animationType;
    private Runnable onComplete;
    
    /**
     * Creates an animation for a cell.
     * @param cell The cell to animate
     */
    public CellClaimAnimation(CustomGridCell cell) {
        this.cell = cell;
        this.animationType = AnimationType.COMBO; // Default to combo
    }
    
    /**
     * Sets the animation type.
     * @param type The type of animation
     * @return this for method chaining
     */
    public CellClaimAnimation setAnimationType(AnimationType type) {
        this.animationType = type;
        return this;
    }
    
    /**
     * Plays the animation with a callback on completion.
     * @param onComplete Callback to run when animation finishes
     */
    public void play(Runnable onComplete) {
        this.onComplete = onComplete;
        
        // Choose animation based on type
        switch (animationType) {
            case POP:
                playPopAnimation();
                break;
            case PULSE:
                playPulseAnimation();
                break;
            case BURST:
                playBurstAnimation();
                break;
            case COMBO:
                playComboAnimation();
                break;
        }
    }
    
    /**
     * Plays the animation without a callback.
     */
    public void play() {
        play(() -> {});
    }
    
    /**
     * Pop animation: Cell scales up and down (optimized).
     */
    private void playPopAnimation() {
        frameCount = 0;
        
        animationTimer = new Timer(16, e -> { // ~60fps
            frameCount++;
            
            // Calculate scale using sine wave for smooth animation
            double progress = frameCount / (double) TOTAL_FRAMES;
            double scale = 1.0 + (0.2 * Math.sin(progress * Math.PI)); // Reduced magnitude
            
            // Apply scale to cell
            cell.setAnimationScale(scale);
            cell.repaint();
            
            if (frameCount >= TOTAL_FRAMES) {
                animationTimer.stop();
                cell.setAnimationScale(1.0);
                cell.repaint();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Pulse animation: Glowing aura pulses (optimized).
     */
    private void playPulseAnimation() {
        frameCount = 0;
        
        animationTimer = new Timer(16, e -> {
            frameCount++;
            
            // Calculate glow intensity (reduced for performance)
            double progress = frameCount / (double) TOTAL_FRAMES;
            float glowIntensity = (float) (0.7 * Math.sin(progress * Math.PI));
            
            // Apply glow to cell
            cell.setGlowIntensity(glowIntensity);
            cell.repaint();
            
            if (frameCount >= TOTAL_FRAMES) {
                animationTimer.stop();
                cell.setGlowIntensity(0.0f);
                cell.repaint();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Burst animation: Small particles explode from cell (optimized).
     */
    private void playBurstAnimation() {
        frameCount = 0;
        
        // Create fewer particles for performance
        int particleCount = 8; // Reduced from 12
        BurstParticle[] particles = new BurstParticle[particleCount];
        
        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            particles[i] = new BurstParticle(angle);
        }
        
        animationTimer = new Timer(16, e -> {
            frameCount++;
            
            // Update particle positions
            double progress = frameCount / (double) TOTAL_FRAMES;
            for (BurstParticle particle : particles) {
                particle.update(progress);
            }
            
            // Pass particles to cell for rendering
            cell.setBurstParticles(particles);
            cell.repaint();
            
            if (frameCount >= TOTAL_FRAMES) {
                animationTimer.stop();
                cell.setBurstParticles(null);
                cell.repaint();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Combo animation: Combines pop and pulse effects (optimized).
     */
    private void playComboAnimation() {
        frameCount = 0;
        
        animationTimer = new Timer(16, e -> {
            frameCount++;
            
            double progress = frameCount / (double) TOTAL_FRAMES;
            
            // Simplified scale effect (smaller magnitude)
            double scale = 1.0 + (0.15 * Math.sin(progress * Math.PI));
            cell.setAnimationScale(scale);
            
            // Simplified glow effect (reduced intensity)
            float glowIntensity = (float) (0.6 * Math.sin(progress * Math.PI));
            cell.setGlowIntensity(glowIntensity);
            
            cell.repaint();
            
            if (frameCount >= TOTAL_FRAMES) {
                animationTimer.stop();
                cell.setAnimationScale(1.0);
                cell.setGlowIntensity(0.0f);
                cell.repaint();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Stops the animation immediately.
     */
    public void stop() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
            cell.setAnimationScale(1.0);
            cell.setGlowIntensity(0.0f);
            cell.setBurstParticles(null);
            cell.repaint();
        }
    }
    
    /**
     * Inner class representing a burst particle.
     */
    public static class BurstParticle {
        private double angle;
        private double distance;
        private float alpha;
        private double size;
        
        public BurstParticle(double angle) {
            this.angle = angle;
            this.distance = 0;
            this.alpha = 1.0f;
            this.size = 4.0;
        }
        
        public void update(double progress) {
            // Particles move outward with ease-out
            this.distance = 30 * (1 - Math.pow(1 - progress, 3));
            // Fade out
            this.alpha = (float) (1 - progress);
            // Shrink slightly
            this.size = 4.0 * (1 - progress * 0.5);
        }
        
        public double getX() {
            return Math.cos(angle) * distance;
        }
        
        public double getY() {
            return Math.sin(angle) * distance;
        }
        
        public float getAlpha() {
            return alpha;
        }
        
        public double getSize() {
            return size;
        }
    }
}
