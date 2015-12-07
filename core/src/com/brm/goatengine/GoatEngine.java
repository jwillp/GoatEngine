package com.brm.GoatEngine;

import com.badlogic.gdx.Gdx;
import com.brm.GoatEngine.EventManager.EventManager;
import com.brm.GoatEngine.GConsole.DefaultCommands.ExitCommand;
import com.brm.GoatEngine.GConsole.DefaultCommands.HelpCommand;
import com.brm.GoatEngine.GConsole.GConsole;
import com.brm.GoatEngine.GraphicsRendering.GraphicsEngine;
import com.brm.GoatEngine.Input.InputManager;
import com.brm.GoatEngine.LevelEditor.ConsoleCommands.ShowLevelEditor;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
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


    // LevelEditor
    private static LevelEditor levelEditor;


    // TODO NetworkManager ?

    private static boolean initialised = false;
    private static boolean running = false;


    /**
     * This initializes the Game Engine
     */
    public static void init(){
        Logger.info("Engine Initialisation ...");

        // Load configuration
        GEConfig.loadConfig();
        Logger.info(" > Engine config read and applied");

        //Graphics Engine
        graphicsEngine = new GraphicsEngine();
        Logger.info(" > Graphics Engine initialised");

        // Event Manager
        eventManager = new EventManager();
        Logger.info(" > Event Manager initialised");

        // input manager
        inputManager = new InputManager();
        inputManager.init();
        Logger.info(" > Input Manager initialised");

        // Audio Manager
        audioMixer = new AudioMixer();
        Logger.info(" > Audio Manager initialised");

        //Init the console
        console = new GConsole();
        console.setDisabled(!GEConfig.Console.CONS_ENABLED);
        console.log("Dev Console initialised", Console.LogLevel.SUCCESS);
        if(GEConfig.Console.CONS_ENABLED)
            Logger.info(" > Dev Console initialised");



        //Script Engine Init
        scriptEngine = new ScriptingEngine();
        scriptEngine.init();
        Logger.info(" > Scripting Engine initialised");



        //Game Screen manager
        gameScreenManager = new GameScreenManager();
        gameScreenManager.init();
        Logger.info(" > Game screen Manager initialised");

        // Level Editor
        levelEditor = new LevelEditor();

        // RUN DEFAULT MAIN SCRIPT
        console.addCommand(new ExitCommand());
        console.addCommand(new HelpCommand());
        console.addCommand(new ShowLevelEditor());
        /*try{
            scriptEngine.("scripts/main.groovy");
        }catch(Exception e){
            console.log(e.getMessage(), Console.LogLevel.ERROR);
        }*/

        initialised = true;
        running = true;

        Logger.info("Engine initialisation complete");
    }


    /**
     * Updates the engine for ONE frame
     */
    public static void update(){
        if(running){
            if(!initialised){
                throw new EngineUninitializedException();
            }

            float deltaTime = Gdx.graphics.getDeltaTime();

            //Clears the screen
            graphicsEngine.clearScreen();

            if(gameScreenManager.isRunning()){
                //Game Screen Manager
                gameScreenManager.handleEvents();
                gameScreenManager.update(deltaTime);
            }
            gameScreenManager.draw(deltaTime);

            levelEditor.update(deltaTime);

            //Draw Console
            console.refresh();
            console.draw();
        }
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


    public static void showLevelEditor(){
        levelEditor.setEnabled(true);
    }

    static class EngineUninitializedException extends RuntimeException{
        public EngineUninitializedException(){
            super("Goat Engine uninitialized, use GoatEngine.init() at start of program");
        }
    }

}
