package com.mycompany.javagrid4.ui.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Help/Rules dialog that provides comprehensive game instructions.
 * Shows game objective, rules, controls, keyboard shortcuts, and strategy tips.
 */
public class HelpDialog extends JDialog {
    private static final Color HEADER_COLOR = new Color(50, 50, 100);
    private static final Color TEXT_COLOR = new Color(60, 60, 80);
    private static final Color ACCENT_COLOR = new Color(100, 150, 255);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    
    private JTabbedPane tabbedPane;
    
    public HelpDialog(Frame parent) {
        super(parent, "JavaGrid4 - Help & Rules", true);
        initializeDialog();
        createContent();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeDialog() {
        setSize(700, 350);  // Compact height
        setResizable(true);  // Allow user to resize if needed
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Set minimum size to prevent too small
        setMinimumSize(new Dimension(600, 300));
    }
    
    private void createContent() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Tabbed content
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 13));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        
        tabbedPane.addTab("📖 Game Rules", createRulesPanel());
        tabbedPane.addTab("🎮 How to Play", createHowToPlayPanel());
        tabbedPane.addTab("⌨️ Controls", createControlsPanel());
        tabbedPane.addTab("💡 Strategy Tips", createStrategyPanel());
        tabbedPane.addTab("ℹ️ About", createAboutPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer with close button
        add(createFooter(), BorderLayout.SOUTH);
        
        // Add ESC key to close dialog
        setupKeyboardShortcuts();
    }
    
    /**
     * Sets up keyboard shortcuts for the dialog.
     */
    private void setupKeyboardShortcuts() {
        // ESC key to close
        getRootPane().registerKeyboardAction(
            e -> dispose(),
            KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));  // Reduced padding
        
        JLabel titleLabel = new JLabel("JavaGrid4 - Help & Rules");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));  // Smaller font
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Learn how to master the grid!");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 11));  // Smaller font
        subtitleLabel.setForeground(new Color(200, 200, 255));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));  // Reduced gap
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private Component createRulesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));  // Reduced padding
        
        // Game Objective
        panel.add(createSectionTitle("🎯 Game Objective"));
        panel.add(createTextBlock(
            "The goal of JavaGrid4 is to claim more cells than your opponent by the end of the game. " +
            "The player with the most claimed cells (value 4) wins!"
        ));
        panel.add(Box.createVerticalStrut(8));  // Reduced spacing
        
        // Basic Rules
        panel.add(createSectionTitle("📋 Basic Rules"));
        panel.add(createBulletPoint("1. Players take turns clicking on grid cells"));
        panel.add(createBulletPoint("2. Each click increments the cell's value by 1"));
        panel.add(createBulletPoint("3. Clicking also increments the 4 neighboring cells (up, down, left, right)"));
        panel.add(createBulletPoint("4. When a cell reaches value 4, it's claimed by the current player"));
        panel.add(createBulletPoint("5. Claimed cells (value 4) cannot be clicked again"));
        panel.add(createBulletPoint("6. The game ends when all cells reach value 4"));
        panel.add(Box.createVerticalStrut(8));  // Reduced spacing
        
        // Scoring
        panel.add(createSectionTitle("🏆 Scoring"));
        panel.add(createTextBlock(
            "Each cell claimed (reaching value 4) awards 1 point to the player. " +
            "Since clicking a cell can affect up to 5 cells (the clicked cell + 4 neighbors), " +
            "strategic play can lead to claiming multiple cells in one turn!"
        ));
        panel.add(Box.createVerticalStrut(12));
        
        // Special Cases
        panel.add(createSectionTitle("⚠️ Special Cases"));
        panel.add(createBulletPoint("• Edge cells have only 3 neighbors (one side is empty)"));
        panel.add(createBulletPoint("• Corner cells have only 2 neighbors"));
        panel.add(createBulletPoint("• Cells already at value 4 are locked and shown in player colors"));
        panel.add(createBulletPoint("• A tie occurs if both players have the same score"));
        
        panel.add(Box.createVerticalGlue());
        
        return new JScrollPane(panel);
    }
    
    private Component createHowToPlayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Starting the game
        panel.add(createSectionTitle("🚀 Starting a Game"));
        panel.add(createNumberedStep("1", "Enter player names and choose colors"));
        panel.add(createNumberedStep("2", "Select board size (3x3 to 7x7)"));
        panel.add(createNumberedStep("3", "Click 'Start Game' button"));
        panel.add(Box.createVerticalStrut(15));
        
        // During gameplay
        panel.add(createSectionTitle("🎮 During Gameplay"));
        panel.add(createNumberedStep("1", "Wait for your turn (current player shown at top)"));
        panel.add(createNumberedStep("2", "Click any cell that hasn't reached value 4"));
        panel.add(createNumberedStep("3", "Watch as the cell and its neighbors increment"));
        panel.add(createNumberedStep("4", "Celebrate when cells reach 4 (you score!)"));
        panel.add(createNumberedStep("5", "Opponent's turn begins automatically"));
        panel.add(Box.createVerticalStrut(15));
        
        // Understanding cell values
        panel.add(createSectionTitle("🔢 Understanding Cell Values"));
        panel.add(createTextBlock(
            "• Value 0-3: Available to click, shown in neutral color\n" +
            "• Value 4: Claimed by a player, shown in player's color\n" +
            "• Larger numbers = darker color intensity\n" +
            "• Animations play when cells are claimed"
        ));
        panel.add(Box.createVerticalStrut(15));
        
        // Game controls
        panel.add(createSectionTitle("🎛️ Game Controls"));
        panel.add(createBulletPoint("• Restart: Start a new game with same settings"));
        panel.add(createBulletPoint("• Pause: Freeze the game (resume with same button)"));
        panel.add(createBulletPoint("• Menu: Return to main menu (with confirmation)"));
        panel.add(createBulletPoint("• Undo/Redo: Take back or replay moves"));
        panel.add(Box.createVerticalStrut(15));
        
        // End of game
        panel.add(createSectionTitle("🏁 End of Game"));
        panel.add(createTextBlock(
            "When all cells reach value 4, a game over overlay appears showing:\n" +
            "• Winner announcement\n" +
            "• Final scores for both players\n" +
            "• Automatic transition to results screen with victory confetti!"
        ));
        
        panel.add(Box.createVerticalGlue());
        
        return new JScrollPane(panel);
    }
    
    private Component createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Mouse controls
        panel.add(createSectionTitle("🖱️ Mouse Controls"));
        panel.add(createControlRow("Left Click", "Select cell to increment (on available cells)"));
        panel.add(createControlRow("Button Hover", "Visual feedback on interactive elements"));
        panel.add(Box.createVerticalStrut(15));
        
        // Keyboard shortcuts
        panel.add(createSectionTitle("⌨️ Keyboard Shortcuts"));
        panel.add(createControlRow("ESC", "Return to main menu (with confirmation)"));
        panel.add(createControlRow("R", "Restart game (with confirmation)"));
        panel.add(createControlRow("P", "Pause/Resume game"));
        panel.add(createControlRow("Ctrl+Z", "Undo last move"));
        panel.add(createControlRow("Ctrl+Y", "Redo last undone move"));
        panel.add(createControlRow("F1", "Open this help dialog"));
        panel.add(Box.createVerticalStrut(15));
        
        // Button controls
        panel.add(createSectionTitle("🎚️ Button Controls"));
        panel.add(createControlRow("Restart Button", "Begin a new game with same configuration"));
        panel.add(createControlRow("Pause Button", "Freeze/unfreeze the game"));
        panel.add(createControlRow("Menu Button", "Return to menu (prompts for confirmation)"));
        panel.add(createControlRow("Undo Button", "Take back your last move"));
        panel.add(createControlRow("Redo Button", "Replay a move you undid"));
        panel.add(Box.createVerticalStrut(15));
        
        // Results screen
        panel.add(createSectionTitle("🎊 Results Screen Controls"));
        panel.add(createControlRow("Play Again", "Start a new game with same settings"));
        panel.add(createControlRow("New Game", "Return to menu to change settings"));
        panel.add(createControlRow("Exit", "Close the application"));
        
        panel.add(Box.createVerticalGlue());
        
        return new JScrollPane(panel);
    }
    
    private Component createStrategyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Basic strategies
        panel.add(createSectionTitle("🎯 Basic Strategies"));
        panel.add(createStrategyTip(
            "Center Control",
            "Center cells have 4 neighbors, making them valuable for claiming multiple cells. " +
            "Try to control the center early in the game."
        ));
        panel.add(createStrategyTip(
            "Edge Awareness",
            "Edge and corner cells have fewer neighbors (3 and 2 respectively). " +
            "Use them strategically to limit your opponent's options."
        ));
        panel.add(createStrategyTip(
            "Plan Ahead",
            "Look at cell values before clicking. A cell at value 3 will be claimed with one more click. " +
            "Try to time your moves to claim multiple cells at once."
        ));
        panel.add(Box.createVerticalStrut(15));
        
        // Advanced strategies
        panel.add(createSectionTitle("🎓 Advanced Strategies"));
        panel.add(createStrategyTip(
            "Chain Reactions",
            "Position cells so that one click triggers multiple claims. " +
            "This requires setting up multiple cells at value 3 near each other."
        ));
        panel.add(createStrategyTip(
            "Defensive Play",
            "If your opponent has cells ready to claim (at value 3), consider clicking them " +
            "to reach value 4 yourself, denying them the point."
        ));
        panel.add(createStrategyTip(
            "Count Remaining Cells",
            "Keep track of how many unclaimed cells remain. " +
            "If you're ahead, focus on safe moves. If behind, take calculated risks."
        ));
        panel.add(Box.createVerticalStrut(15));
        
        // Pro tips
        panel.add(createSectionTitle("⚡ Pro Tips"));
        panel.add(createBulletPoint("• Use Undo to explore different strategies without penalty"));
        panel.add(createBulletPoint("• Watch for cells at value 3 - they're one click from scoring"));
        panel.add(createBulletPoint("• Corners are great for final moves when options are limited"));
        panel.add(createBulletPoint("• Don't forget: clicking affects 5 cells (center + 4 neighbors)"));
        panel.add(createBulletPoint("• Pay attention to the score difference, not just cell count"));
        
        panel.add(Box.createVerticalGlue());
        
        return new JScrollPane(panel);
    }
    
    private Component createAboutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Game info
        panel.add(createSectionTitle("ℹ️ About JavaGrid4"));
        panel.add(createTextBlock(
            "JavaGrid4 is a strategic two-player grid-based game where players compete to claim cells " +
            "by incrementing their values. The game features smooth animations, sound effects, " +
            "and a polished user experience."
        ));
        panel.add(Box.createVerticalStrut(15));
        
        // Version info
        panel.add(createSectionTitle("📦 Version Information"));
        panel.add(createInfoRow("Version", "1.0.0"));
        panel.add(createInfoRow("Release Date", "November 2025"));
        panel.add(createInfoRow("Platform", "Java (Cross-platform)"));
        panel.add(createInfoRow("Framework", "Java Swing"));
        panel.add(Box.createVerticalStrut(15));
        
        // Features
        panel.add(createSectionTitle("✨ Features"));
        panel.add(createBulletPoint("• Two-player local gameplay"));
        panel.add(createBulletPoint("• Customizable player names and colors"));
        panel.add(createBulletPoint("• Variable board sizes (3x3 to 7x7)"));
        panel.add(createBulletPoint("• Smooth cell claim animations"));
        panel.add(createBulletPoint("• Flying score increment animations"));
        panel.add(createBulletPoint("• Undo/Redo system with Command pattern"));
        panel.add(createBulletPoint("• Synthetic sound effects"));
        panel.add(createBulletPoint("• Victory confetti celebration"));
        panel.add(createBulletPoint("• Game over overlay with results"));
        panel.add(createBulletPoint("• Screen transitions with fade effects"));
        panel.add(Box.createVerticalStrut(15));
        
        // Credits
        panel.add(createSectionTitle("👨‍💻 Credits"));
        panel.add(createTextBlock(
            "Developed as a demonstration of advanced Java Swing programming techniques, " +
            "design patterns, and game development principles."
        ));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createTextBlock(
            "Design Patterns Used:\n" +
            "• MVC (Model-View-Controller)\n" +
            "• Observer Pattern\n" +
            "• Command Pattern (Undo/Redo)\n" +
            "• Singleton Pattern\n" +
            "• Strategy Pattern (Animation types)"
        ));
        panel.add(Box.createVerticalStrut(15));
        
        // Contact/support
        panel.add(createSectionTitle("📧 Support"));
        panel.add(createTextBlock(
            "For questions, feedback, or bug reports, please refer to the project repository.\n\n" +
            "Thank you for playing JavaGrid4! 🎮"
        ));
        
        panel.add(Box.createVerticalGlue());
        
        return new JScrollPane(panel);
    }
    
    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(5, 20, 8, 20));
        
        JButton closeButton = new JButton("Close (ESC)");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(HEADER_COLOR);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(130, 35));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        closeButton.addActionListener((ActionEvent e) -> dispose());
        
        // Hover effect
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(ACCENT_COLOR);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(HEADER_COLOR);
            }
        });
        
        footerPanel.add(closeButton);
        
        return footerPanel;
    }
    
    // Helper methods for creating UI components
    
    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));  // Reduced from 18
        label.setForeground(HEADER_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));  // Reduced padding
        return label;
    }
    
    private JPanel createTextBlock(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            new EmptyBorder(10, 12, 10, 12)  // Reduced padding
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));  // Reduced height
        
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));  // Reduced from 14
        textArea.setForeground(TEXT_COLOR);
        textArea.setBackground(CARD_COLOR);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        panel.add(textArea, BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        return panel;
    }
    
    private JPanel createBulletPoint(String text) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));  // Reduced from 30
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));  // Reduced from 14
        label.setForeground(TEXT_COLOR);
        
        panel.add(label, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createNumberedStep(String number, String text) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        numberLabel.setForeground(ACCENT_COLOR);
        numberLabel.setPreferredSize(new Dimension(30, 30));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_COLOR);
        
        panel.add(numberLabel, BorderLayout.WEST);
        panel.add(textLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlRow(String key, String description) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 240)),
            new EmptyBorder(10, 15, 10, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        keyLabel.setForeground(ACCENT_COLOR);
        keyLabel.setPreferredSize(new Dimension(120, 30));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_COLOR);
        
        panel.add(keyLabel, BorderLayout.WEST);
        panel.add(descLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStrategyTip(String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 12, 10, 12)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("💡 " + title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setForeground(TEXT_COLOR);
        descArea.setBackground(CARD_COLOR);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        panel.add(titleLabel);
        panel.add(descArea);
        
        return panel;
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelText = new JLabel(label + ":");
        labelText.setFont(new Font("Arial", Font.BOLD, 14));
        labelText.setForeground(HEADER_COLOR);
        labelText.setPreferredSize(new Dimension(150, 30));
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Arial", Font.PLAIN, 14));
        valueText.setForeground(TEXT_COLOR);
        
        panel.add(labelText, BorderLayout.WEST);
        panel.add(valueText, BorderLayout.CENTER);
        
        return panel;
    }
}
