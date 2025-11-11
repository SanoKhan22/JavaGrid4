package com.mycompany.javagrid4;

import com.mycompany.javagrid4.audio.SoundManager;
import com.mycompany.javagrid4.commands.CommandHistory;
import com.mycompany.javagrid4.commands.MoveCommand;
import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.components.CustomGridCell;
import com.mycompany.javagrid4.ui.components.ScoreCard;
import com.mycompany.javagrid4.ui.components.ControlCard;
import com.mycompany.javagrid4.ui.components.TurnIndicator;
import com.mycompany.javagrid4.ui.components.PauseOverlay;
import com.mycompany.javagrid4.ui.components.GridContainer;
import com.mycompany.javagrid4.ui.components.GameTimer;
import com.mycompany.javagrid4.ui.effects.ScoreIncrementAnimation;
import com.mycompany.javagrid4.ui.effects.GameOverOverlay;
import com.mycompany.javagrid4.ui.dialogs.HelpDialog;
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
 * - Ctrl+Z: Undo last move
 * - Ctrl+Y: Redo last undone move
 */
public class GamePanel extends JPanel {
    private final PropertyChangeSupport propertyChangeSupport;
    private GameEngine gameEngine;
    private CustomGridCell[][] gridCells;
    private GameConfig config;
    private boolean isPaused;
    private CommandHistory commandHistory;
    private GameTimer gameTimer;
    private boolean gameStarted;
    
    // GUI Components
    private JPanel topPanel;
    private JPanel centerPanel;
    private GridContainer gridContainer;
    private JPanel bottomPanel;
    private JPanel controlPanel;
    
    private JLabel titleLabel;
    private JLabel currentPlayerLabel; // Legacy (kept for compatibility)
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel pauseOverlayLabel; // Legacy (kept for compatibility)
    
    // Material Design components
    private TurnIndicator turnIndicator;
    private PauseOverlay pauseOverlay;
    private ScoreCard player1Card;
    private ScoreCard currentTurnCard;
    private ScoreCard player2Card;
    
    // Material Design Control Cards
    private ControlCard restartCard;
    private ControlCard undoCard;
    private ControlCard redoCard;
    private ControlCard helpCard;
    private ControlCard menuCard;
    private ControlCard pauseCard;
    
    // Legacy buttons (kept for compatibility)
    private JButton restartButton;
    private JButton pauseButton;
    private JButton menuButton;
    private JButton undoButton;
    private JButton redoButton;
    
    /**
     * Creates a new GamePanel with game configuration.
     * @param config Game configuration with player names, colors, and board size
     */
    public GamePanel(GameConfig config) {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.config = config;
        this.gameEngine = new GameEngine(config.getBoardSize());
        this.commandHistory = new CommandHistory();
        this.isPaused = false;
        this.gameTimer = new GameTimer();
        this.gameStarted = false;
        
        // Enable glassmorphism background
        setOpaque(false);
        
        initializeComponents();
        setupLayout();
        createGrid(config.getBoardSize());
        setupKeyboardShortcuts();
        updateDisplay();
    }
    
