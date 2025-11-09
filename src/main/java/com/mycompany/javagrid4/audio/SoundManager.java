package com.mycompany.javagrid4.audio;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages game audio including sound effects and background music.
 * Handles loading, caching, and playback of audio files.
 * 
 * Features:
 * - Sound effect playback with volume control
 * - Audio caching for performance
 * - Master volume control
 * - Mute/unmute functionality
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class SoundManager {
    
    private static SoundManager instance;
    
    // Volume control (0.0 to 1.0)
    private float masterVolume = 0.7f;
    private boolean muted = false;
    
    // Sound cache
    private final Map<String, Clip> soundCache;
    
    // Sound effect keys
    public static final String SOUND_CLICK = "click";
    public static final String SOUND_CLAIM = "claim";
    public static final String SOUND_VICTORY = "victory";
    public static final String SOUND_BUTTON = "button";
    public static final String SOUND_ERROR = "error";
    
    /**
     * Private constructor for singleton pattern.
     */
    private SoundManager() {
        soundCache = new HashMap<>();
        initializeSounds();
    }
    
    /**
     * Gets the singleton instance.
     * @return SoundManager instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Initializes all game sounds.
     * Creates synthetic sounds if audio files are not available.
     */
    private void initializeSounds() {
        // We'll create synthetic sounds programmatically
        // This avoids the need for external audio files
        try {
            createSyntheticSound(SOUND_CLICK, 100, 0.1f);      // Short click
            createSyntheticSound(SOUND_CLAIM, 300, 0.15f);     // Cell claim
            createSyntheticSound(SOUND_VICTORY, 500, 0.3f);    // Victory
            createSyntheticSound(SOUND_BUTTON, 150, 0.12f);    // Button press
            createSyntheticSound(SOUND_ERROR, 200, 0.15f);     // Error/invalid
        } catch (Exception e) {
            System.err.println("Failed to initialize sounds: " + e.getMessage());
        }
    }
    
    /**
     * Creates a synthetic beep sound.
     * @param frequency Sound frequency in Hz
     * @param duration Sound duration in seconds
     */
    private void createSyntheticSound(String key, int frequency, float duration) {
        try {
            // Audio format: 44.1kHz, 8-bit, mono
            AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
            int samples = (int) (format.getSampleRate() * duration);
            byte[] buffer = new byte[samples];
            
            // Generate sine wave with envelope for smooth sound
            for (int i = 0; i < samples; i++) {
                double angle = 2.0 * Math.PI * i * frequency / format.getSampleRate();
                
                // Apply envelope (fade in/out) to avoid clicks
                double envelope = 1.0;
                if (i < samples * 0.1) {
                    // Fade in (first 10%)
                    envelope = i / (samples * 0.1);
                } else if (i > samples * 0.7) {
                    // Fade out (last 30%)
                    envelope = (samples - i) / (samples * 0.3);
                }
                
                buffer[i] = (byte) (Math.sin(angle) * 127 * envelope);
            }
            
            // Create clip from buffer
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(format, buffer, 0, buffer.length);
            
            soundCache.put(key, clip);
            
        } catch (LineUnavailableException e) {
            System.err.println("Failed to create sound " + key + ": " + e.getMessage());
        }
    }
    
    /**
     * Plays a sound effect.
     * @param soundKey Sound identifier
     */
    public void playSound(String soundKey) {
        if (muted) {
            return;
        }
        
        Clip clip = soundCache.get(soundKey);
        if (clip != null) {
            // Run in separate thread to avoid blocking
            new Thread(() -> {
                try {
                    // Stop if already playing
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    
                    // Reset to beginning
                    clip.setFramePosition(0);
                    
                    // Set volume
                    setClipVolume(clip, masterVolume);
                    
                    // Play
                    clip.start();
                    
                } catch (Exception e) {
                    System.err.println("Error playing sound: " + e.getMessage());
                }
            }).start();
        }
    }
    
    /**
     * Sets the volume for a specific clip.
     * @param clip The audio clip
     * @param volume Volume level (0.0 to 1.0)
     */
    private void setClipVolume(Clip clip, float volume) {
        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Convert linear volume to decibels
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                // Clamp to control's range
                dB = Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum()));
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            System.err.println("Failed to set volume: " + e.getMessage());
        }
    }
    
    /**
     * Sets the master volume level.
     * @param volume Volume (0.0 = silent, 1.0 = full)
     */
    public void setVolume(float volume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    /**
     * Gets the current master volume.
     * @return Volume level (0.0 to 1.0)
     */
    public float getVolume() {
        return masterVolume;
    }
    
    /**
     * Mutes all sounds.
     */
    public void mute() {
        muted = true;
    }
    
    /**
     * Unmutes all sounds.
     */
    public void unmute() {
        muted = false;
    }
    
    /**
     * Toggles mute state.
     */
    public void toggleMute() {
        muted = !muted;
    }
    
    /**
     * Checks if sound is muted.
     * @return true if muted
     */
    public boolean isMuted() {
        return muted;
    }
    
    /**
     * Stops all currently playing sounds.
     */
    public void stopAll() {
        for (Clip clip : soundCache.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
    
    /**
     * Cleans up resources.
     * Call this when shutting down the application.
     */
    public void cleanup() {
        stopAll();
        for (Clip clip : soundCache.values()) {
            clip.close();
        }
        soundCache.clear();
    }
}
