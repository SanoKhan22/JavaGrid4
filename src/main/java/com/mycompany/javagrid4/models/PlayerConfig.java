package com.mycompany.javagrid4.models;

import java.awt.Color;

/**
 * Configuration for a player including name and color choice.
 * Used by MenuPanel to capture player setup data.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class PlayerConfig {
    private final int playerId;
    private String name;
    private Color color;
    
    /**
     * Creates a player configuration with default values.
     * @param playerId Player identifier (1 or 2)
     */
    public PlayerConfig(int playerId) {
        this.playerId = playerId;
        this.name = "Player " + playerId;
        this.color = (playerId == 1) 
            ? new Color(255, 100, 100)  // Default red for Player 1
            : new Color(100, 150, 255); // Default blue for Player 2
    }
    
    /**
     * Creates a player configuration with custom values.
     * @param playerId Player identifier (1 or 2)
     * @param name Player's display name
     * @param color Player's chosen color
     */
    public PlayerConfig(int playerId, String name, Color color) {
        this.playerId = playerId;
        this.name = name;
        this.color = color;
    }
    
    /**
     * Gets the player ID.
     * @return Player identifier (1 or 2)
     */
    public int getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the player's name.
     * @return Player's display name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the player's name.
     * @param name New name (max 10 characters)
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the player's color.
     * @return Player's chosen color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the player's color.
     * @param color New color
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Validates that this configuration is complete and valid.
     * @return true if name is 1-10 characters and color is set
     */
    public boolean isValid() {
        return name != null 
            && !name.trim().isEmpty() 
            && name.length() <= 10 
            && color != null;
    }
    
    @Override
    public String toString() {
        return String.format("PlayerConfig[id=%d, name='%s', color=%s]", 
            playerId, name, color);
    }
}
