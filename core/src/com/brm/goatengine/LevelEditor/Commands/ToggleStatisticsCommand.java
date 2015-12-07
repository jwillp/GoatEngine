package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.LevelEditor.LevelEditor;

/**
 * Toggle Statistics
 */
public class ToggleStatisticsCommand extends EditorCommand {


    private LevelEditor editor;

    public ToggleStatisticsCommand(LevelEditor editor){

        this.editor = editor;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        editor.getView().toggleStatistics();
    }
}
