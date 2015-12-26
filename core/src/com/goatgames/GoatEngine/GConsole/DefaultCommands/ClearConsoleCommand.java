package com.goatgames.goatengine.gconsole.DefaultCommands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

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
