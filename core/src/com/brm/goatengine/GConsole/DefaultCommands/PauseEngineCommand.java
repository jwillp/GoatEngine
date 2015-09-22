package com.brm.GoatEngine.GConsole.DefaultCommands;

import com.brm.GoatEngine.GConsole.ConsoleCommand;
import com.brm.GoatEngine.GoatEngine;

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
