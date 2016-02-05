package com.goatgames.goatengine;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.eventmanager.EventManager;
import com.goatgames.goatengine.gconsole.GConsole;
import com.goatgames.goatengine.gconsole.commands.*;
import com.goatgames.goatengine.graphicsrendering.GraphicsEngine;
import com.goatgames.goatengine.input.InputManager;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.goatgames.goatengine.leveleditor.consolecommands.ShowLevelEditor;
import com.goatgames.goatengine.screenmanager.GameScreenManager;
import com.goatgames.goatengine.scriptingengine.lua.LuaScriptingEngine;
import com.goatgames.goatengine.utils.EngineProfiler;
import com.goatgames.goatengine.utils.Logger;
import com.goatgames.goatengine.utils.Timer;

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
    public static LuaScriptingEngine scriptEngine;

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

    // Performance profiling
    private static Timer performanceTimer = new Timer();
    private static EngineProfiler profiler = new EngineProfiler();





    /**
     * This initializes the Game Engine
     */
    public static void init(){
        Logger.info("Engine Initialisation ...");
        performanceTimer.start();


        // Load configuration
        GEConfig.loadConfig();
        Logger.info(" > Engine config read and applied " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        // Graphics Engine
        graphicsEngine = new GraphicsEngine();
        graphicsEngine.init();
        Logger.info(" > Graphics Engine initialised "+ performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        // Event Manager
        eventManager = new EventManager();
        Logger.info(" > Event Manager initialised "+ performanceTimer.getDeltaTime() + "ms");
        //eventManager.registerListener(profiler);
        performanceTimer.reset();

        // Input manager
        inputManager = new InputManager();
        inputManager.init();
        Logger.info(" > Input Manager initialised "+ performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        // Audio Manager
        audioMixer = new AudioMixer();
        Logger.info(" > Audio Manager initialised "+ performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        // Init the console
        console = new GConsole();
        console.setDisabled(!GEConfig.Console.CONS_ENABLED);
        if(GEConfig.Console.CONS_ENABLED) {
            Logger.info(" > Dev Console initialised " + performanceTimer.getDeltaTime() + "ms");

            // Default commands
            console.addCommand(new ExitCommand());
            console.addCommand(new HelpCommand());
            console.addCommand(new ShowLevelEditor());
            console.addCommand(new ClearConsoleCommand());
            console.addCommand(new PauseEngineCommand());
            console.addCommand(new ResumeEngineCommand());
            console.addCommand(new ReloadScreenCommand());
            console.addCommand(new ChangeScreenCommand());
            console.addCommand(new LuaCommand());
        }
        performanceTimer.reset();


        // Script Engine Init
        scriptEngine = new LuaScriptingEngine();
        scriptEngine.init();
        Logger.info(" > Scripting Engine initialised " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();


        // Game Screen manager
        gameScreenManager = new GameScreenManager();
        gameScreenManager.init();
        Logger.info(" > Game screen Manager initialised " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        // Level Editor
        levelEditor = new LevelEditor();



        /*try{
            scriptEngine.("scripts/main.groovy");
        }catch(Exception e){
            console.log(e.getMessage(), Console.LogLevel.ERROR);
        }*/

        initialised = true;
        running = true;

        Logger.info("Engine initialisation complete " + performanceTimer.getRunningTime() + "ms");
        performanceTimer.reset();
    }


    /**
     * Updates the engine for ONE frame
     */
    public static void update(){
        if(running){
            if(!initialised){
                throw new EngineUninitializedException();
            }
            //eventManager.fireEvent(new EngineEvents.GameTickBeginEvent(), false);

            float deltaTime = Gdx.graphics.getDeltaTime();


            if(gameScreenManager.isRunning()){
                //Game Screen Manager
                //eventManager.fireEvent(new EngineEvents.LogicTickBeginEvent(), false);
                gameScreenManager.handleEvents();
                gameScreenManager.update(deltaTime);
                //eventManager.fireEvent(new EngineEvents.LogicTickEndEvent(), false);

            }
            //eventManager.fireEvent(new EngineEvents.RenderTickBeginEvent(), false);
            gameScreenManager.draw(deltaTime);
            //eventManager.fireEvent(new EngineEvents.RenderTickEndEvent(), false);


            levelEditor.update(deltaTime);

            //Draw Console
            console.refresh();
            console.draw();

            //eventManager.fireEvent(new EngineEvents.GameTickEndEvent());
        }else{
            Gdx.app.exit();
        }
    }


    public static void shutdown(){
        running = false;
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
