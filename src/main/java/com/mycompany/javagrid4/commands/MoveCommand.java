package com.mycompany.javagrid4.commands;

import com.mycompany.javagrid4.GameEngine;
import com.mycompany.javagrid4.Player;

/**
 * Command representing a player's move on the game board.
 * Encapsulates all information needed to execute and undo a move.
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class MoveCommand implements GameCommand {
    
    private final GameEngine gameEngine;
    private final int row;
    private final int col;
    private final Player player;
    
    // State to restore on undo - we need to capture the entire affected area
    private int[] previousCellValues; // Values of clicked cell and neighbors
    private Player[] previousCellOwners; // Owners of clicked cell and neighbors
    private int[] cellPositions; // Row/col pairs of affected cells
    private Player previousCurrentPlayer;
    private boolean wasGameOver;
    private int previousPlayer1Score;
    private int previousPlayer2Score;
    private int pointsAwarded;
    
    /**
     * Creates a new move command.
     * 
     * @param gameEngine The game engine
     * @param row Cell row
     * @param col Cell column
     * @param player Player making the move
     */
    public MoveCommand(GameEngine gameEngine, int row, int col, Player player) {
        this.gameEngine = gameEngine;
        this.row = row;
        this.col = col;
        this.player = player;
    }
    
    @Override
    public void execute() {
        // Save current state before move
        captureState();
        
        // Execute move
        pointsAwarded = gameEngine.applyMove(row, col, player);
    }
    
    /**
     * Captures the current state of affected cells before the move.
     */
    private void captureState() {
        // Save game state
        previousCurrentPlayer = gameEngine.getCurrentPlayer();
        wasGameOver = gameEngine.isGameOver();
        previousPlayer1Score = gameEngine.getScore(Player.PLAYER_ONE);
        previousPlayer2Score = gameEngine.getScore(Player.PLAYER_TWO);
        
        // Identify affected cells (clicked + orthogonal neighbors)
        int gridSize = gameEngine.getGridSize();
        java.util.List<Integer> positions = new java.util.ArrayList<>();
        java.util.List<Integer> values = new java.util.ArrayList<>();
        java.util.List<Player> owners = new java.util.ArrayList<>();
        
        // Add clicked cell
        positions.add(row);
        positions.add(col);
        values.add(gameEngine.getCellValue(row, col));
        owners.add(gameEngine.getCellOwner(row, col));
        
        // Add neighbors
        if (row > 0) {
            positions.add(row - 1);
            positions.add(col);
            values.add(gameEngine.getCellValue(row - 1, col));
            owners.add(gameEngine.getCellOwner(row - 1, col));
        }
        if (row < gridSize - 1) {
            positions.add(row + 1);
            positions.add(col);
            values.add(gameEngine.getCellValue(row + 1, col));
            owners.add(gameEngine.getCellOwner(row + 1, col));
        }
        if (col > 0) {
            positions.add(row);
            positions.add(col - 1);
            values.add(gameEngine.getCellValue(row, col - 1));
            owners.add(gameEngine.getCellOwner(row, col - 1));
        }
        if (col < gridSize - 1) {
            positions.add(row);
            positions.add(col + 1);
            values.add(gameEngine.getCellValue(row, col + 1));
            owners.add(gameEngine.getCellOwner(row, col + 1));
        }
        
        // Convert to arrays
        cellPositions = positions.stream().mapToInt(i -> i).toArray();
        previousCellValues = values.stream().mapToInt(i -> i).toArray();
        previousCellOwners = owners.toArray(new Player[0]);
    }
    
    @Override
    public void undo() {
        // Restore cell values and owners
        for (int i = 0; i < previousCellValues.length; i++) {
            int r = cellPositions[i * 2];
            int c = cellPositions[i * 2 + 1];
            gameEngine.setCellValue(r, c, previousCellValues[i]);
            gameEngine.setCellOwner(r, c, previousCellOwners[i]);
        }
        
        // Restore scores
        gameEngine.getGameState().setScore(Player.PLAYER_ONE, previousPlayer1Score);
        gameEngine.getGameState().setScore(Player.PLAYER_TWO, previousPlayer2Score);
        
        // Restore current player
        gameEngine.setCurrentPlayer(previousCurrentPlayer);
        
        // Restore game over state
        gameEngine.setGameOver(wasGameOver);
    }
    
    @Override
    public String getDescription() {
        return String.format("Move by %s at (%d, %d)", player, row, col);
    }
}
