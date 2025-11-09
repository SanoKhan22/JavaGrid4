package com.mycompany.javagrid4;

/**
 * Enum representing the two players in JavaGrid4.
 * Each player has an ID and display name.
 */
public enum Player {
    PLAYER_ONE(1, "Player 1"),
    PLAYER_TWO(2, "Player 2");
    
    private final int id;
    private final String displayName;
    
    /**
     * Constructor for Player enum.
     * @param id Numeric identifier for the player
     * @param displayName Human-readable name
     */
    Player(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
    
    /**
     * Gets the numeric ID of the player.
     * @return Player ID (1 or 2)
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the display name of the player.
     * @return Player's display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Returns the opposite player.
     * @return The other player
     */
    public Player getOpponent() {
        return this == PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE;
    }
}
