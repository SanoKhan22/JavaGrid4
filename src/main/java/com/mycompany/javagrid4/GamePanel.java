package com.mycompany.javagrid4;

import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.components.CustomGridCell;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Main GUI controller for JavaGrid4.
 * Handles all visual components, layout, and user interactions.
 * Delegates game logic to GameEngine.
 * 
 * Keyboard shortcuts:
 * - ESC: Return to menu (with confirmation)
 * - R: Restart game (with confirmation)
 * - P: Pause/Resume game
 */
public class GamePanel extends JPanel {
    private final PropertyChangeSupport propertyChangeSupport;
    private GameEngine gameEngine;
    private CustomGridCell[][] gridCells;
    private GameConfig config;
    private boolean isPaused;
    
    // GUI Components
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel controlPanel;
    
    private JLabel titleLabel;
    private JLabel currentPlayerLabel;
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel pauseOverlayLabel;
    
    private JButton restartButton;
    private JButton pauseButton;
    private JButton menuButton;
    
    /**
     * Creates a new GamePanel with game configuration.
     * @param config Game configuration with player names, colors, and board size
     */
    public GamePanel(GameConfig config) {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.config = config;
        this.gameEngine = new GameEngine(config.getBoardSize());
        this.isPaused = false;
        
        initializeComponents();
        setupLayout();
        createGrid(config.getBoardSize());
        setupKeyboardShortcuts();
        updateDisplay();
    }
    
    /**
     * Adds a property change listener for screen transitions.
     * Event "gameEnded" fired with results array when game ends.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Adds a property change listener for a specific property.
     * @param propertyName Name of the property to listen for
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
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
        
        // Control panel with game flow buttons
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(240, 240, 245));
        
        // Restart button
        restartButton = createControlButton("Restart (R)", new Color(220, 100, 100));
        restartButton.addActionListener(e -> handleRestart());
        restartButton.setToolTipText("Restart the game (R)");
        
        // Pause button
        pauseButton = createControlButton("Pause (P)", new Color(200, 150, 70));
        pauseButton.addActionListener(e -> handlePause());
        pauseButton.setToolTipText("Pause/Resume game (P)");
        
        // Menu button
        menuButton = createControlButton("Menu (ESC)", new Color(100, 130, 200));
        menuButton.addActionListener(e -> handleBackToMenu());
        menuButton.setToolTipText("Back to menu (ESC)");
        
        controlPanel.add(restartButton);
        controlPanel.add(pauseButton);
        controlPanel.add(menuButton);
        
        // Pause overlay label (initially hidden)
        pauseOverlayLabel = new JLabel("PAUSED - Press P to Resume");
        pauseOverlayLabel.setFont(new Font("Arial", Font.BOLD, 36));
        pauseOverlayLabel.setForeground(new Color(255, 100, 100));
        pauseOverlayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pauseOverlayLabel.setOpaque(true);
        pauseOverlayLabel.setBackground(new Color(0, 0, 0, 180));
        pauseOverlayLabel.setVisible(false);
    }
    
    /**
     * Creates a styled control button.
     */
    private JButton createControlButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Sets up the main layout structure.
     */
    private void setupLayout() {
        // Main content area
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Combine bottom panel and control panel
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(bottomPanel, BorderLayout.NORTH);
        bottomContainer.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomContainer, BorderLayout.SOUTH);
        
        // Use layered pane to overlay pause screen
        setLayout(new OverlayLayout(this));
        add(pauseOverlayLabel);
        add(mainPanel);
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
     * Handles game end scenario - transitions to results screen.
     */
    private void handleGameEnd() {
        Player winner = gameEngine.getGameState().getWinner();
        int player1Score = gameEngine.getScore(Player.PLAYER_ONE);
        int player2Score = gameEngine.getScore(Player.PLAYER_TWO);
        
        // Fire event with results: [config, winner, player1Score, player2Score]
        Object[] results = new Object[] { config, winner, player1Score, player2Score };
        propertyChangeSupport.firePropertyChange("gameEnded", null, results);
    }
    
    /**
     * Sets up keyboard shortcuts for game controls.
     */
    private void setupKeyboardShortcuts() {
        // Get input and action maps
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // ESC - Back to Menu
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "backToMenu");
        actionMap.put("backToMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBackToMenu();
            }
        });
        
        // R - Restart Game
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");
        actionMap.put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRestart();
            }
        });
        
        // P - Pause/Resume
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePause();
            }
        });
    }
    
    /**
     * Handles restart button/shortcut.
     * Shows confirmation dialog before restarting.
     */
    private void handleRestart() {
        if (isPaused) {
            return; // Don't allow restart while paused
        }
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to restart the game?\nAll progress will be lost.",
            "Restart Game",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            gameEngine.resetBoard();
            syncGridWithEngine();
            updateDisplay();
        }
    }
    
    /**
     * Handles pause button/shortcut.
     * Toggles game pause state.
     */
    private void handlePause() {
        if (gameEngine.isGameOver()) {
            return; // Can't pause if game is over
        }
        
        isPaused = !isPaused;
        
        if (isPaused) {
            pauseButton.setText("Resume (P)");
            pauseButton.setBackground(new Color(70, 160, 70));
            pauseOverlayLabel.setVisible(true);
            
            // Disable grid interaction
            for (int row = 0; row < gridCells.length; row++) {
                for (int col = 0; col < gridCells[row].length; col++) {
                    gridCells[row][col].setEnabled(false);
                }
            }
        } else {
            pauseButton.setText("Pause (P)");
            pauseButton.setBackground(new Color(200, 150, 70));
            pauseOverlayLabel.setVisible(false);
            
            // Re-enable grid interaction
            for (int row = 0; row < gridCells.length; row++) {
                for (int col = 0; col < gridCells[row].length; col++) {
                    gridCells[row][col].setEnabled(true);
                }
            }
        }
        
        repaint();
    }
    
    /**
     * Handles back to menu button/shortcut.
     * Shows confirmation dialog before exiting to menu.
     */
    private void handleBackToMenu() {
        if (gameEngine.isGameOver()) {
            // If game is over, just go back without confirmation
            propertyChangeSupport.firePropertyChange("backToMenu", null, null);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to return to the menu?\nAll progress will be lost.",
            "Back to Menu",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            propertyChangeSupport.firePropertyChange("backToMenu", null, null);
        }
    }
}
