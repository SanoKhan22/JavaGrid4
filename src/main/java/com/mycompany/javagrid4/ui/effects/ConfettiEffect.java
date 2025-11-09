package com.mycompany.javagrid4.ui.effects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Victory confetti effect overlay panel.
 * Displays animated confetti particles falling across the screen.
 * 
 * Features:
 * - Physics simulation (gravity, air resistance)
 * - Multiple particle shapes (rectangle, circle, triangle, star)
 * - Colorful particles with fade-out effect
 * - Configurable duration and particle count
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class ConfettiEffect extends JPanel {
    
    private final List<Particle> particles;
    private final Timer animationTimer;
    private final Random random;
    
    // Configuration
    private static final int PARTICLE_COUNT = 150;
    private static final int FRAME_DELAY = 16; // ~60fps
    private static final int EFFECT_DURATION = 5000; // 5 seconds
    
    // Particle properties
    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 12;
    private static final double MIN_VELOCITY_X = -3.0;
    private static final double MAX_VELOCITY_X = 3.0;
    private static final double MIN_VELOCITY_Y = -15.0;
    private static final double MAX_VELOCITY_Y = -5.0;
    private static final double MIN_ROTATION_SPEED = -0.2;
    private static final double MAX_ROTATION_SPEED = 0.2;
    private static final double FADE_RATE = 0.005; // Alpha reduction per frame
    
    // Vibrant confetti colors
    private static final Color[] CONFETTI_COLORS = {
        new Color(255, 59, 48),   // Red
        new Color(255, 149, 0),   // Orange
        new Color(255, 204, 0),   // Yellow
        new Color(52, 199, 89),   // Green
        new Color(0, 122, 255),   // Blue
        new Color(88, 86, 214),   // Indigo
        new Color(175, 82, 222),  // Purple
        new Color(255, 45, 85),   // Pink
        new Color(90, 200, 250),  // Cyan
        new Color(255, 55, 95)    // Magenta
    };
    
    private Runnable onComplete;
    
    /**
     * Creates a new confetti effect.
     * 
     * @param width Panel width
     * @param height Panel height
     */
    public ConfettiEffect(int width, int height) {
        setOpaque(false); // Transparent background
        setSize(width, height);
        
        particles = new ArrayList<>();
        random = new Random();
        
        // Create particles
        createParticles(width, height);
        
        // Animation timer
        animationTimer = new Timer(FRAME_DELAY, e -> {
            updateParticles();
            repaint();
            
            // Check if all particles are dead
            if (particles.stream().noneMatch(Particle::isAlive)) {
                stopEffect();
            }
        });
        
        // Auto-stop after duration
        Timer durationTimer = new Timer(EFFECT_DURATION, e -> stopEffect());
        durationTimer.setRepeats(false);
        durationTimer.start();
    }
    
    /**
     * Creates confetti particles across the top of the screen.
     * 
     * @param width Panel width
     * @param height Panel height
     */
    private void createParticles(int width, int height) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            // Random position across the top
            double x = random.nextDouble() * width;
            double y = -50 + random.nextDouble() * 100; // Start above screen
            
            // Random velocity (spread outward and upward)
            double velocityX = MIN_VELOCITY_X + random.nextDouble() * (MAX_VELOCITY_X - MIN_VELOCITY_X);
            double velocityY = MIN_VELOCITY_Y + random.nextDouble() * (MAX_VELOCITY_Y - MIN_VELOCITY_Y);
            
            // Random appearance
            Color color = CONFETTI_COLORS[random.nextInt(CONFETTI_COLORS.length)];
            int size = MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE + 1);
            Particle.Shape shape = Particle.Shape.values()[random.nextInt(Particle.Shape.values().length)];
            
            // Random rotation
            double rotationSpeed = MIN_ROTATION_SPEED + random.nextDouble() * (MAX_ROTATION_SPEED - MIN_ROTATION_SPEED);
            
            particles.add(new Particle(x, y, velocityX, velocityY, color, size, shape, rotationSpeed, FADE_RATE));
        }
    }
    
    /**
     * Updates all particles.
     */
    private void updateParticles() {
        for (Particle particle : particles) {
            particle.update();
        }
        
        // Remove dead or off-screen particles
        particles.removeIf(p -> !p.isAlive() || p.isOffScreen(getWidth(), getHeight()));
    }
    
    /**
     * Starts the confetti animation.
     */
    public void start() {
        animationTimer.start();
    }
    
    /**
     * Stops the confetti animation.
     */
    private void stopEffect() {
        if (animationTimer.isRunning()) {
            animationTimer.stop();
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }
    
    /**
     * Sets a callback to run when the effect completes.
     * 
     * @param onComplete Callback to run
     */
    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
    
    /**
     * Paints the confetti particles.
     * 
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw all particles
        for (Particle particle : particles) {
            particle.draw(g2d);
        }
    }
}
