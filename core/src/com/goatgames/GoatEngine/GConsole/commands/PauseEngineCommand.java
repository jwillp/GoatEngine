package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

/**
 * Pauses the game engine
 */
public class PauseEngineCommand extends ConsoleCommand {


    public PauseEngineCommand() {
    }

    @Override
    protected void execute(String... args) {
        GoatEngine.gameScreenManager.pause();
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDesc() {
        return "Pauses the game engine";
    }

    @Override
    public String getUsage() {
        return "Usage: pause";
    }
}
