package com.goatgames.goatengine.gconsole.DefaultCommands;

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
    public void exec(String... args) {
        super.exec(args);
        GoatEngine.gameScreenManager.pause();
    }
}
