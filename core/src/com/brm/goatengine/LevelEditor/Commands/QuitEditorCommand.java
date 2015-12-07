package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.LevelEditor.LevelEditor;

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
