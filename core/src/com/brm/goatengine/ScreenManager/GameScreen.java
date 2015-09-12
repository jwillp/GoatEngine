package com.brm.GoatEngine.ScreenManager;

import com.brm.GoatEngine.ECS.common.TagsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;
import com.brm.GoatEngine.ScriptingEngine.ScriptSystem;
import com.brm.GoatEngine.ECS.core.ECSManager;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.TmxSupport.MapConfig;
import com.brm.GoatEngine.TmxSupport.MapConfigObject;
import com.brm.GoatEngine.Utils.Logger;
import com.brm.GoatEngine.Utils.OrderedProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

public class GameScreen{

    protected ECSManager ecsManager = new ECSManager();
    private String name;

    private String tmxConfigFile;

    private MapConfig mapConfig;

    public GameScreen(final String name){
        this.name = name;
    }


    private void loadConfigFile(){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(GEConfig.SCRN_MNGR_DIR + this.name);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            this.mapConfig = new MapConfig(GEConfig.SCRN_MNGR_MAP_DIR + prop.getProperty("map_config_file")); // Required

        } catch (FileNotFoundException e) {
            GameScreenNotFoundException exception = new GameScreenNotFoundException(this.name);
            Logger.fatal(exception.getMessage());
            throw exception;
        } catch (IOException e) {
            Logger.fatal(e.getMessage());
            Logger.logStackTrace(e);
        }
    }



    public void pause() {
        Logger.info("Game Screen: " + this.name + "paused");
    }

    public void init(GameScreenManager screenManager) {
        Logger.info("Game Screen: " + this.name + " initialisation ... ");

        //READ data from Config file
        this.loadConfigFile();

        // The Default Script System
        GoatEngine.eventManager.registerListener(this.ecsManager.getSystemManager());
        ecsManager.getSystemManager().addSystem(ScriptSystem.class, new ScriptSystem());

        // Load Map Config File
        for(MapConfigObject obj : this.mapConfig.read()){
            Entity entity = ecsManager.getEntityManager().createEntity();
            if(obj.tag != null){
                ((TagsComponent)entity.getComponent(TagsComponent.ID)).addTag(obj.tag);
            }
            if(obj.script != null){
                ((ScriptComponent)entity.getComponent(ScriptComponent.ID)).addScript(obj.script);
            }
        }

        Logger.info("Game Screen: " + this.name + " initialised");
    }

    public void cleanUp() {

    }

    public void resume() {
        Logger.info("Game Screen: " + this.name + "resumed");
    }


    public void handleInput(GameScreenManager screenManager){}

    public void update(GameScreenManager screenManager, float deltaTime){}

    public void draw(GameScreenManager screenManager, float deltaTime){}



    public EntityManager getEntityManager(){
        return this.ecsManager.getEntityManager();
    }


    private class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super("Could not find game screen : " + name + " File not found : " + GEConfig.SCRN_MNGR_DIR + name);
        }
    }
}
