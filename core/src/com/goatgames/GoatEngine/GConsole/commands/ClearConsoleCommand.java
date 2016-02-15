package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

/**
 * Clears the console screen buffer
 */
public class ClearConsoleCommand extends ConsoleCommand {


    public ClearConsoleCommand() {}

    @Override
    protected void execute(String... args) {
        GoatEngine.console.clear();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDesc() {
        return "Clears the console screen buffer";
    }

    @Override
    public String getUsage() {
        return "Usage: clear";
    }
}
