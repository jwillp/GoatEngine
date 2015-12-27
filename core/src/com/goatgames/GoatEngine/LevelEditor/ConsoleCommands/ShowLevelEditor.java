package com.goatgames.goatengine.leveleditor.ConsoleCommands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

/**
 * Show the Level Editor
 */
public class ShowLevelEditor extends ConsoleCommand {
    public ShowLevelEditor() {
        super("level_editor");
        this.desc = "Displays the level editor";
        this.usage = "Usage: level_editor";
    }


    @Override
    public void exec(String... args) {
        super.exec(args);
        GoatEngine.showLevelEditor();
        this.console.toggle();
    }
}