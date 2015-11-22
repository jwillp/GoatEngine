package com.brm.GoatEngine.LevelEditor.Commands;

/**
 * Commands that can be Undone and redone
 */
public interface UndoCommand {

    /**
     * Used to undo a command
     */
    public void undo();

    /**
     * Used to do a command
     */
    public void redo();
}
