package com.brm.GoatEngine.GConsole;

import com.brm.GoatEngine.GoatEngine;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;

/**
 * Script Command Executor specific to the GoatEngine
 */
public class GConsoleCommandExecutor extends CommandExecutor {



    // SCRIPTING COMMANDS //
    /**
     * Loads a script file and runs it
     * @param scriptName the script file name
     */
    public void runScript(String scriptName){

        //Automatically add the extension
        if(!scriptName.endsWith(".groovy")){
            scriptName += ".groovy";
        }

        //runScript("scripts/"+scriptName); //Retry in the script folder

        /*try{
            GoatEngine.scriptEngine.runScript(scriptName);
        }catch(ScriptingEngine.ScriptNotFoundException e){
            GoatEngine.console.log(e.getMessage(), Console.LogLevel.ERROR);
        }catch (Exception e){
            GoatEngine.console.log(e.getMessage(), Console.LogLevel.ERROR);
        }*/

    }

    public void reloadScript(String scriptFileName){
        /*try{
            GoatEngine.scriptEngine.reloadScript(scriptFileName);
        }catch(Exception e){
            console.log(e.getMessage(), Console.LogLevel.ERROR);
        }*/

    }

    public void e(String s){
        console.log("s", Console.LogLevel.INFO);
    }


    // GAME SCREEN COMMANDS //

    /**
     * Pauses the game screen manager
     */
    public void pauseGame(){
        GoatEngine.gameScreenManager.pause();
    }

    public void resumeGame(){
        GoatEngine.gameScreenManager.resume();
    }

    public void changeScreen(String name){}

}
