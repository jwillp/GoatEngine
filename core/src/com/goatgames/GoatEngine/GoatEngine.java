package com.goatgames.goatengine;

import com.badlogic.gdx.Gdx;
import com.goatgames.gdk.GAssert;
import com.goatgames.gdk.logger.ILogger;
import com.goatgames.goatengine.config.engine.GEConfig;
import com.goatgames.goatengine.ecs.io.PrefabFactory;
import com.goatgames.goatengine.eventmanager.EngineEventManager;
import com.goatgames.goatengine.eventmanager.engineevents.LateUpdateEvent;
import com.goatgames.gdk.io.IFileManager;
import com.goatgames.goatengine.graphicsrendering.GraphicsEngine;
import com.goatgames.goatengine.input.InputManager;
import com.goatgames.goatengine.screenmanager.GameScreenManager;
import com.goatgames.goatengine.scriptingengine.common.IScriptingEngine;
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
    /**
     * Represents the current build number of the engine.
     * By convention it is a date with the following format YY.MM.DD
     */
    public static String BUILD_VERSION = "16.02.12_beanshell";

    /**
     * Configuration of the engine
     */
    public final static GEConfig config = new GEConfig();

    /**
     * Indicates whether or not the engine has been initialised
     */
    private static boolean initialised = false;

    /**
     * Indicates whether or not the engine is currently running.
     */
    private static boolean running = false;

    // MODULES BEGIN
    //ScreenManager
    public static GameScreenManager gameScreenManager;

    //Event Manager
    public static EngineEventManager eventManager;

    //Music and Sound Manager
    public static AudioMixer audioMixer;

    //InputManager
    public static InputManager inputManager = null;

    //Graphics Engine
    public static GraphicsEngine graphicsEngine;

    //Asset Manager
    public static ResourceManager resourceManager;

    // Blackboard
    public static Blackboard blackboard;

    public static IFileManager fileManager = null;

    public static ILogger logger = null;

    public static PrefabFactory prefabFactory = null;

    public static IScriptingEngine scriptingEngine = null;
    // TODO NetworkManager ?


    // MODULES END


    /**
     * Used for performance profiling
     */
    private static Timer performanceTimer = new Timer();

    /**
     * Timer used to display the current FPS
     * once every second in the title bar
     */
    private static Timer devCrxStatsTimer;

    /**
     * For reuse
     */
    private static final LateUpdateEvent lateUpdateEvent = new LateUpdateEvent();

    /**
     * The implementation specification of the engine
     */
    public static GEImplSpecs specs;


    /**
     * This initializes the Game Engine
     */
    public static void init(){


        if(specs == null){
            specs = new DefaultGEImplSpecs();
        }

        fileManager = specs.getFileManager();
        logger = specs.getLogger();
        prefabFactory = specs.getPrefabFactory();
        inputManager = specs.getInputManager();
        scriptingEngine = specs.getScriptingEngine();

        GAssert.setLogger(logger);

        performanceTimer.start();

        // Load configuration
        config.load();
        GoatEngine.logger.info(" > Engine config read and applied " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        GoatEngine.logger.info("Engine Initialisation ...");

        if(config.dev_ctx){
            devCrxStatsTimer = new Timer(Timer.ONE_SECOND);
        }

        // Blackboard
        blackboard = new Blackboard();

        // Graphics Engine
        graphicsEngine = new GraphicsEngine();
        graphicsEngine.init();
        GoatEngine.logger.info(" > Graphics Engine initialised "+ performanceTimer.getDeltaTime() + "ms");


        // Event Manager
        eventManager = new EngineEventManager();
        GoatEngine.logger.info(" > Event Manager initialised "+ performanceTimer.getDeltaTime() + "ms");
        //eventManager.registerListener(profiler);
        performanceTimer.reset();

        // Input manager
        if(GAssert.notNull(inputManager, "input manager == null")){
            inputManager.init();
            GoatEngine.logger.info(" > Input Manager initialised "+ performanceTimer.getDeltaTime() + "ms");
            performanceTimer.reset();
        }

        // Audio Manager
        audioMixer = new AudioMixer();
        GoatEngine.logger.info(" > Audio Manager initialised "+ performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();


        // Asset Manager
        resourceManager = new ResourceManager();
        resourceManager.init();
        GoatEngine.logger.info("> Asset Manager initialised " + performanceTimer.getDeltaTime() + " ms ");
        performanceTimer.reset();

        // Game Screen manager
        gameScreenManager = new GameScreenManager(specs.getGameScreenLoader());
        gameScreenManager.init();
        GoatEngine.logger.info(" > Game screen Manager initialised " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        initialised = true;
        running = true;

        GoatEngine.logger.info("Engine initialisation complete " + performanceTimer.getRunningTime() + "ms");
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
            //eventManager.fireEvent(new EngineEvent.GameTickBeginEvent(), false);

            updateDevCtxStats();


            float deltaTime = Gdx.graphics.getDeltaTime();
            if(gameScreenManager.isRunning()){
                //Game Screen Manager
                //eventManager.fireEvent(new EngineEvent.LogicTickBeginEvent(), false);
                gameScreenManager.handleEvents();
                gameScreenManager.update(deltaTime);
                //eventManager.fireEvent(new EngineEvent.LogicTickEndEvent(), false);
                resourceManager.update();
                eventManager.fireEvent(lateUpdateEvent);
            }
            gameScreenManager.draw(deltaTime);
            //eventManager.fireEvent(new EngineEvent.RenderTickEndEvent(), false);

            //eventManager.fireEvent(new EngineEvent.GameTickEndEvent());
        }else{
            Gdx.app.exit();
        }
    }


    /**
     * When dev ctx enabled updates statistics
     */
    public static void updateDevCtxStats(){
        if(config.dev_ctx){
            if(devCrxStatsTimer.isDone()){
                devCrxStatsTimer.reset();
                int currentFPS = Gdx.graphics.getFramesPerSecond();
                String gameTitle = config.game.name;
                String windowTitle = String.format("%s[%dx%d] %d FPS",
                        gameTitle, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), currentFPS);
                Gdx.graphics.setTitle(windowTitle);
            }
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

        //Dispose resources
        resourceManager.dispose();
    }

    static class EngineUninitializedException extends RuntimeException{
        public EngineUninitializedException(){
            super("Goat Engine uninitialized, use GoatEngine.init() at start of program");
        }
    }

}