    /**
     * Custom paint component for Material Design gradient background.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing and high-quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Create diagonal gradient from light blue-gray to light purple-gray
        Color topLeft = new Color(235, 242, 250);      // Light blue-gray
        Color bottomRight = new Color(242, 237, 250);  // Light purple-gray
        
        GradientPaint gradient = new GradientPaint(
            0, 0, topLeft,
            width, height, bottomRight
        );
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
        // Add subtle texture overlay with dots pattern
        g2d.setColor(new Color(255, 255, 255, 15)); // Very subtle white
        for (int y = 0; y < height; y += 20) {
            for (int x = 0; x < width; x += 20) {
                g2d.fillOval(x, y, 2, 2);
            }
        }
        
        g2d.dispose();
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
        topPanel.setOpaque(false); // Transparent for glassmorphism
        
        // Timer panel at the top
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setOpaque(false);
        timerPanel.add(gameTimer);
        
        titleLabel = new JLabel("JavaGrid4");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(String.format("%dx%d Board", 
            config.getBoardSize(), config.getBoardSize()));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(timerPanel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(subtitleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        
        // Center panel for grid with Material Design container
        centerPanel = new JPanel();
        centerPanel.setOpaque(false); // Transparent for glassmorphism
        
        // Wrap grid in Material Design container card
        gridContainer = new GridContainer();
        gridContainer.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel with Material Design score cards
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setOpaque(false);
        
        // Create score cards
        player1Card = new ScoreCard(
            config.getPlayer1().getName().toUpperCase(),
            "Score: 0",
            config.getPlayer1().getColor(),
            false
        );
        
        // Create animated turn indicator (replaces currentTurnCard)
        turnIndicator = new TurnIndicator(
            config.getPlayer1().getName(),
            config.getPlayer1().getColor()
        );
        
        // Keep old currentTurnCard for compatibility (hidden)
        currentTurnCard = new ScoreCard(
            "CURRENT TURN",
            config.getPlayer1().getName(),
            config.getPlayer1().getColor(),
            true // Active/elevated
        );
        currentTurnCard.setVisible(false);
        
        player2Card = new ScoreCard(
            config.getPlayer2().getName().toUpperCase(),
            "Score: 0",
            config.getPlayer2().getColor(),
            false
        );
        
        bottomPanel.add(player1Card);
        bottomPanel.add(turnIndicator); // Use new animated turn indicator
        bottomPanel.add(player2Card);
        
        // Keep old labels for compatibility (hidden)
        player1ScoreLabel = new JLabel(config.getPlayer1().getName() + ": 0", SwingConstants.CENTER);
        player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        player1ScoreLabel.setForeground(config.getPlayer1().getColor());
        player1ScoreLabel.setVisible(false);
        
        currentPlayerLabel = new JLabel("Current: " + config.getPlayer1().getName(), SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentPlayerLabel.setForeground(config.getPlayer1().getColor());
        currentPlayerLabel.setVisible(false);
        
        player2ScoreLabel = new JLabel(config.getPlayer2().getName() + ": 0", SwingConstants.CENTER);
        player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        player2ScoreLabel.setForeground(config.getPlayer2().getColor());
        player2ScoreLabel.setVisible(false);
        
        // Control panel with game flow buttons
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setOpaque(false); // Transparent for glassmorphism
        
        // Create Material Design control cards
        restartCard = new ControlCard("Restart", "↻", new Color(220, 100, 100), this::handleRestart);
        undoCard = new ControlCard("Undo", "⟲", new Color(100, 100, 120), this::handleUndo);
        redoCard = new ControlCard("Redo", "⟳", new Color(100, 100, 120), this::handleRedo);
        menuCard = new ControlCard("Menu", "≡", new Color(100, 130, 200), this::handleBackToMenu);
        pauseCard = new ControlCard("Pause", "⏸", new Color(150, 100, 200), this::handlePause);
        
        // Initial states
        undoCard.setEnabled(false);
        redoCard.setEnabled(false);
        
        // Add cards to control panel
        controlPanel.add(undoCard);
        controlPanel.add(redoCard);
        controlPanel.add(restartCard);
        controlPanel.add(pauseCard);
        controlPanel.add(menuCard);
        
        // Legacy buttons (kept hidden for compatibility)
        restartButton = createControlButton("Restart (R)", new Color(220, 100, 100));
        restartButton.addActionListener(e -> handleRestart());
        restartButton.setToolTipText("Restart the game (R)");
        restartButton.setVisible(false);
        
        pauseButton = createControlButton("Pause (P)", new Color(200, 150, 70));
        pauseButton.addActionListener(e -> handlePause());
        pauseButton.setToolTipText("Pause/Resume game (P)");
        pauseButton.setVisible(false);
        
        menuButton = createControlButton("Menu (ESC)", new Color(100, 130, 200));
        menuButton.addActionListener(e -> handleBackToMenu());
        menuButton.setToolTipText("Back to menu (ESC)");
        menuButton.setVisible(false);
        
        undoButton = createIconButton("undo.png", "Undo (Ctrl+Z)");
        undoButton.addActionListener(e -> handleUndo());
        undoButton.setEnabled(false);
        undoButton.setVisible(false);
        
        redoButton = createIconButton("redo.png", "Redo (Ctrl+Y)");
        redoButton.addActionListener(e -> handleRedo());
        redoButton.setEnabled(false);
        redoButton.setVisible(false);
        
        // Create optimized pause overlay with click-to-resume
        pauseOverlay = new PauseOverlay();
        pauseOverlay.setOnResumeCallback(() -> handlePause());
        
        // Legacy pause overlay label (kept hidden for compatibility)
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
     * Creates an icon-based button for undo/redo actions.
     * @param iconName Name of the icon file (e.g., "undo.png")
     * @param tooltip Tooltip text for the button
     * @return Styled icon button
     */
    private JButton createIconButton(String iconName, String tooltip) {
        JButton button = new JButton();
        
        // Load icon
        try {
            java.io.InputStream iconStream = getClass().getResourceAsStream("/icons/" + iconName);
            if (iconStream != null) {
                java.awt.image.BufferedImage iconImage = javax.imageio.ImageIO.read(iconStream);
                java.awt.Image scaledImage = iconImage.getScaledInstance(28, 28, java.awt.Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback to text if icon not found
                button.setText(iconName.contains("undo") ? "↶" : "↷");
                button.setFont(new Font("Arial", Font.BOLD, 20));
            }
        } catch (Exception e) {
            // Fallback to text emoji
            button.setText(iconName.contains("undo") ? "↶" : "↷");
            button.setFont(new Font("Arial", Font.BOLD, 20));
        }
        
        // Button styling
        Color bgColor = iconName.contains("undo") ? new Color(100, 150, 200) : new Color(100, 180, 150);
        button.setPreferredSize(new Dimension(50, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        
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
        mainPanel.add(gridContainer, BorderLayout.CENTER); // Use Material Design container
        
        // Combine bottom panel and control panel
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(bottomPanel, BorderLayout.NORTH);
        bottomContainer.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomContainer, BorderLayout.SOUTH);
        
        // Use layered pane to overlay pause screen and modal
        setLayout(new OverlayLayout(this));
        add(pauseOverlay); // Material Design pause overlay
        add(pauseOverlayLabel); // Legacy (kept for compatibility)
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
        if (gameEngine.isGameOver() || isPaused) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
            return;
        }
        
        int row = clickedCell.getRow();
        int col = clickedCell.getColumn();
        
        // Check if the clicked cell is already at maximum value (4)
        if (gameEngine.getCellValue(row, col) >= 4) {
            // Invalid move - cell is already maxed out
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
            
            // Visual feedback: brief flash to show it's not clickable
            clickedCell.setEnabled(false);
            Timer flashTimer = new Timer(100, e -> clickedCell.setEnabled(true));
            flashTimer.setRepeats(false);
            flashTimer.start();
            
            return; // Don't change turn, don't process move
        }
        
        // Start timer on first move
        if (!gameStarted) {
            gameTimer.start();
            gameStarted = true;
        }
        
        Player currentPlayer = gameEngine.getGameState().getCurrentPlayer();
        
        // Play click sound
        SoundManager.getInstance().playSound(SoundManager.SOUND_CLICK);
        
        // Capture cell values BEFORE the move
        int gridSize = gameEngine.getGridSize();
        int[] valuesBefore = new int[5]; // clicked cell + 4 neighbors
        valuesBefore[0] = gameEngine.getCellValue(row, col);
        valuesBefore[1] = row > 0 ? gameEngine.getCellValue(row - 1, col) : -1;
        valuesBefore[2] = row < gridSize - 1 ? gameEngine.getCellValue(row + 1, col) : -1;
        valuesBefore[3] = col > 0 ? gameEngine.getCellValue(row, col - 1) : -1;
        valuesBefore[4] = col < gridSize - 1 ? gameEngine.getCellValue(row, col + 1) : -1;
        
        // Create and execute move command (supports undo/redo)
        MoveCommand moveCommand = new MoveCommand(gameEngine, row, col, currentPlayer);
        commandHistory.executeCommand(moveCommand);
        
        // Check if any cells were JUST claimed (changed from <4 to 4)
        boolean cellsClaimed = checkForNewlyClaimedCells(row, col, valuesBefore);
        if (cellsClaimed) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_CLAIM);
        }
        
