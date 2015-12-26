package com.goatgames.goatengine.gconsole.DefaultCommands;

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
    public void exec(String... args) {
        super.exec(args);
        // TODO
        GoatEngine.gameScreenManager.pause();
       
    }
}
