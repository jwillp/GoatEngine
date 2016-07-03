package com.goatgames.goatengine.screenmanager;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ai.AISystem;
import com.goatgames.goatengine.ecs.common.EntityDestructionSystem;
import com.goatgames.goatengine.ecs.core.ECSManager;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.graphicsrendering.RenderingSystem;
import com.goatgames.goatengine.input.InputSystem;
import com.goatgames.goatengine.physics.PhysicsSystem;
import com.goatgames.goatengine.scriptingengine.EntityScriptSystem;
import com.goatgames.goatengine.scriptingengine.lua.LuaEntityScriptSystem;
import com.goatgames.goatengine.ui.UIEngine;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.utils.TiledMapLoadedEvent;
import com.goatgames.goatengine.utils.TmxMapLoader;

import java.io.FileNotFoundException;

public final class GameScreen{

    protected ECSManager ecsManager = new ECSManager();
    private String name;

    private PhysicsSystem physicsSystem;

    private GameScreenConfig config;

    private UIEngine uiEngine;
    private boolean initialized = false;
    private RenderingSystem renderingSystem;

    public GameScreen(final String name){
        this.name = name;
        config = new GameScreenConfig(GoatEngine.config.getString("screens.directory") + this.name);
        uiEngine = new UIEngine();
    }


    public void init(GameScreenManager screenManager) {
        GoatEngine.logger.info(String.format("> Game Screen: %s initialisation ... ", this.name));

        //READ data from Config file
        loadConfigFile();



        // Initialise Systems
        GoatEngine.eventManager.registerListener(this.ecsManager.getSystemManager());

        ecsManager.getSystemManager().addSystem(InputSystem.class, new InputSystem());
        ecsManager.getSystemManager().addSystem(AISystem.class, new AISystem());
        ecsManager.getSystemManager().addSystem(EntityScriptSystem.class, new EntityScriptSystem());
        ecsManager.getSystemManager().addSystem(LuaEntityScriptSystem.class, new LuaEntityScriptSystem());
        ecsManager.getSystemManager().addSystem(EntityDestructionSystem.class, new EntityDestructionSystem());

        physicsSystem = new PhysicsSystem();
        ecsManager.getSystemManager().addSystem(PhysicsSystem.class, physicsSystem);
        renderingSystem = new RenderingSystem();
        ecsManager.getSystemManager().addSystem(RenderingSystem.class, renderingSystem);

        ecsManager.getSystemManager().initSystems();


        // Load Map
        loadMap();

        // Apply Level Configuration
        loadLevel();

        initialized = true;
        GoatEngine.eventManager.fireEvent(new GamScreenInitialisedEvent(this.name));
        GoatEngine.logger.info(String.format("> Game Screen: %s initialised", this.name));
    }

    public void cleanUp() {
        GoatEngine.logger.info(String.format("Game Screen: %s cleaned up", this.name));
    }

    public void pause() {
        GoatEngine.logger.info(String.format("Game Screen: %s paused", this.name));
    }

    public void resume() {
        GoatEngine.logger.info(String.format("Game Screen: %s resumed", this.name));
    }


    public void preUpdate(GameScreenManager screenManager){
        ecsManager.getSystemManager().preUpdate();
    }

    public void update(GameScreenManager screenManager, float deltaTime){
        ecsManager.getSystemManager().update();
    }

    public void draw(GameScreenManager screenManager, float deltaTime){
        uiEngine.render(deltaTime);
        ecsManager.getSystemManager().draw();
    }


    /**
     * Reads the game screen config file
     */
    private void loadConfigFile(){
        try {
            config.load();
        } catch (FileNotFoundException e) {
            GameScreenNotFoundException exception = new GameScreenNotFoundException(this.name);
            GoatEngine.logger.fatal(exception.getMessage());
            throw exception;
        }
    }


    /**
     * Applies the info (add entities/objects) read from
     * the Level Config file
     */
    private void loadLevel(){
        // Load Level Config File
        final String LEVEL_CONFIG = config.getString("level");
        if(GAssert.that(Gdx.files.internal(LEVEL_CONFIG).exists(),
                String.format("The Level could not be loaded the file \"%s\" does not exist.", LEVEL_CONFIG))){
            this.getEntityManager().loadLevel(LEVEL_CONFIG);
        }

        /*Timer t = new Timer(Timer.INFINITE);
        t.start();

        IniSerializer serializer = new IniSerializer(LEVEL_CONFIG, this.getEntityManager());
        Logger.info(String.format("Level read time: %d ms", t.getDeltaTime()));
        t.reset();

        serializer.load();
        Logger.info(String.format("Level load time: %d ms", t.getDeltaTime()));
        Logger.info(String.format("> Number of entity added: %d", getEntityManager().getEntityCount()));
        t.reset();


        // TEST TO JSON //
        ISerialiser ser = new JsonSerialiser();
        String s = ser.serialiseLevel(this.getEntityManager());
        Logger.info(String.format("Level WRITE time: %d ms", t.getDeltaTime()));
        Logger.info(s);*/
    }


    private void loadMap(){
        String TMX_FILE = config.getString("tmx_map");
        if(TMX_FILE.isEmpty()) return;
        String map = config.getString("tmx_map");
        TmxMapLoader loader = new TmxMapLoader(getEntityManager());
        GoatEngine.logger.info(String.format("Loading map %s ... ", TMX_FILE));
        loader.loadMap(map);
        GoatEngine.logger.info(String.format("%s Loaded", TMX_FILE));
        renderingSystem.setTiledMap(GoatEngine.resourceManager.getMap(map));
        GoatEngine.eventManager.fireEvent(new TiledMapLoadedEvent(map));
    }



    public EntityManager getEntityManager(){
        return this.ecsManager.getEntityManager();
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public GameScreenConfig getConfig() {
        return config;
    }

    public UIEngine getUiEngine() {
        return uiEngine;
    }

    public String getName() {
        return name;
    }

    public boolean isInitialized() {
        return initialized;
    }


    // EXCEPTIONS //

    public class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super(String.format("Could not find game screen : %s File not found : %s%s",
                    name, GoatEngine.config.getString("screens.directory"), name));
        }
    }
}
