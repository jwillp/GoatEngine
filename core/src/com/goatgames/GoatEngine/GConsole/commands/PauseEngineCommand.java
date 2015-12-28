package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

/**
 * Pauses the game engine
 */
public class PauseEngineCommand extends ConsoleCommand {


    public PauseEngineCommand() {
        super("pause");
        this.desc = "Pauses the game engine";
        this.usage = "Usage: pause";
    }

    @Override
    protected void execute(String... args) {
        GoatEngine.gameScreenManager.pause();
    }
}
