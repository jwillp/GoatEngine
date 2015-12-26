package com.goatgames.goatengine.leveleditor.Commands;

import com.goatgames.goatengine.GoatEngine;

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
