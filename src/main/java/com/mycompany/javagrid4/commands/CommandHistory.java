package com.mycompany.javagrid4.commands;

import java.util.Stack;

/**
 * Manages command history for undo/redo functionality.
 * Maintains two stacks:
 * - Undo stack: Commands that can be undone
 * - Redo stack: Commands that can be redone
 * 
 * @author JavaGrid4 Team
 * @version 1.0
 */
public class CommandHistory {
    
    private final Stack<GameCommand> undoStack;
    private final Stack<GameCommand> redoStack;
    
    /**
     * Creates a new command history.
     */
    public CommandHistory() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    
    /**
     * Executes a command and adds it to history.
     * Clears redo stack (can't redo after new action).
     * 
     * @param command Command to execute
     */
    public void executeCommand(GameCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack on new action
    }
    
    /**
     * Undoes the last command.
     * 
     * @return true if undo was successful, false if nothing to undo
     */
    public boolean undo() {
        if (canUndo()) {
            GameCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            return true;
        }
        return false;
    }
    
    /**
     * Redoes the last undone command.
     * 
     * @return true if redo was successful, false if nothing to redo
     */
    public boolean redo() {
        if (canRedo()) {
            GameCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if undo is available.
     * 
     * @return true if there are commands to undo
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    /**
     * Checks if redo is available.
     * 
     * @return true if there are commands to redo
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    /**
     * Clears all command history.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
    
    /**
     * Gets the number of commands that can be undone.
     * 
     * @return Undo stack size
     */
    public int getUndoCount() {
        return undoStack.size();
    }
    
    /**
     * Gets the number of commands that can be redone.
     * 
     * @return Redo stack size
     */
    public int getRedoCount() {
        return redoStack.size();
    }
}
