package com.mycompany.javagrid4;

import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.screens.MenuPanel;
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
    // ResultsPanel will be added later
    
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
        
        // Game panel will be created dynamically when game starts
        // (because it needs GameConfig from menu)
        
        // Results panel will be added in next phase
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
        
        // Game panel transitions will be added after we create it
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
        gamePanel = new GamePanel();
        
        // Remove old game panel if exists
        Component[] components = mainContainer.getComponents();
        for (Component comp : components) {
            if (comp instanceof GamePanel) {
                mainContainer.remove(comp);
            }
        }
        
        // Add new game panel
        mainContainer.add(gamePanel, GAME_SCREEN);
        
        // TODO: Apply config to game panel (player names, colors, board size)
        // This will be implemented when we update GamePanel
        
        // Switch to game screen
        cardLayout.show(mainContainer, GAME_SCREEN);
        setTitle("JavaGrid4 - Game in Progress");
    }
}
