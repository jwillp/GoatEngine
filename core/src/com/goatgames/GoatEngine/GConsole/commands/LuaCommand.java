package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import com.goatgames.goatengine.utils.Logger;

/**
 * Executes lua script
 */
public class LuaCommand extends ConsoleCommand {


    public LuaCommand() {

    }

    @Override
    protected void execute(String... args) {
        if(args.length == 0){
            console.log(getUsage());
        }
        Logger.info("Calling script: " + args[0]);
        LuaScript script = new LuaScript("data/scripts/" + args[0]);
        script.load();
        script.executeFunction(args[1]);
    }

    @Override
    public String getName() {
        return "lua";
    }

    @Override
    public String getDesc() {
        return "Executes a Lua Script or method within";
    }

    @Override
    public String getUsage() {
        return "Usage: lua <scriptName> [methodName]";
    }
}