        // Switch player
        gameEngine.getGameState().switchPlayer();
        
        // Update all cell displays
        syncGridWithEngine();
        
        // Update score and player display
        updateDisplay();
        
        // Update undo/redo button states
        updateUndoRedoButtons();
        
        // Check if game ended
        if (gameEngine.isGameOver()) {
            handleGameEnd();
        }
    }
    
    /**
     * Checks if any cells were claimed by the move.
     * @param row Move row
     * @param col Move column
     * @return true if cells reached value 4
     */
    /**
     * Checks if any cells were NEWLY claimed (just reached value 4) and triggers animations.
     * Only animates cells that changed from <4 to 4, not cells already at 4.
     * @param row The row of the clicked cell
     * @param col The column of the clicked cell
     * @param valuesBefore Array of values before the move [clicked, top, bottom, left, right]
     * @return true if any cells were newly claimed
     */
    private boolean checkForNewlyClaimedCells(int row, int col, int[] valuesBefore) {
        int gridSize = gameEngine.getGridSize();
        boolean anyNewlyClaimed = false;
        
        // Check clicked cell - animate only if it JUST reached 4
        int currentValue = gameEngine.getCellValue(row, col);
        if (currentValue == 4 && valuesBefore[0] < 4) {
            gridCells[row][col].playClaimAnimation();
            // No flying animation - instant score update
            anyNewlyClaimed = true;
        }
        
        // Check top neighbor
        if (row > 0) {
            currentValue = gameEngine.getCellValue(row - 1, col);
            if (currentValue == 4 && valuesBefore[1] < 4) {
                gridCells[row - 1][col].playClaimAnimation();
                // No flying animation - instant score update
                anyNewlyClaimed = true;
            }
        }
        
        // Check bottom neighbor
        if (row < gridSize - 1) {
            currentValue = gameEngine.getCellValue(row + 1, col);
            if (currentValue == 4 && valuesBefore[2] < 4) {
                gridCells[row + 1][col].playClaimAnimation();
                // No flying animation - instant score update
                anyNewlyClaimed = true;
            }
        }
        
        // Check left neighbor
        if (col > 0) {
            currentValue = gameEngine.getCellValue(row, col - 1);
            if (currentValue == 4 && valuesBefore[3] < 4) {
                gridCells[row][col - 1].playClaimAnimation();
                // No flying animation - instant score update
                anyNewlyClaimed = true;
            }
        }
        
        // Check right neighbor
        if (col < gridSize - 1) {
            currentValue = gameEngine.getCellValue(row, col + 1);
            if (currentValue == 4 && valuesBefore[4] < 4) {
                gridCells[row][col + 1].playClaimAnimation();
                // No flying animation - instant score update
                anyNewlyClaimed = true;
            }
        }
        
        return anyNewlyClaimed;
    }
    
    /**
     * Animates a score increment flying from a cell to the player's score label.
     * @param cell The cell that was claimed
     * @param player The player who claimed it
     * @param points Number of points scored (usually 1)
     */
    private void animateScoreIncrement(CustomGridCell cell, Player player, int points) {
        // Get cell center position in GamePanel coordinates
        Point cellCenter = new Point(
            cell.getX() + cell.getWidth() / 2,
            cell.getY() + cell.getHeight() / 2
        );
        
        // Convert to absolute position within centerPanel
        Component parent = cell.getParent();
        while (parent != null && parent != centerPanel) {
            cellCenter.x += parent.getX();
            cellCenter.y += parent.getY();
            parent = parent.getParent();
        }
        
        // Get score label position
        JLabel scoreLabel = (player == Player.PLAYER_ONE) ? 
                            player1ScoreLabel : player2ScoreLabel;
        
        Point scorePos = new Point(
            scoreLabel.getX() + scoreLabel.getWidth() / 2,
            scoreLabel.getY() + scoreLabel.getHeight() / 2
        );
        
        // Convert score label position to same coordinate space
        parent = scoreLabel.getParent();
        while (parent != null && parent != this) {
            scorePos.x += parent.getX();
            scorePos.y += parent.getY();
            parent = parent.getParent();
        }
        
        // Get player color
        Color playerColor = (player == Player.PLAYER_ONE) ? 
                            config.getPlayer1().getColor() : 
                            config.getPlayer2().getColor();
        
        // Create and start animation
        ScoreIncrementAnimation animation = new ScoreIncrementAnimation(
            cellCenter, scorePos, points, playerColor
        );
        
        // Add to this panel (GamePanel) so it's visible over everything
        add(animation);
        setComponentZOrder(animation, 0); // Bring to front
        animation.start();
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
        
        // Calculate max score based on board size (rough estimate: boardSize * boardSize * 3)
        int boardSize = config.getBoardSize();
        int estimatedMaxScore = boardSize * boardSize * 3;
        
        // Get player names and colors from config
        String player1Name = config.getPlayer1().getName();
        String player2Name = config.getPlayer2().getName();
        Color player1Color = config.getPlayer1().getColor();
        Color player2Color = config.getPlayer2().getColor();
        
        // Update Material Design score cards with progress bars
        player1Card.setValue("Score: " + player1Score);
        player1Card.updateScore(player1Score, estimatedMaxScore);
        
        player2Card.setValue("Score: " + player2Score);
        player2Card.updateScore(player2Score, estimatedMaxScore);
        
        // Update current turn card
        if (currentPlayer == Player.PLAYER_ONE) {
            currentTurnCard.setValue(player1Name);
            currentTurnCard.setAccentColor(player1Color);
        } else {
            currentTurnCard.setValue(player2Name);
            currentTurnCard.setAccentColor(player2Color);
        }
        
        // Update old labels (for compatibility, hidden)
        player1ScoreLabel.setText(String.format("%s: %d", player1Name, player1Score));
        player1ScoreLabel.setForeground(player1Color);
        
        player2ScoreLabel.setText(String.format("%s: %d", player2Name, player2Score));
        player2ScoreLabel.setForeground(player2Color);
        
        // Update current player display
        if (currentPlayer == Player.PLAYER_ONE) {
            // Update animated turn indicator
            turnIndicator.updatePlayer(player1Name, player1Color);
            
            // Update legacy label (kept for compatibility)
            currentPlayerLabel.setText("Current: " + player1Name);
            currentPlayerLabel.setForeground(player1Color);
        } else {
            // Update animated turn indicator
            turnIndicator.updatePlayer(player2Name, player2Color);
            
            // Update legacy label (kept for compatibility)
            currentPlayerLabel.setText("Current: " + player2Name);
            currentPlayerLabel.setForeground(player2Color);
        }
    }
    
    /**
     * Handles game end scenario - shows overlay then transitions to results screen.
     */
    private void handleGameEnd() {
        // Stop the timer and get elapsed time
        gameTimer.stop();
        int elapsedSeconds = gameTimer.getElapsedSeconds();
        
        // Play victory sound
        SoundManager.getInstance().playSound(SoundManager.SOUND_VICTORY);
        
        Player winner = gameEngine.getGameState().getWinner();
        int player1Score = gameEngine.getScore(Player.PLAYER_ONE);
        int player2Score = gameEngine.getScore(Player.PLAYER_TWO);
        
        // Create game over overlay with 2 second display time
        GameOverOverlay overlay = new GameOverOverlay(
            winner,
            config.getPlayer1().getName(),
            config.getPlayer2().getName(),
            player1Score,
            player2Score,
            config.getPlayer1().getColor(),
            config.getPlayer2().getColor(),
            () -> {
                // After overlay completes, transition to results with elapsed time
                Object[] results = new Object[] { config, winner, player1Score, player2Score, elapsedSeconds };
                propertyChangeSupport.firePropertyChange("gameEnded", null, results);
            }
        );
        
        // Add overlay to cover entire panel
        overlay.setBounds(0, 0, getWidth(), getHeight());
        add(overlay);
        setComponentZOrder(overlay, 0); // Bring to front
        
        // Start the overlay animation
        overlay.start();
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
        
        // Ctrl+Z - Undo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
        actionMap.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUndo();
            }
        });
        
        // Ctrl+Y - Redo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
        actionMap.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRedo();
            }
        });
        
        // F1 - Help Dialog
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "help");
        actionMap.put("help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleHelp();
            }
        });
    }
    
    /**
     * Handles restart button/shortcut.
     * Shows confirmation dialog before restarting.
     */
    private void handleRestart() {
        if (isPaused) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
            return; // Don't allow restart while paused
        }
        
        SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        
        int choice = com.mycompany.javagrid4.ui.dialogs.SimpleDialog.showConfirmDialog(
            this,
            "Are you sure you want to restart the game? All progress will be lost.",
            "Restart Game",
            "Restart",
            "Cancel"
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            gameEngine.resetBoard();
            commandHistory.clear(); // Clear undo/redo history
            gameTimer.reset();
            gameStarted = false;
            syncGridWithEngine();
            updateDisplay();
            updateUndoRedoButtons();
        }
    }
    
    /**
     * Handles undo button/shortcut.
     * Undoes the last move if available.
     */
    private void handleUndo() {
        if (isPaused || gameEngine.isGameOver()) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
            return; // Can't undo while paused or game over
        }
        
        if (commandHistory.undo()) {
            // Play button sound
            SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
            
            // Switch player back
            gameEngine.getGameState().switchPlayer();
            
            // Update display
            syncGridWithEngine();
            updateDisplay();
            updateUndoRedoButtons();
        } else {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
        }
    }
    
    /**
     * Handles redo button/shortcut.
     * Redoes the last undone move if available.
     */
    private void handleRedo() {
        if (isPaused || gameEngine.isGameOver()) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
            return; // Can't redo while paused or game over
        }
        
        if (commandHistory.redo()) {
            // Play button sound
            SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
            
            // Switch player forward
            gameEngine.getGameState().switchPlayer();
            
            // Update display
            syncGridWithEngine();
            updateDisplay();
            updateUndoRedoButtons();
        } else {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
        }
    }
    
    /**
     * Updates the enabled state of undo/redo buttons.
     */
    private void updateUndoRedoButtons() {
        boolean canUndo = commandHistory.canUndo() && !gameEngine.isGameOver();
        boolean canRedo = commandHistory.canRedo() && !gameEngine.isGameOver();
        
        // Update Material Design cards
        undoCard.setEnabled(canUndo);
        redoCard.setEnabled(canRedo);
        
        // Update legacy buttons (kept for compatibility)
        undoButton.setEnabled(canUndo);
        redoButton.setEnabled(canRedo);
    }
    
    /**
     * Handles pause button/shortcut.
     * Toggles game pause state.
     */
    private void handlePause() {
        if (gameEngine.isGameOver()) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
            return; // Can't pause if game is over
        }
        
        SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        isPaused = !isPaused;
        
        if (isPaused) {
            gameTimer.pause();
            
            // Update Material Design pause card
            pauseCard.setIcon("▶");
            
            // Update legacy button (kept for compatibility)
            pauseButton.setText("Resume (P)");
            pauseButton.setBackground(new Color(70, 160, 70));
            
            // Show Material Design pause overlay with animation
            pauseOverlay.showOverlay();
            
            // Legacy overlay (kept hidden for compatibility)
            pauseOverlayLabel.setVisible(false);
            
            // Disable grid interaction
            for (int row = 0; row < gridCells.length; row++) {
                for (int col = 0; col < gridCells[row].length; col++) {
                    gridCells[row][col].setEnabled(false);
                }
            }
        } else {
            gameTimer.resume();
            
            // Update Material Design pause card
            pauseCard.setIcon("⏸");
            
            // Update legacy button (kept for compatibility)
            pauseButton.setText("Pause (P)");
            pauseButton.setBackground(new Color(200, 150, 70));
            
            // Hide Material Design pause overlay with animation
            pauseOverlay.hideOverlay();
            
            // Legacy overlay (kept hidden for compatibility)
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
     * Handles help dialog shortcut (F1).
     * Opens the help/rules dialog.
     */
    private void handleHelp() {
        SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        
        // Get the parent frame for the dialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        
        // Create and show help dialog
        HelpDialog helpDialog = new HelpDialog(parentFrame);
        helpDialog.setVisible(true);
    }
    
    /**
     * Handles back to menu button/shortcut.
     * Shows confirmation dialog before exiting to menu.
     */
    private void handleBackToMenu() {
        SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
        
        if (gameEngine.isGameOver()) {
            // If game is over, just go back without confirmation
            propertyChangeSupport.firePropertyChange("backToMenu", null, null);
            return;
        }
        
        int choice = com.mycompany.javagrid4.ui.dialogs.SimpleDialog.showConfirmDialog(
            this,
            "Are you sure you want to return to the menu? All progress will be lost.",
            "Back to Menu",
            "Leave Game",
            "Stay"
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            propertyChangeSupport.firePropertyChange("backToMenu", null, null);
        }
    }
}
