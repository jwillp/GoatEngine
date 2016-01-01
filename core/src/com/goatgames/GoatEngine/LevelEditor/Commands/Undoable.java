package com.goatgames.goatengine.leveleditor.commands;

/**
 * Represents actions that can be undone
 */
public interface Undoable {

    /**
     * Used to undo a command
     */
    public void undo();

    /**
     * Used to do a command
     */
    public void redo();
}
