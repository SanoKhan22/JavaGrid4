package com.mycompany.javagrid4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main GUI controller for JavaGrid4.
 * Handles all visual components, layout, and user interactions.
 * Delegates game logic to GameEngine.
 */
public class GamePanel extends JPanel {
    private static final int DEFAULT_GRID_SIZE = 3;
    private static final String[] GRID_SIZE_OPTIONS = {"3x3", "5x5", "7x7"};
    
    private GameEngine gameEngine;
    private GridCell[][] gridCells;
    
    // GUI Components
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    
    private JComboBox<String> sizeSelector;
    private JLabel currentPlayerLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    
    /**
     * Creates a new GamePanel with default grid size.
     */
    public GamePanel() {
        this.gameEngine = new GameEngine(DEFAULT_GRID_SIZE);
        initializeComponents();
        setupLayout();
        createGrid(DEFAULT_GRID_SIZE);
        updateDisplay();
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel with size selector
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel sizeLabel = new JLabel("Board Size:");
        sizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        sizeSelector = new JComboBox<>(GRID_SIZE_OPTIONS);
        sizeSelector.setFont(new Font("Arial", Font.PLAIN, 14));
        sizeSelector.addActionListener(this::handleSizeChange);
        
        topPanel.add(sizeLabel);
        topPanel.add(sizeSelector);
        
        // Center panel for grid
        centerPanel = new JPanel();
        
        // Bottom panel with scores and current player
        bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        player1ScoreLabel = new JLabel("Player 1: 0", SwingConstants.CENTER);
        player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        player1ScoreLabel.setForeground(new Color(255, 50, 50));
        
        currentPlayerLabel = new JLabel("Current: Player 1", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        player2ScoreLabel = new JLabel("Player 2: 0", SwingConstants.CENTER);
        player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        player2ScoreLabel.setForeground(new Color(50, 100, 255));
        
        bottomPanel.add(player1ScoreLabel);
        bottomPanel.add(currentPlayerLabel);
        bottomPanel.add(player2ScoreLabel);
    }
    
    /**
     * Sets up the main layout structure.
     */
    private void setupLayout() {
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the game grid with specified size.
     * @param size Grid size (3, 5, or 7)
     */
    private void createGrid(int size) {
        centerPanel.removeAll();
        centerPanel.setLayout(new GridLayout(size, size, 5, 5));
        
        gridCells = new GridCell[size][size];
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                GridCell cell = new GridCell(row, col);
                cell.addActionListener(this::handleCellClick);
                gridCells[row][col] = cell;
                centerPanel.add(cell);
            }
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    /**
     * Handles cell click events.
     * @param event Action event from clicked cell
     */
    private void handleCellClick(ActionEvent event) {
        if (gameEngine.isGameOver()) {
            return;
        }
        
        GridCell clickedCell = (GridCell) event.getSource();
        int row = clickedCell.getRow();
        int col = clickedCell.getColumn();
        
        Player currentPlayer = gameEngine.getGameState().getCurrentPlayer();
        
        // Apply move through game engine
        gameEngine.applyMove(row, col, currentPlayer);
        
        // Switch player
        gameEngine.getGameState().switchPlayer();
        
        // Update all cell displays
        syncGridWithEngine();
        
        // Update score and player display
        updateDisplay();
        
        // Check if game ended
        if (gameEngine.isGameOver()) {
            handleGameEnd();
        }
    }
    
    /**
     * Handles board size change events.
     * @param event Action event from size selector
     */
    private void handleSizeChange(ActionEvent event) {
        String selected = (String) sizeSelector.getSelectedItem();
        int newSize = Integer.parseInt(selected.substring(0, 1));
        
        // Change grid size in engine
        gameEngine.changeGridSize(newSize);
        
        // Recreate visual grid
        createGrid(newSize);
        
        // Update display
        updateDisplay();
    }
    
    /**
     * Synchronizes visual grid cells with engine state.
     */
    private void syncGridWithEngine() {
        int size = gameEngine.getGridSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                GridCell cell = gridCells[row][col];
                cell.setCellValue(gameEngine.getCellValue(row, col));
                cell.setOwner(gameEngine.getCellOwner(row, col));
            }
        }
    }
    
    /**
     * Updates score labels and current player display.
     */
    private void updateDisplay() {
        int player1Score = gameEngine.getScore(Player.PLAYER_ONE);
        int player2Score = gameEngine.getScore(Player.PLAYER_TWO);
        Player currentPlayer = gameEngine.getGameState().getCurrentPlayer();
        
        player1ScoreLabel.setText(String.format("%s: %d", 
            Player.PLAYER_ONE.getDisplayName(), player1Score));
        player2ScoreLabel.setText(String.format("%s: %d", 
            Player.PLAYER_TWO.getDisplayName(), player2Score));
        currentPlayerLabel.setText("Current: " + currentPlayer.getDisplayName());
    }
    
    /**
     * Handles game end scenario - shows winner and offers restart.
     */
    private void handleGameEnd() {
        Player winner = gameEngine.getGameState().getWinner();
        String message;
        
        if (winner == null) {
            message = "Game Over! It's a tie!\n";
        } else {
            message = String.format("Game Over! %s wins!\n", winner.getDisplayName());
        }
        
        message += String.format("%s: %d points\n%s: %d points\n\nStart a new game?",
            Player.PLAYER_ONE.getDisplayName(), gameEngine.getScore(Player.PLAYER_ONE),
            Player.PLAYER_TWO.getDisplayName(), gameEngine.getScore(Player.PLAYER_TWO));
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            message,
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        }
    }
    
    /**
     * Restarts the game with current grid size.
     */
    private void restartGame() {
        gameEngine.resetBoard();
        syncGridWithEngine();
        updateDisplay();
    }
}
