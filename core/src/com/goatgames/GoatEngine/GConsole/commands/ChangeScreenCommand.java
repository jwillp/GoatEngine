package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.screenmanager.GameScreen;
import com.strongjoshua.console.Console;

/**
 * Changes the current screen
 */
public class ChangeScreenCommand extends ConsoleCommand {

    public ChangeScreenCommand(){}

    @Override
    protected void execute(String... args) {
        String currentScreen = GoatEngine.gameScreenManager.getCurrentScreen().getName();
        try{
            GoatEngine.gameScreenManager.changeScreen(args[0]);
        }catch (GameScreen.GameScreenNotFoundException e){
            console.log(e.getMessage(), Console.LogLevel.WARNING);
            new ChangeScreenCommand().exec(currentScreen);
        }
    }

    @Override
    public String getName() {
        return "change_screen";
    }

    @Override
    public String getDesc() {
        return "Changes the current game screen manager's screen for a given screen";
    }

    @Override
    public String getUsage() {
        return "Usage: change_screen screen_name";
    }
}
