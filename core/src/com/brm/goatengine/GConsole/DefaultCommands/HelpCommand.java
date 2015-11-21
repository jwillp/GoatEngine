package com.brm.GoatEngine.GConsole.DefaultCommands;

import com.brm.GoatEngine.GConsole.ConsoleCommand;
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
    public void exec(String... args) {
        super.exec(args);
        console.log("HELP COMMAND", Console.LogLevel.INFO);
        for(ConsoleCommand c: this.console.getCommands()){
            String helpString = String.format("%-60s %s" ,c.getName(), c.getDesc());
            console.log(helpString, Console.LogLevel.INFO);

        }
    }
}
