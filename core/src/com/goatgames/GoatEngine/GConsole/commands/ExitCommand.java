package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

/**
 * Exits (the engine/the game)
 */
public class ExitCommand extends ConsoleCommand {


    public ExitCommand() {

    }

    @Override
    protected void execute(String... args) {
        GoatEngine.shutdown();
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDesc() {
        return "Exits the game engine";
    }

    @Override
    public String getUsage() {
        return "Usage: exit";
    }
}
