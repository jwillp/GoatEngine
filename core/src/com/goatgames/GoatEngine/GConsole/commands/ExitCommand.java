package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

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
    protected void execute(String... args) {
        GoatEngine.shutdown();
    }
}
