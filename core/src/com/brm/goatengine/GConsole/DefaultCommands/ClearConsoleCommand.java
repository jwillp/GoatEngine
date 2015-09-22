package com.brm.GoatEngine.GConsole.DefaultCommands;

import com.brm.GoatEngine.GConsole.ConsoleCommand;
import com.brm.GoatEngine.GoatEngine;

/**
 * Clears the console screen buffer
 */
public class ClearConsoleCommand extends ConsoleCommand {


    public ClearConsoleCommand() {
        super("clear");
        this.desc = "Clears the console screen buffer";
        this.usage = "Usage: clear";
    }



    @Override
    public void exec(String... args) {
        super.exec(args);
        GoatEngine.console.clear();
    }
}
