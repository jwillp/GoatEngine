package com.brm.GoatEngine.GConsole;

import com.strongjoshua.console.Console;

/**
 * A command Object for GConsole
 */
public abstract class ConsoleCommand {

    protected String name;    // Name of the command
    protected String desc = "";    // Desciption of the command
    protected String usage = "";   // Usage and parameters

    public ConsoleCommand(String name) {
        this.name = name;
    }



    public abstract void exec(String... args);

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
}
