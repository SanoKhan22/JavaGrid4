package com.mycompany.javagrid4;

import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.components.CustomGridCell;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main GUI controller for JavaGrid4.
 * Handles all visual components, layout, and user interactions.
 * Delegates game logic to GameEngine.
 */
public class GamePanel extends JPanel {
    private GameEngine gameEngine;
    private CustomGridCell[][] gridCells;
    private GameConfig config;
    
    // GUI Components
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    
    private JLabel titleLabel;
    private JLabel currentPlayerLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    
    /**
     * Creates a new GamePanel with game configuration.
     * @param config Game configuration with player names, colors, and board size
     */
    public GamePanel(GameConfig config) {
        this.config = config;
        this.gameEngine = new GameEngine(config.getBoardSize());
        initializeComponents();
        setupLayout();
        createGrid(config.getBoardSize());
        updateDisplay();
    }
    
    /**
     * Initializes all GUI components.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top panel with game title
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        
        titleLabel = new JLabel("JavaGrid4");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(String.format("%dx%d Board", 
            config.getBoardSize(), config.getBoardSize()));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(subtitleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        
        // Center panel for grid
        centerPanel = new JPanel();
        
        // Bottom panel with scores and current player
        bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        player1ScoreLabel = new JLabel(config.getPlayer1().getName() + ": 0", SwingConstants.CENTER);
        player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        player1ScoreLabel.setForeground(config.getPlayer1().getColor());
        
        currentPlayerLabel = new JLabel("Current: " + config.getPlayer1().getName(), SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentPlayerLabel.setForeground(config.getPlayer1().getColor());
        
        player2ScoreLabel = new JLabel(config.getPlayer2().getName() + ": 0", SwingConstants.CENTER);
        player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        player2ScoreLabel.setForeground(config.getPlayer2().getColor());
        
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
        
        gridCells = new CustomGridCell[size][size];
        
        Color player1Color = config.getPlayer1().getColor();
        Color player2Color = config.getPlayer2().getColor();
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CustomGridCell cell = new CustomGridCell(row, col, player1Color, player2Color);
                
                // Add mouse listener for clicks
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCellClick(cell);
                    }
                });
                
                gridCells[row][col] = cell;
                centerPanel.add(cell);
            }
        }
        
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    /**
     * Handles cell click events.
     * @param clickedCell The cell that was clicked
     */
    private void handleCellClick(CustomGridCell clickedCell) {
        if (gameEngine.isGameOver()) {
            return;
        }
        
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
     * Synchronizes visual grid cells with engine state.
     */
    private void syncGridWithEngine() {
        int size = gameEngine.getGridSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CustomGridCell cell = gridCells[row][col];
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
        
        // Get player names and colors from config
        String player1Name = config.getPlayer1().getName();
        String player2Name = config.getPlayer2().getName();
        Color player1Color = config.getPlayer1().getColor();
        Color player2Color = config.getPlayer2().getColor();
        
        player1ScoreLabel.setText(String.format("%s: %d", player1Name, player1Score));
        player1ScoreLabel.setForeground(player1Color);
        
        player2ScoreLabel.setText(String.format("%s: %d", player2Name, player2Score));
        player2ScoreLabel.setForeground(player2Color);
        
        // Update current player display
        if (currentPlayer == Player.PLAYER_ONE) {
            currentPlayerLabel.setText("Current: " + player1Name);
            currentPlayerLabel.setForeground(player1Color);
        } else {
            currentPlayerLabel.setText("Current: " + player2Name);
            currentPlayerLabel.setForeground(player2Color);
        }
    }
    
    /**
     * Handles game end scenario - shows winner and offers restart.
     */
    private void handleGameEnd() {
        Player winner = gameEngine.getGameState().getWinner();
        String message;
        
        String player1Name = config.getPlayer1().getName();
        String player2Name = config.getPlayer2().getName();
        
        if (winner == null) {
            message = "Game Over! It's a tie!\n";
        } else {
            String winnerName = (winner == Player.PLAYER_ONE) ? player1Name : player2Name;
            message = String.format("Game Over! %s wins!\n", winnerName);
        }
        
        message += String.format("%s: %d points\n%s: %d points\n\nStart a new game?",
            player1Name, gameEngine.getScore(Player.PLAYER_ONE),
            player2Name, gameEngine.getScore(Player.PLAYER_TWO));
        
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
