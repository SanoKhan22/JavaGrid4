package com.mycompany.javagrid4.commands;

/**
 * Command interface for implementing undo/redo functionality.
 * Follows the Command design pattern.
 * 
 * Each command encapsulates a game action that can be:
 * - Executed (perform the action)
 * - Undone (reverse the action)
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public interface GameCommand {
    
    /**
     * Executes the command (performs the action).
     */
    void execute();
    
    /**
     * Undoes the command (reverses the action).
     */
    void undo();
    
    /**
     * Gets a description of this command.
     * Used for debugging and logging.
     * 
     * @return Command description
     */
    String getDescription();
}
