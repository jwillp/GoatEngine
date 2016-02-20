package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.gconsole.ConsoleCommand;

/**
 * Resumes the game engine
 */
public class ResumeEngineCommand extends ConsoleCommand {


    public ResumeEngineCommand() {

    }


    @Override
    protected void execute(String... args) {
        GoatEngine.gameScreenManager.resume();
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getDesc() {
        return "Resumes the game engine";
    }

    @Override
    public String getUsage() {
        return "Usage: resume";
    }
}
