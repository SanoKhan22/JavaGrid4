package com.mycompany.javagrid4.models;

/**
 * Complete game configuration from menu screen.
 * Holds player configurations and board size selection.
 * Passed between screens to maintain game setup.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class GameConfig {
    private PlayerConfig player1;
    private PlayerConfig player2;
    private int boardSize;
    
    /**
     * Creates a game configuration with default values.
     * - Player 1 and Player 2 with default names/colors
     * - Board size 3×3
     */
    public GameConfig() {
        this.player1 = new PlayerConfig(1);
        this.player2 = new PlayerConfig(2);
        this.boardSize = 3; // Default 3×3
    }
    
    /**
     * Creates a game configuration with custom values.
     * @param player1 Player 1 configuration
     * @param player2 Player 2 configuration
     * @param boardSize Board size (3, 5, or 7)
     */
    public GameConfig(PlayerConfig player1, PlayerConfig player2, int boardSize) {
        this.player1 = player1;
        this.player2 = player2;
        this.boardSize = boardSize;
    }
    
    /**
     * Gets Player 1 configuration.
     * @return Player 1 config
     */
    public PlayerConfig getPlayer1() {
        return player1;
    }
    
    /**
     * Sets Player 1 configuration.
     * @param player1 New Player 1 config
     */
    public void setPlayer1(PlayerConfig player1) {
        this.player1 = player1;
    }
    
    /**
     * Gets Player 2 configuration.
     * @return Player 2 config
     */
    public PlayerConfig getPlayer2() {
        return player2;
    }
    
    /**
     * Sets Player 2 configuration.
     * @param player2 New Player 2 config
     */
    public void setPlayer2(PlayerConfig player2) {
        this.player2 = player2;
    }
    
    /**
     * Gets the board size.
     * @return Board size (3, 5, or 7)
     */
    public int getBoardSize() {
        return boardSize;
    }
    
    /**
     * Sets the board size.
     * @param boardSize New board size (3, 5, or 7)
     */
    public void setBoardSize(int boardSize) {
        if (boardSize != 3 && boardSize != 5 && boardSize != 7) {
            throw new IllegalArgumentException("Board size must be 3, 5, or 7");
        }
        this.boardSize = boardSize;
    }
    
    /**
     * Validates that this configuration is complete and valid.
     * @return true if both players are valid and board size is 3, 5, or 7
     */
    public boolean isValid() {
        return player1 != null && player1.isValid() 
            && player2 != null && player2.isValid() 
            && (boardSize == 3 || boardSize == 5 || boardSize == 7);
    }
    
    @Override
    public String toString() {
        return String.format("GameConfig[player1=%s, player2=%s, boardSize=%d]", 
            player1, player2, boardSize);
    }
}
