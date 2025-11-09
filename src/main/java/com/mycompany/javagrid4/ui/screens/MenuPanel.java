package com.mycompany.javagrid4.ui.screens;

import com.mycompany.javagrid4.audio.SoundManager;
import com.mycompany.javagrid4.models.GameConfig;
import com.mycompany.javagrid4.ui.components.ColorPickerButton;
import com.mycompany.javagrid4.ui.components.BoardSizeCard;
import com.mycompany.javagrid4.ui.components.SoundControlPanel;
import com.mycompany.javagrid4.ui.dialogs.HelpDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.imageio.ImageIO;

/**
 * Menu screen for player setup and game configuration.
 * Features:
 * - Player name input (max 10 characters)
 * - Color picker for each player
 * - Board size selection (3×3, 5×5, 7×7)
 * - Real-time form validation
 * - Start game button (enabled when valid)
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class MenuPanel extends JPanel {
    
    private final PropertyChangeSupport propertyChangeSupport;
    private final GameConfig gameConfig;
    
    // Player 1 components
    private JTextField player1NameField;
    private ColorPickerButton player1ColorButton;
    private JLabel player1CharCountLabel;
    
    // Player 2 components
    private JTextField player2NameField;
    private ColorPickerButton player2ColorButton;
    private JLabel player2CharCountLabel;
    
    // Board size components
    private BoardSizeCard card3x3;
    private BoardSizeCard card5x5;
    private BoardSizeCard card7x7;
    
    // Action button
    private JButton startButton;
    
    /**
     * Creates the menu panel with default configuration.
     */
    public MenuPanel() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.gameConfig = new GameConfig();
        
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));
        
        initComponents();
        layoutComponents();
        attachListeners();
        
        // Initialize character counters
        updatePlayer1Name();
        updatePlayer2Name();
        updateStartButton();
    }
    
    /**
     * Adds a property change listener for screen transitions.
     * Event "startGame" fired with GameConfig when user clicks Start.
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
     * Initializes all UI components.
     */
    private void initComponents() {
        // Player 1 components with enhanced styling
        player1NameField = createStyledTextField(gameConfig.getPlayer1().getName());
        player1ColorButton = new ColorPickerButton(gameConfig.getPlayer1().getColor());
        player1CharCountLabel = new JLabel("0/10");
        player1CharCountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        player1CharCountLabel.setForeground(Color.GRAY);
        
        // Player 2 components with enhanced styling
        player2NameField = createStyledTextField(gameConfig.getPlayer2().getName());
        player2ColorButton = new ColorPickerButton(gameConfig.getPlayer2().getColor());
        player2CharCountLabel = new JLabel("0/10");
        player2CharCountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        player2CharCountLabel.setForeground(Color.GRAY);
        
        // Board size cards
        card3x3 = new BoardSizeCard(3);
        card5x5 = new BoardSizeCard(5);
        card7x7 = new BoardSizeCard(7);
        
        // Set default selection
        card3x3.setSelected(true);
        
        // Start button with beautiful styling
        startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(280, 65));
        startButton.setBackground(new Color(70, 160, 70));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 120, 50), 3, true),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setOpaque(true);
        
        // Add hover effect with smooth color transition
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (startButton.isEnabled()) {
                    startButton.setBackground(new Color(90, 190, 90));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (startButton.isEnabled()) {
                    startButton.setBackground(new Color(70, 160, 70));
                } else {
                    startButton.setBackground(new Color(150, 150, 150));
                }
            }
        });
    }
    
    /**
     * Creates a styled text field with focus effects and placeholder.
     */
    private JTextField createStyledTextField(String initialText) {
        JTextField field = new JTextField(initialText, 15);
        field.setFont(new Font("Arial", Font.PLAIN, 20));
        field.setPreferredSize(new Dimension(220, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Add focus listener for glow effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 150, 255), 3),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Layouts all components using GridBagLayout.
     */
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(getBackground());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 40, 80));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title with subtitle and utilities - add more space from top
        JPanel titlePanel = createTitlePanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        mainPanel.add(titlePanel, gbc);
        
        // Player 1 panel
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 5);
        gbc.weightx = 1.0;
        mainPanel.add(createPlayerPanel("Player 1", player1NameField, player1ColorButton, 
            new Color(255, 100, 100)), gbc);
        
        // Player 2 panel
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 5, 10, 10);
        mainPanel.add(createPlayerPanel("Player 2", player2NameField, player2ColorButton, 
            new Color(100, 150, 255)), gbc);
        
        // Board size panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        mainPanel.add(createBoardSizePanel(), gbc);
        
        // Start button - centered with more space
        gbc.gridy = 3;
        gbc.insets = new Insets(35, 10, 15, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(startButton, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the title panel with main title, subtitle, and utility controls.
     */
    private JPanel createTitlePanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(getBackground());
        
        // Center panel with title and subtitle
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(getBackground());
        
        // Main title
        JLabel titleLabel = new JLabel("JavaGrid4");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(50, 50, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Strategy Grid Game");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(100, 100, 120));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(subtitleLabel);
        
        // Add utility panel to the right
        outerPanel.add(centerPanel, BorderLayout.CENTER);
        outerPanel.add(createUtilityPanel(), BorderLayout.EAST);
        
        return outerPanel;
    }
    
    /**
     * Creates a utility panel with sound controls and help button.
     * Designed with matching sizes and premium styling.
     */
    private JPanel createUtilityPanel() {
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        outerPanel.setBackground(getBackground());
        
        // Container with subtle background and shadow effect
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBackground(new Color(248, 250, 255));
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 245), 2, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Help button with custom icon
        JButton helpBtn = new JButton();
        
        // Load and set custom help icon
        try {
            Image helpImg = ImageIO.read(getClass().getResourceAsStream("/icons/help.png"));
            ImageIcon helpIcon = new ImageIcon(helpImg.getScaledInstance(28, 28, Image.SCALE_SMOOTH));
            helpBtn.setIcon(helpIcon);
        } catch (Exception e) {
            // Fallback to emoji if icon fails to load
            helpBtn.setText("📖");
            helpBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        }
        
        helpBtn.setPreferredSize(new Dimension(45, 45));
        helpBtn.setMinimumSize(new Dimension(45, 45));
        helpBtn.setMaximumSize(new Dimension(45, 45));
        helpBtn.setBackground(new Color(100, 150, 255));
        helpBtn.setForeground(Color.WHITE);
        helpBtn.setFocusPainted(false);
        helpBtn.setBorderPainted(false);
        helpBtn.setContentAreaFilled(false);
        helpBtn.setOpaque(false);
        helpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpBtn.setToolTipText("<html><b>Help & Rules</b><br>View game instructions (F1)</html>");
        
        helpBtn.addActionListener(e -> handleHelp());
        
        // Vertical separator
        JPanel separator = new JPanel();
        separator.setBackground(new Color(200, 210, 230));
        separator.setPreferredSize(new Dimension(2, 45));
        separator.setMaximumSize(new Dimension(2, 45));
        
        // Sound controls with matching height
        SoundControlPanel soundPanel = new SoundControlPanel();
        soundPanel.setupKeyboardShortcut();
        soundPanel.setPreferredSize(new Dimension(180, 45));
        soundPanel.setMaximumSize(new Dimension(180, 45));
        soundPanel.setOpaque(false);
        
        container.add(helpBtn);
        container.add(Box.createHorizontalStrut(10));
        container.add(separator);
        container.add(Box.createHorizontalStrut(10));
        container.add(soundPanel);
        
        outerPanel.add(container);
        
        return outerPanel;
    }
    
    /**
     * Creates a player configuration panel with themed accent color.
     */
    private JPanel createPlayerPanel(String title, JTextField nameField, 
                                     ColorPickerButton colorButton, Color accentColor) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 3, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title label with icon
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(accentColor.darker());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(titleLabel, gbc);
        
        // Name label
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        panel.add(nameLabel, gbc);
        
        // Name field (already styled in initComponents)
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(nameField, gbc);
        
        // Character count label with validation icon
        JLabel charCountLabel = (nameField == player1NameField) ? player1CharCountLabel : player2CharCountLabel;
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.insets = new Insets(2, 8, 8, 8);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(charCountLabel, gbc);
        
        // Color label
        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(colorLabel, gbc);
        
        // Color button - make it bigger and more visible
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(colorButton, gbc);
        
        return panel;
    }
    
    /**
     * Creates the board size selection panel with visual cards.
     */
    private JPanel createBoardSizePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(getBackground());
        
        // Title
        JLabel titleLabel = new JLabel("Choose Board Size");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // Cards panel
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        cardsPanel.setBackground(getBackground());
        cardsPanel.add(card3x3);
        cardsPanel.add(card5x5);
        cardsPanel.add(card7x7);
        
        panel.add(cardsPanel);
        
        return panel;
    }
    
    /**
     * Creates the instructions panel.
     */
    /**
     * Attaches event listeners to all components.
     */
    private void attachListeners() {
        // Player 1 name field listener
        player1NameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePlayer1Name(); }
            public void removeUpdate(DocumentEvent e) { updatePlayer1Name(); }
            public void changedUpdate(DocumentEvent e) { updatePlayer1Name(); }
        });
        
        // Player 2 name field listener
        player2NameField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePlayer2Name(); }
            public void removeUpdate(DocumentEvent e) { updatePlayer2Name(); }
            public void changedUpdate(DocumentEvent e) { updatePlayer2Name(); }
        });
        
        // Color button listeners with validation to prevent same colors
        player1ColorButton.addColorChangeListener(color -> {
            // Check if Player 2 has the same color
            if (color.equals(gameConfig.getPlayer2().getColor())) {
                JOptionPane.showMessageDialog(this,
                    "Both players cannot have the same color!\nPlease choose a different color.",
                    "Color Conflict",
                    JOptionPane.WARNING_MESSAGE);
                // Revert to previous color
                player1ColorButton.setSelectedColor(gameConfig.getPlayer1().getColor());
            } else {
                gameConfig.getPlayer1().setColor(color);
                updateStartButton();
            }
        });
        
        player2ColorButton.addColorChangeListener(color -> {
            // Check if Player 1 has the same color
            if (color.equals(gameConfig.getPlayer1().getColor())) {
                JOptionPane.showMessageDialog(this,
                    "Both players cannot have the same color!\nPlease choose a different color.",
                    "Color Conflict",
                    JOptionPane.WARNING_MESSAGE);
                // Revert to previous color
                player2ColorButton.setSelectedColor(gameConfig.getPlayer2().getColor());
            } else {
                gameConfig.getPlayer2().setColor(color);
                updateStartButton();
            }
        });
        
        // Board size card listeners
        card3x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuPanel.this.selectBoardSize(3);
            }
        });
        
        card5x5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuPanel.this.selectBoardSize(5);
            }
        });
        
        card7x7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuPanel.this.selectBoardSize(7);
            }
        });
        
        // Start button listener
        startButton.addActionListener(e -> handleStartGame());
    }
    
    /**
     * Updates Player 1 name in config and validates.
     */
    private void updatePlayer1Name() {
        String name = player1NameField.getText();
        int length = name.length();
        gameConfig.getPlayer1().setName(name);
        
        // Update character counter with validation icon
        if (length > 10) {
            player1CharCountLabel.setText("❌ " + length + "/10");
            player1CharCountLabel.setForeground(new Color(200, 50, 50));
            player1NameField.setBackground(new Color(255, 235, 235));
        } else if (length == 0) {
            player1CharCountLabel.setText("⚠️ " + length + "/10");
            player1CharCountLabel.setForeground(new Color(200, 150, 0));
            player1NameField.setBackground(new Color(255, 250, 235));
        } else {
            player1CharCountLabel.setText("✓ " + length + "/10");
            player1CharCountLabel.setForeground(new Color(50, 150, 50));
            player1NameField.setBackground(Color.WHITE);
        }
        
        updateStartButton();
    }
    
    /**
     * Updates Player 2 name in config and validates.
     */
    private void updatePlayer2Name() {
        String name = player2NameField.getText();
        int length = name.length();
        gameConfig.getPlayer2().setName(name);
        
        // Update character counter with validation icon
        if (length > 10) {
            player2CharCountLabel.setText("❌ " + length + "/10");
            player2CharCountLabel.setForeground(new Color(200, 50, 50));
            player2NameField.setBackground(new Color(255, 235, 235));
        } else if (length == 0) {
            player2CharCountLabel.setText("⚠️ " + length + "/10");
            player2CharCountLabel.setForeground(new Color(200, 150, 0));
            player2NameField.setBackground(new Color(255, 250, 235));
        } else {
            player2CharCountLabel.setText("✓ " + length + "/10");
            player2CharCountLabel.setForeground(new Color(50, 150, 50));
            player2NameField.setBackground(Color.WHITE);
        }
        
        updateStartButton();
    }
    
    /**
     * Selects a board size and updates UI.
     */
    private void selectBoardSize(int size) {
        // Update cards
        card3x3.setSelected(size == 3);
        card5x5.setSelected(size == 5);
        card7x7.setSelected(size == 7);
        
        // Update config
        gameConfig.setBoardSize(size);
    }
    
    /**
     * Updates the start button enabled state based on validation.
     */
    private void updateStartButton() {
        boolean isValid = gameConfig.isValid();
        startButton.setEnabled(isValid);
        
        // Update button appearance based on state
        if (isValid) {
            startButton.setBackground(new Color(70, 160, 70));
        } else {
            startButton.setBackground(new Color(150, 150, 150));
        }
    }
    
    /**
     * Handles the start game button click.
     * Fires "startGame" property change event with GameConfig.
     */
    private void handleStartGame() {
        if (gameConfig.isValid()) {
            SoundManager.getInstance().playSound(SoundManager.SOUND_BUTTON);
            propertyChangeSupport.firePropertyChange("startGame", null, gameConfig);
        } else {
            SoundManager.getInstance().playSound(SoundManager.SOUND_ERROR);
        }
    }
    
    /**
     * Handles the help button click.
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
}
