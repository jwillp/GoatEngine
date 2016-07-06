package com.goatgames.goatengine.screenmanager;

import com.badlogic.gdx.Gdx;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ai.AISystem;
import com.goatgames.goatengine.ecs.common.EntityDestructionSystem;
import com.goatgames.goatengine.ecs.core.ECSManager;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.ecs.core.EntitySystemManager;
import com.goatgames.goatengine.files.IFileHandle;
import com.goatgames.goatengine.graphicsrendering.RenderingSystem;
import com.goatgames.goatengine.input.InputSystem;
import com.goatgames.goatengine.physics.PhysicsSystem;
import com.goatgames.goatengine.scriptingengine.EntityScriptSystem;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import com.goatgames.goatengine.ui.UIEngine;
import com.goatgames.goatengine.config.LuaEngineConfig;
import com.goatgames.goatengine.utils.TiledMapLoadedEvent;
import com.goatgames.goatengine.utils.TmxMapLoader;
import org.luaj.vm2.LuaTable;

import java.io.FileNotFoundException;

/**
 * Lua Based Game Screen
 */
public class LuaGameScreen extends LuaEngineConfig implements IGameScreen{

    protected ECSManager ecsManager = new ECSManager();
    private String name;

    private PhysicsSystem physicsSystem;

    private IGameScreenConfig config;

    private UIEngine uiEngine;
    private boolean initialised = false;
    private RenderingSystem renderingSystem;

    public LuaGameScreen(final String name){
        this.name = name;
        config = new LuaGameScreenConfig();
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

        // Scripting
        ecsManager.getSystemManager().addSystem(EntityScriptSystem.class, new EntityScriptSystem());

        EntitySystem scriptingSystem = GoatEngine.scriptingEngine.getScriptingSystem();
        ecsManager.getSystemManager().addSystem(scriptingSystem.getClass(), scriptingSystem);

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

        initialised = true;
        GoatEngine.eventManager.fireEvent(new GamScreenInitialisedEvent(this.name));
        GoatEngine.logger.info(String.format("> Game Screen: %s initialised", this.name));
    }

    @Override
    public void pause(GameScreenManager screenManager) {
        GoatEngine.logger.info(String.format("Game Screen: %s paused", this.name));
    }

    @Override
    public void resume(GameScreenManager screenManager) {
        GoatEngine.logger.info(String.format("Game Screen: %s resumed", this.name));
    }

    @Override
    public void cleanUp() {
        GoatEngine.logger.info(String.format("Game Screen: %s cleaned up", this.name));
    }

    @Override
    public void preUpdate(GameScreenManager screenManager){
        ecsManager.getSystemManager().preUpdate();
    }

    @Override
    public void update(GameScreenManager screenManager, float deltaTime){
        ecsManager.getSystemManager().update();
    }

    @Override
    public void draw(GameScreenManager screenManager, float deltaTime){
        uiEngine.render(deltaTime);
        ecsManager.getSystemManager().draw();
    }

    /**
     * Reads the game screen config file
     */
    private void loadConfigFile(){
        String configPath = GoatEngine.config.getString("screens.directory") + this.name;
        IFileHandle configHandle = GoatEngine.fileManager.getFileHandle(configPath);
        config.load(configHandle);
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

    @Override
    public EntitySystemManager getEntitySystemManager() {
        return ecsManager.getSystemManager();
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public IGameScreenConfig getConfig() {
        return config;
    }


    public String getName() {
        return name;
    }

    @Override
    public boolean isInitialised() {
        return initialised;
    }

    @Override
    public UIEngine getUIEngine() {
        return uiEngine;
    }
}
