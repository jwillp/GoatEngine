package com.brm.GoatEngine.GConsole;

/**
 * A command Object for GConsole
 */
public abstract class ConsoleCommand {

    private String name;    // Name of the command
    private String desc;    // Desciption of the command
    private String usage;   // Usage and parameters

    public abstract void exec(String... args);
}
