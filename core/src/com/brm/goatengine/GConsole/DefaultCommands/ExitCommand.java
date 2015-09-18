package com.brm.GoatEngine.GConsole.DefaultCommands;

import com.brm.GoatEngine.GConsole.ConsoleCommand;
import com.brm.GoatEngine.GoatEngine;

/**
 * Exits (the engine/the game)
 */
public class ExitCommand extends ConsoleCommand {


    public ExitCommand() {
        super("exit");
        this.desc = "Exits the game engine";
        this.usage = "Usage: exit";
    }



    @Override
    public void exec(String... args) {
        super.exec(args);
        GoatEngine.gameScreenManager.pause();
    }
}
