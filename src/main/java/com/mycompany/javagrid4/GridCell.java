package com.mycompany.javagrid4;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a single cell in the JavaGrid4 game grid.
 * Extends JButton to provide visual representation and interaction.
 */
public class GridCell extends JButton {
    private static final int CELL_SIZE = 80;
    private static final int FONT_SIZE = 24;
    private static final Color NEUTRAL_COLOR = new Color(200, 200, 200);
    
    private final int row;
    private final int column;
    private int cellValue;
    private Player owner;
    
    // Custom player colors
    private Color player1Color;
    private Color player2Color;
    
    /**
     * Creates a new GridCell at the specified position with default colors.
     * @param row The row index in the grid
     * @param column The column index in the grid
     */
    public GridCell(int row, int column) {
        this(row, column, new Color(255, 100, 100), new Color(100, 150, 255));
    }
    
    /**
     * Creates a new GridCell at the specified position with custom player colors.
     * @param row The row index in the grid
     * @param column The column index in the grid
     * @param player1Color Color for Player 1
     * @param player2Color Color for Player 2
     */
    public GridCell(int row, int column, Color player1Color, Color player2Color) {
        this.row = row;
        this.column = column;
        this.cellValue = 0;
        this.owner = null;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        
        initializeAppearance();
    }
    
    /**
     * Initializes the visual appearance of the cell.
     */
    private void initializeAppearance() {
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        setFocusPainted(false);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        updateDisplay();
    }
    
    /**
     * Gets the row position of this cell.
     * @return Row index
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Gets the column position of this cell.
     * @return Column index
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * Gets the current value of this cell (0-4).
     * @return Current cell value
     */
    public int getCellValue() {
        return cellValue;
    }
    
    /**
     * Sets the value of this cell and updates display.
     * @param value New value (0-4)
     */
    public void setCellValue(int value) {
        this.cellValue = value;
        updateDisplay();
    }
    
    /**
     * Gets the owner of this cell (player who reached 4).
     * @return Owner player or null if neutral
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Sets the owner of this cell and updates display.
     * @param owner Player who owns this cell
     */
    public void setOwner(Player owner) {
        this.owner = owner;
        updateDisplay();
    }
    
    /**
     * Updates the visual display based on current value and owner.
     */
    private void updateDisplay() {
        setText(String.valueOf(cellValue));
        
        if (owner == null) {
            setBackground(NEUTRAL_COLOR);
        } else if (owner == Player.PLAYER_ONE) {
            setBackground(player1Color);
        } else {
            setBackground(player2Color);
        }
    }
    
    /**
     * Resets the cell to initial state (value 0, no owner).
     */
    public void resetCell() {
        this.cellValue = 0;
        this.owner = null;
        updateDisplay();
    }
}
