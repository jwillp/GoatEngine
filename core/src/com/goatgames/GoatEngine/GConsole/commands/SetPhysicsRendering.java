package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.screenmanager.GameScreenConfig;
import com.strongjoshua.console.Console;

import java.util.Objects;

/**
 * Sets the physics rendering
 */
public class SetPhysicsRendering extends ConsoleCommand {

    public SetPhysicsRendering() {
    }

    @Override
    protected void execute(String... args) {
        if(args.length == 0){
            console.log("you must specify a value", Console.LogLevel.ERROR);
            console.log(getUsage(), Console.LogLevel.DEFAULT);
            return;
        }
        GameScreenConfig config = GoatEngine.gameScreenManager.getCurrentScreen().getConfig();

        boolean value = Boolean.parseBoolean(args[0]);
        if(Objects.equals(args[0].toLowerCase(), "off")) value = false;
        else if(args[0].toLowerCase().equals("on")) value = true;

        config.setBoolean("rendering.physics_debug", value);
    }

    @Override
    public String getName() {
        return "physics_rendering";
    }

    @Override
    public String getDesc() {
        return "Sets the debug rendering to ON or OFF";
    }

    @Override
    public String getUsage() {
        return String.format("Usage: %s <true|false>", getName());
    }
}
