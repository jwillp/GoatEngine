package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.goatgames.goatengine.utils.Logger;
import com.strongjoshua.console.Console;

import java.util.Objects;

/**
 * Prints the list of available commands for the Console along with their description
 */
public class HelpCommand extends ConsoleCommand{

    @Override
    protected void execute(String... args) {
        if(args.length == 0) {
            console.log("HELP COMMAND", Console.LogLevel.INFO);
        }

        for (ConsoleCommand c : this.console.getCommands()) {

            if(args.length == 0) {
                String helpString = String.format("%-20s %s", c.getName(), c.getDesc());
                Logger.debug(helpString);
                console.log(helpString, Console.LogLevel.INFO);
            }else{
                if(Objects.equals(c.getName(), args[0])){
                    console.log(getName());
                    console.log(getDesc());
                    console.log(getUsage());
                }
            }
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDesc() {
        return "Prints the list of all the available commands";
    }

    @Override
    public String getUsage() {
        return "Usage: help";
    }
}
