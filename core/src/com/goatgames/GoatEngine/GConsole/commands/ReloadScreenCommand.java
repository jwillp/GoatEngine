package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.gconsole.ConsoleCommand;

/**
 * Reloads a screen
 */
public class ReloadScreenCommand extends ConsoleCommand {

    public ReloadScreenCommand() {
        super("reload");
        this.desc = "Reloads the current game screen";
        this.usage = "Usage: reload";
    }

    @Override
    protected void execute(String... args) {
        String currentScreen = GoatEngine.gameScreenManager.getCurrentScreen().getName();
        GoatEngine.gameScreenManager.changeScreen(currentScreen);
    }

}
