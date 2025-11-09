package com.mycompany.javagrid4;

import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.screens.MenuPanel;
import com.mycompany.javagrid4.ui.screens.ResultsPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Entry point for JavaGrid4 - A two-player strategy grid game.
 * Multi-screen application with CardLayout navigation:
 * - Menu Screen: Player setup and configuration
 * - Game Screen: Active gameplay
 * - Results Screen: Winner display and replay options
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class JavaGrid4 extends JFrame {

    private static final String MENU_SCREEN = "MENU";
    private static final String GAME_SCREEN = "GAME";
    private static final String RESULTS_SCREEN = "RESULTS";
    
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private ResultsPanel resultsPanel;
    
    /**
     * Main entry point - launches the JavaGrid4 game window.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Run Swing code on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JavaGrid4 app = new JavaGrid4();
            app.setVisible(true);
        });
    }
    
    /**
     * Constructor - sets up the multi-screen application.
     */
    public JavaGrid4() {
        super("JavaGrid4 - Strategy Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(950, 950));
        
        // Create CardLayout for screen switching
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        // Initialize all screens
        initializeScreens();
        
        // Wire up screen transitions
        setupScreenTransitions();
        
        // Add main container to frame
        add(mainContainer);
        
        // Pack and center window
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Start on menu screen
        showMenuScreen();
    }
    
    /**
     * Initializes all screen panels.
     */
    private void initializeScreens() {
        // Create menu screen
        menuPanel = new MenuPanel();
        mainContainer.add(menuPanel, MENU_SCREEN);
        
        // Create results screen
        resultsPanel = new ResultsPanel();
        mainContainer.add(resultsPanel, RESULTS_SCREEN);
        
        // Game panel will be created dynamically when game starts
        // (because it needs GameConfig from menu)
    }
    
    /**
     * Sets up listeners for screen transitions.
     */
    private void setupScreenTransitions() {
        // Listen for "startGame" event from MenuPanel
        menuPanel.addPropertyChangeListener("startGame", evt -> {
            GameConfig config = (GameConfig) evt.getNewValue();
            startNewGame(config);
        });
        
        // Listen for "playAgain" event from ResultsPanel
        resultsPanel.addPropertyChangeListener("playAgain", evt -> {
            GameConfig config = (GameConfig) evt.getNewValue();
            startNewGame(config);
        });
        
        // Listen for "backToMenu" event from ResultsPanel
        resultsPanel.addPropertyChangeListener("backToMenu", evt -> {
            showMenuScreen();
        });
    }
    
    /**
     * Shows the menu screen.
     */
    private void showMenuScreen() {
        cardLayout.show(mainContainer, MENU_SCREEN);
        setTitle("JavaGrid4 - Menu");
    }
    
    /**
     * Starts a new game with the given configuration.
     * @param config Game configuration from menu
     */
    private void startNewGame(GameConfig config) {
        // Create new game panel with configuration
        gamePanel = new GamePanel(config);
        
        // Remove old game panel if exists
        Component[] components = mainContainer.getComponents();
        for (Component comp : components) {
            if (comp instanceof GamePanel) {
                mainContainer.remove(comp);
            }
        }
        
        // Add new game panel
        mainContainer.add(gamePanel, GAME_SCREEN);
        
        // Listen for game end event
        gamePanel.addPropertyChangeListener("gameEnded", evt -> {
            Object[] results = (Object[]) evt.getNewValue();
            showResults((GameConfig) results[0], (Player) results[1], 
                       (Integer) results[2], (Integer) results[3]);
        });
        
        // Switch to game screen
        cardLayout.show(mainContainer, GAME_SCREEN);
        setTitle("JavaGrid4 - Game in Progress");
    }
    
    /**
     * Shows the results screen with game outcome.
     * @param config Game configuration
     * @param winner Winning player (or null for tie)
     * @param player1Score Player 1's final score
     * @param player2Score Player 2's final score
     */
    private void showResults(GameConfig config, Player winner, int player1Score, int player2Score) {
        resultsPanel.setResults(config, winner, player1Score, player2Score);
        cardLayout.show(mainContainer, RESULTS_SCREEN);
        setTitle("JavaGrid4 - Results");
    }
}
