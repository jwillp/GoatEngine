package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.screenmanager.GameScreenConfig;
import com.strongjoshua.console.Console;

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
        config.setBoolean("rendering.physics_debug", Boolean.parseBoolean(args[0]));
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
