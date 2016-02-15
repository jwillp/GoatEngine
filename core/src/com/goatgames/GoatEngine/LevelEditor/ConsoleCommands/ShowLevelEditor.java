package com.goatgames.goatengine.leveleditor.consolecommands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

/**
 * Show the Level Editor
 */
public class ShowLevelEditor extends ConsoleCommand {

    public ShowLevelEditor() {}

    @Override
    protected void execute(String... args) {
        GoatEngine.showLevelEditor();
        this.console.toggle();
    }

    @Override
    public String getName() {
        return "level_editor";
    }

    @Override
    public String getDesc() {
        return "Displays the level editor";
    }

    @Override
    public String getUsage() {
        return "Usage: level_editor";
    }
}
