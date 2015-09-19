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
        for(ConsoleCommand c: this.console.getCommands()){
            console.log(c.getName() + "     -     " + c.getDesc(), Console.LogLevel.INFO);
        }
    }
}
