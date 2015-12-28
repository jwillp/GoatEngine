package com.goatgames.goatengine.gconsole.commands;

import com.goatgames.goatengine.gconsole.ConsoleCommand;
import com.strongjoshua.console.Console;

/**
 * Prints the list of available commands for the Console along with their description
 */
public class HelpCommand extends ConsoleCommand{

    public HelpCommand() {
        super("help");
        this.desc = "Prints the list of all the available commands";
        this.usage = "Usage: exit";
    }

    @Override
    protected void execute(String... args) {
        console.log("HELP COMMAND", Console.LogLevel.INFO);
        for(ConsoleCommand c: this.console.getCommands()){
            String helpString = String.format("%-60s %s" ,c.getName(), c.getDesc());
            console.log(helpString, Console.LogLevel.INFO);

        }
    }
}
