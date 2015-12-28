package com.goatgames.goatengine.gconsole.commands;

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
    protected void execute(String... args) {
        GoatEngine.gameScreenManager.resume();
    }
}
