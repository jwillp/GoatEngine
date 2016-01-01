package com.goatgames.goatengine.leveleditor.commands;

import com.goatgames.goatengine.leveleditor.LevelEditor;

/**
 * Quits the Editor
 */
public class QuitEditorCommand extends EditorCommand {


    private LevelEditor editor;

    public QuitEditorCommand(LevelEditor editor){
        this.editor = editor;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        editor.setEnabled(false);
    }
}
