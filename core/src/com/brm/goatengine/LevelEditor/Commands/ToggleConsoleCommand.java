package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.GoatEngine;

/**
 * Toggles the console
 */
public class ToggleConsoleCommand extends EditorCommand {
    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        GoatEngine.console.toggle();
    }
}
