package com.goatgames.goatengine.gconsole.DefaultCommands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.GoatEngine;

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
