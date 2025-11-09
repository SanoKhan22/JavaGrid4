package com.mycompany.javagrid4;

/**
 * Core business logic for JavaGrid4 game.
 * Handles grid state, move validation, scoring, and game rules.
 * Contains no GUI dependencies.
 */
public class GameEngine {
    private static final int MAX_CELL_VALUE = 4;
    private static final int MIN_GRID_SIZE = 3;
    private static final int MAX_GRID_SIZE = 7;
    
    private int gridSize;
    private int[][] cellValues;
    private Player[][] cellOwners;
    private GameState gameState;
    
    /**
     * Creates a new GameEngine with specified grid size.
     * @param gridSize Size of the grid (3, 5, or 7)
     */
    public GameEngine(int gridSize) {
        if (gridSize < MIN_GRID_SIZE || gridSize > MAX_GRID_SIZE || gridSize % 2 == 0) {
            throw new IllegalArgumentException("Grid size must be 3, 5, or 7");
        }
        
        this.gridSize = gridSize;
        this.cellValues = new int[gridSize][gridSize];
        this.cellOwners = new Player[gridSize][gridSize];
        this.gameState = new GameState();
        
        initializeGrid();
    }
    
    /**
     * Initializes all grid cells to zero with no owners.
     */
    private void initializeGrid() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                cellValues[row][col] = 0;
                cellOwners[row][col] = null;
            }
        }
    }
    
    /**
     * Gets the current grid size.
     * @return Grid size (N for N×N grid)
     */
    public int getGridSize() {
        return gridSize;
    }
    
    /**
     * Gets the value at a specific cell.
     * @param row Row index
     * @param col Column index
     * @return Cell value (0-4)
     */
    public int getCellValue(int row, int col) {
        validatePosition(row, col);
        return cellValues[row][col];
    }
    
    /**
     * Gets the owner of a specific cell.
     * @param row Row index
     * @param col Column index
     * @return Owner player or null if neutral
     */
    public Player getCellOwner(int row, int col) {
        validatePosition(row, col);
        return cellOwners[row][col];
    }
    
    /**
     * Sets the owner of a specific cell (used for undo/redo).
     * @param row Row index
     * @param col Column index
     * @param owner Owner player or null
     */
    public void setCellOwner(int row, int col, Player owner) {
        validatePosition(row, col);
        cellOwners[row][col] = owner;
    }
    
    /**
     * Sets the value of a specific cell (used for undo/redo).
     * @param row Row index
     * @param col Column index
     * @param value Cell value (0-4)
     */
    public void setCellValue(int row, int col, int value) {
        validatePosition(row, col);
        if (value < 0 || value > MAX_CELL_VALUE) {
            throw new IllegalArgumentException("Cell value must be between 0 and " + MAX_CELL_VALUE);
        }
        cellValues[row][col] = value;
    }
    
    /**
     * Applies a move at the specified position for the given player.
     * Increments the clicked cell and its orthogonal neighbors.
     * Awards points if any cell reaches 4.
     * 
     * @param row Row index
     * @param col Column index
     * @param player Player making the move
     * @return Number of points awarded for this move
     */
    public int applyMove(int row, int col, Player player) {
        validatePosition(row, col);
        
        if (gameState.isGameOver()) {
            return 0;
        }
        
        int pointsAwarded = 0;
        
        // Increment clicked cell
        pointsAwarded += incrementAndCheck(row, col, player);
        
        // Increment orthogonal neighbors
        if (row > 0) {
            pointsAwarded += incrementAndCheck(row - 1, col, player);
        }
        if (row < gridSize - 1) {
            pointsAwarded += incrementAndCheck(row + 1, col, player);
        }
        if (col > 0) {
            pointsAwarded += incrementAndCheck(row, col - 1, player);
        }
        if (col < gridSize - 1) {
            pointsAwarded += incrementAndCheck(row, col + 1, player);
        }
        
        // Award points to player
        if (pointsAwarded > 0) {
            gameState.addScore(player, pointsAwarded);
        }
        
        // Check if game is over
        checkGameEnd();
        
        return pointsAwarded;
    }
    
    /**
     * Increments a cell and checks if it reaches max value.
     * If cell reaches 4, assigns ownership to player.
     * 
     * @param row Row index
     * @param col Column index
     * @param player Current player
     * @return 1 if cell newly reached 4, 0 otherwise
     */
    private int incrementAndCheck(int row, int col, Player player) {
        if (cellValues[row][col] < MAX_CELL_VALUE) {
            cellValues[row][col]++;
            
            if (cellValues[row][col] == MAX_CELL_VALUE && cellOwners[row][col] == null) {
                cellOwners[row][col] = player;
                return 1; // Award point for newly claimed cell
            }
        }
        return 0;
    }
    
    /**
     * Checks if the game has ended (all cells at max value).
     */
    private void checkGameEnd() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (cellValues[row][col] < MAX_CELL_VALUE) {
                    return; // Game continues
                }
            }
        }
        gameState.setGameOver(true);
    }
    
    /**
     * Checks if the game is over.
     * @return true if all cells have reached max value
     */
    public boolean isGameOver() {
        return gameState.isGameOver();
    }
    
    /**
     * Gets the score for a specific player.
     * @param player Player to get score for
     * @return Player's current score
     */
    public int getScore(Player player) {
        return gameState.getScore(player);
    }
    
    /**
     * Gets the current game state manager.
     * @return GameState object
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * Gets the current player.
     * @return Current player
     */
    public Player getCurrentPlayer() {
        return gameState.getCurrentPlayer();
    }
    
    /**
     * Sets the current player (used for undo/redo).
     * @param player Player to set as current
     */
    public void setCurrentPlayer(Player player) {
        gameState.setCurrentPlayer(player);
    }
    
    /**
     * Sets the game over status (used for undo/redo).
     * @param gameOver Game over status
     */
    public void setGameOver(boolean gameOver) {
        gameState.setGameOver(gameOver);
    }
    
    /**
     * Gets the winner (used for undo/redo).
     * @return Winning player or null
     */
    public Player getWinner() {
        return gameState.getWinner();
    }
    
    /**
     * Sets winner to null (used for undo/redo).
     * This is handled by resetting game over state.
     * @param winner Should be null
     */
    public void setWinner(Player winner) {
        // Winner is computed from scores, so this is a no-op
        // Used for command interface compatibility
    }
    
    /**
     * Resets the board to initial state with same grid size.
     */
    public void resetBoard() {
        initializeGrid();
        gameState.reset();
    }
    
    /**
     * Changes the grid size and resets the game.
     * @param newGridSize New grid size (3, 5, or 7)
     */
    public void changeGridSize(int newGridSize) {
        if (newGridSize < MIN_GRID_SIZE || newGridSize > MAX_GRID_SIZE || newGridSize % 2 == 0) {
            throw new IllegalArgumentException("Grid size must be 3, 5, or 7");
        }
        
        this.gridSize = newGridSize;
        this.cellValues = new int[gridSize][gridSize];
        this.cellOwners = new Player[gridSize][gridSize];
        
        initializeGrid();
        gameState.reset();
    }
    
    /**
     * Validates that a position is within grid bounds.
     * @param row Row index
     * @param col Column index
     * @throws IllegalArgumentException if position is invalid
     */
    private void validatePosition(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
            throw new IllegalArgumentException(
                String.format("Position (%d, %d) is out of bounds for %dx%d grid", 
                    row, col, gridSize, gridSize));
        }
    }
}
