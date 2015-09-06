package com.brm.GoatEngine;

import com.badlogic.gdx.Gdx;
import com.brm.GoatEngine.EventManager.EventManager;
import com.brm.GoatEngine.GraphicsEngine.GraphicsEngine;
import com.brm.GoatEngine.Input.InputManager;
import com.brm.GoatEngine.GConsole.GConsoleCommandExecutor;
import com.brm.GoatEngine.GConsole.GConsole;
import com.brm.GoatEngine.ScreenManager.GameScreenManager;
import com.brm.GoatEngine.ScriptingEngine.ScriptingEngine;
import com.brm.GoatEngine.Utils.Logger;
import com.strongjoshua.console.Console;

/**
 * The base class for the whole GamEngine
 * Contains the core functions such as
 *     - init (for startup)
 *     - loop
 *     - shutdown
 *
 * And a static access to the most important modules
 */
public class GoatEngine {

    //Scripting Engine
    public static ScriptingEngine scriptEngine;

    //ScreenManager
    public static GameScreenManager gameScreenManager;

    //Event Manager
    public static EventManager eventManager;

    //Music and Sound Manager
    public static AudioMixer audioMixer;

    //InputManager
    public static InputManager inputManager;

    //Console
    public static GConsole console;

    //Graphics Engine
    public static GraphicsEngine graphicsEngine;


    // TODO NetworkManager ?




    private static boolean initialised = false;


    /**
     * This initializes the Game Engine
     */
    public static void init(){
        GEConfig.loadConfig();

        Logger.info("Engine Initialisation ...");

        //Graphics Engine
        graphicsEngine = new GraphicsEngine();

        // Event Manager
        eventManager = new EventManager();

        // input manager
        inputManager = new InputManager();
        inputManager.init();

        // Audio Manager
        audioMixer = new AudioMixer();

        //Init the console
        console = new GConsole();
        console.setCommandExecutor(new GConsoleCommandExecutor());
        console.log("Dev Console initialised", Console.LogLevel.SUCCESS);
        console.setDisabled(!GEConfig.CONS_ENABLED);
        console.resetInputProcessing();



        //Script Engine Init
        scriptEngine = new ScriptingEngine();
        scriptEngine.init();

        //Game Screen manager
        gameScreenManager = new GameScreenManager();
        gameScreenManager.init();



        // RUN DEFAULT MAIN SCRIPT
        /*try{
            scriptEngine.("scripts/main.groovy");
        }catch(Exception e){
            console.log(e.getMessage(), Console.LogLevel.ERROR);
        }*/

        initialised = true;

        Logger.info("Engine initialisation complete");
    }


    /**
     * Updates the engine for ONE frame
     */
    public static void update(){

        if(!initialised){
            throw new UninitializedEngineException();
        }

        float deltaTime = Gdx.graphics.getDeltaTime();

        //Script Engine
        //TODO Update?

        //Clears the screen
        graphicsEngine.clearScreen();

        if(gameScreenManager.isRunning()){
            //Game Screen Manager
            gameScreenManager.handleEvents();
            gameScreenManager.update(deltaTime);
        }
        gameScreenManager.draw(deltaTime);

        //Draw Console

        console.refresh();
        console.draw();
    }

    /**
     * Cleans up the Engine before close
     */
    public static void cleanUp(){
        //GameScreen Manager
        gameScreenManager.cleanUp();

        //Dispose Script
        scriptEngine.dispose();

        //Dispose Console
        console.dispose();
    }





    static class UninitializedEngineException extends RuntimeException{
        public UninitializedEngineException(){
            super("Goat Engine uninitialized, use GoatEngine.init() at start of program");
        }
    }

}
