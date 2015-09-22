package com.brm.GoatEngine.GConsole.DefaultCommands;

import com.brm.GoatEngine.GConsole.ConsoleCommand;
import com.brm.GoatEngine.GoatEngine;

/**
 * Resumes the game engine
 */
public class ResumeEngineCommand extends ConsoleCommand {


    public ResumeEngineCommand() {
        super("resume");
        this.desc = "Resumes the game engine";
        this.usage = "Usage: resume";
    }



    @Override
    public void exec(String... args) {
        super.exec(args);
        // TODO
       GoatEngine.gameScreenManager.resume();

    }
}
