package com.goatgames.goatengine.leveleditor.commands;

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
