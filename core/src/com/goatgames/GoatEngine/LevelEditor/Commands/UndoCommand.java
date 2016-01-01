package com.goatgames.goatengine.leveleditor.commands;

/**
 * Commands that can be undone and redone
 */
public abstract class UndoCommand extends EditorCommand implements Undoable {


    /**
     * Executes the logic of the command
     */
    public void exec(){ redo(); }

    /**
     * Used to undo a command
     */
    @Override
    public abstract void undo();

    /**
     * Used to do a command
     */
    @Override
    public abstract void redo();
}
