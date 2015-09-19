package com.brm.GoatEngine.GConsole;

import com.brm.GoatEngine.GoatEngine;
import com.strongjoshua.console.Console;

/**
 * A command Object for GConsole
 */
public abstract class ConsoleCommand {

    protected String name;    // Name of the command
    protected String desc = "No description";    // Desciption of the command
    protected String usage = "Usage: ";   // Usage and parameters

    protected Console console;

    public ConsoleCommand(String name) {
        this.name = name;
    }



    public void exec(String... args){
        // Help for usage
        if (args.length != 0) {
            if (args[0].equals("-h") || args[0].equals("-help")) {
                GoatEngine.console.log(usage, Console.LogLevel.DEFAULT);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }
}
