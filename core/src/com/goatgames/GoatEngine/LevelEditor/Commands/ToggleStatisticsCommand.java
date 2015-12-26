package com.goatgames.goatengine.leveleditor.Commands;

import com.goatgames.goatengine.leveleditor.LevelEditor;

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
