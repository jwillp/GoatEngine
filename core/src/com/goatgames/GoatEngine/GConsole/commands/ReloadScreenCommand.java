package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.gconsole.ConsoleCommand;

/**
 * Reloads a screen
 */
public class ReloadScreenCommand extends ConsoleCommand {

    public ReloadScreenCommand() {
    }

    @Override
    protected void execute(String... args) {
        String currentScreen = GoatEngine.gameScreenManager.getCurrentScreen().getName();
        GoatEngine.gameScreenManager.changeScreen(currentScreen);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDesc() {
        return "Reloads the current game screen";
    }

    @Override
    public String getUsage() {
        return "Usage: reload";
    }

}
