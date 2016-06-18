package com.goatgames.goatengine;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.eventmanager.EventManager;
import com.goatgames.goatengine.graphicsrendering.GraphicsEngine;
import com.goatgames.goatengine.input.InputManager;
import com.goatgames.goatengine.screenmanager.GameScreenManager;
import com.goatgames.goatengine.screenmanager.LateUpdateEvent;
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


    //ScreenManager
    public static GameScreenManager gameScreenManager;

    //Event Manager
    public static EventManager eventManager;

    //Music and Sound Manager
    public static AudioMixer audioMixer;

    //InputManager
    public static InputManager inputManager;

    //Graphics Engine
    public static GraphicsEngine graphicsEngine;

    //Asset Manager
    public static ResourceManager resourceManager;

    // Blackboard
    public static Blackboard blackboard;

    // TODO NetworkManager ?

    private static boolean initialised = false;
    private static boolean running = false;

    // Performance profiling
    private static Timer performanceTimer = new Timer();
    public final static GEConfig config = new GEConfig();

    private static Timer devCrxStatsTimer;

    private static final LateUpdateEvent lateUpdateEvent = new LateUpdateEvent();

    /**
     * This initializes the Game Engine
     */
    public static void init(){

        performanceTimer.start();

        // Load configuration
        config.load();
        Logger.info(" > Engine config read and applied " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

        Logger.info("Engine Initialisation ...");

        if(config.getBoolean("dev_ctx")){
            devCrxStatsTimer = new Timer(Timer.ONE_SECOND);
        }

        // Blackboard
        blackboard = new Blackboard();

        // Graphics Engine
        graphicsEngine = new GraphicsEngine();
        graphicsEngine.init();
        Logger.info(" > Graphics Engine initialised "+ performanceTimer.getDeltaTime() + "ms");


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


        // Asset Manager
        resourceManager = new ResourceManager();
        resourceManager.init();
        Logger.info("> Asset Manager initialised " + performanceTimer.getDeltaTime() + " ms ");
        performanceTimer.reset();

        // Game Screen manager
        gameScreenManager = new GameScreenManager();
        gameScreenManager.init();
        Logger.info(" > Game screen Manager initialised " + performanceTimer.getDeltaTime() + "ms");
        performanceTimer.reset();

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

            updateDevCtxStats();


            float deltaTime = Gdx.graphics.getDeltaTime();
            if(gameScreenManager.isRunning()){
                //Game Screen Manager
                //eventManager.fireEvent(new EngineEvents.LogicTickBeginEvent(), false);
                gameScreenManager.handleEvents();
                gameScreenManager.update(deltaTime);
                //eventManager.fireEvent(new EngineEvents.LogicTickEndEvent(), false);
                resourceManager.update();
                eventManager.fireEvent(lateUpdateEvent,false);
            }
            gameScreenManager.draw(deltaTime);
            //eventManager.fireEvent(new EngineEvents.RenderTickEndEvent(), false);

            //eventManager.fireEvent(new EngineEvents.GameTickEndEvent());
        }else{
            Gdx.app.exit();
        }
    }


    /**
     * When dev ctx enabled updates statistics
     */
    public static void updateDevCtxStats(){
        if(config.getBoolean("dev_ctx")){
            if(devCrxStatsTimer.isDone()){
                devCrxStatsTimer.reset();
                int currentFPS = Gdx.graphics.getFramesPerSecond();
                String gameTitle = config.getString("game.name");
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
