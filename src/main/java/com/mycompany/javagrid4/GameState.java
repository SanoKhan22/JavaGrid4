package com.mycompany.javagrid4;

/**
 * Manages the state of the JavaGrid4 game including scores, current player,
 * and game status.
 */
public class GameState {
    private int player1Score;
    private int player2Score;
    private Player currentPlayer;
    private boolean gameOver;
    
    /**
     * Creates a new GameState with default values.
     * Player 1 starts first, scores are zero, game is not over.
     */
    public GameState() {
        this.player1Score = 0;
        this.player2Score = 0;
        this.currentPlayer = Player.PLAYER_ONE;
        this.gameOver = false;
    }
    
    /**
     * Gets the score for a specific player.
     * @param player The player to get the score for
     * @return The player's current score
     */
    public int getScore(Player player) {
        return player == Player.PLAYER_ONE ? player1Score : player2Score;
    }
    
    /**
     * Adds points to a player's score.
     * @param player The player receiving points
     * @param points Number of points to add
     */
    public void addScore(Player player, int points) {
        if (player == Player.PLAYER_ONE) {
            player1Score += points;
        } else {
            player2Score += points;
        }
    }
    
    /**
     * Gets the current active player.
     * @return The player whose turn it is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Switches to the next player's turn.
     */
    public void switchPlayer() {
        currentPlayer = currentPlayer.getOpponent();
    }
    
    /**
     * Checks if the game is over.
     * @return true if game has ended, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Sets the game over status.
     * @param gameOver true to mark game as finished
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    /**
     * Determines the winner based on current scores.
     * @return The winning player, or null if tied
     */
    public Player getWinner() {
        if (player1Score > player2Score) {
            return Player.PLAYER_ONE;
        } else if (player2Score > player1Score) {
            return Player.PLAYER_TWO;
        }
        return null; // Tie
    }
    
    /**
     * Resets all game state to initial values.
     */
    public void reset() {
        player1Score = 0;
        player2Score = 0;
        currentPlayer = Player.PLAYER_ONE;
        gameOver = false;
    }
}
