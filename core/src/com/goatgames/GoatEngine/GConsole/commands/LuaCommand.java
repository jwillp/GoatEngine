package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import com.goatgames.goatengine.utils.Logger;

/**
 * Executes lua script
 */
public class LuaCommand extends ConsoleCommand {


    public LuaCommand() {
        super("lua");
    }

    @Override
    protected void execute(String... args) {
        Logger.info("Calling script: " + args[0]);
        LuaScript script = new LuaScript("data/scripts/" + args[0]);
        script.load();
        script.executeFunction("helloWorldA");
    }
}
