package com.goatgames.goatengine.gconsole;

import com.goatgames.goatengine.GoatEngine;
import com.strongjoshua.console.Console;

/**
 * A command Object for GConsole
 */
public abstract class ConsoleCommand {


    protected Console console;

    public ConsoleCommand(){
    }



    public void exec(String... args){
        // Help for usage
        if(helpRequest()) {
            console.log(getName());
            console.log(getDesc());
            console.log(getUsage());
            return;
        }
        execute(args);
    }

    protected abstract void execute(String... args);



    /**
     * Indicates if the user wanted to see the help associated with the current command
     * @return
     */
    protected boolean helpRequest(String... args) {
        return (args.length != 0) && (args[0].equals("-h") || args[0].equals("--help"));
    }

    public abstract String getName();
    public abstract String getDesc();
    public abstract String getUsage();

    public Console getConsole() {
        return console;
    }
    public void setConsole(Console console) {
        this.console = console;
    }
}
