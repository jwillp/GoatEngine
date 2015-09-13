package com.brm.GoatEngine.ScreenManager;

import com.badlogic.gdx.math.Vector2;
import com.brm.GoatEngine.ECS.common.TagsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.Physics.PhysicsSystem;
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
import java.util.ArrayList;

public class GameScreen{

    protected ECSManager ecsManager = new ECSManager();
    private String name;

    private String tmxConfigFile;

    private MapConfig mapConfig;
    private PhysicsSystem physicsSystem;

    public GameScreen(final String name){
        this.name = name;
    }



    public void pause() {
        Logger.info("Game Screen: " + this.name + "paused");
    }

    public void init(GameScreenManager screenManager) {
        Logger.info("Game Screen: " + this.name + " initialisation ... ");

        // The Default Script System
        GoatEngine.eventManager.registerListener(this.ecsManager.getSystemManager());
        ecsManager.getSystemManager().addSystem(ScriptSystem.class, new ScriptSystem());
        ecsManager.getSystemManager().addSystem(PhysicsSystem.class, new PhysicsSystem());


        //READ data from Config file
        loadConfigFile();



        // Apply Map Configuration
        applyMapConfig();

        Logger.info("Game Screen: " + this.name + " initialised");
    }

    public void cleanUp() {
        Logger.info("Game Screen: " + this.name + "cleaned up");
    }

    public void resume() {
        Logger.info("Game Screen: " + this.name + "resumed");
    }


    public void handleInput(GameScreenManager screenManager){}

    public void update(GameScreenManager screenManager, float deltaTime){
        ecsManager.getSystemManager().getSystem(ScriptSystem.class).update(0);
    }

    public void draw(GameScreenManager screenManager, float deltaTime){}


    /**
     * Reads the game screen config file
     */
    private void loadConfigFile(){
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(GEConfig.SCRN_MNGR_DIR + this.name);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            this.mapConfig = new MapConfig(GEConfig.SCRN_MNGR_MAP_DIR + prop.getProperty("map_config_file")); // Required

            //Gravity
            Vector2 gravity = new Vector2();
            if(prop.getProperty("gravity_x") != null){
                gravity.x = Float.parseFloat(prop.getProperty("gravity_x"));
            }
            if(prop.getProperty("gravity_y") != null){
                gravity.y = Float.parseFloat(prop.getProperty("gravity_y"));
            }
            this.ecsManager.getSystemManager().getSystem(PhysicsSystem.class).setGravity(gravity);



        } catch (FileNotFoundException e) {
            GameScreenNotFoundException exception = new GameScreenNotFoundException(this.name);
            Logger.fatal(exception.getMessage());
            throw exception;
        } catch (IOException e) {
            Logger.fatal(e.getMessage());
            Logger.logStackTrace(e);
        }
    }


    /**
     * Applies the info (add entities/objects) read from
     * the Map Config file
     */
    private void applyMapConfig(){
        // Load Map Config File
        ArrayList<MapConfigObject> objects =  this.mapConfig.read();

        for(MapConfigObject obj : objects){
            Entity entity = ecsManager.getEntityManager().createEntity();
            if(obj.tag != null){
                ((TagsComponent)entity.getComponent(TagsComponent.ID)).addTag(obj.tag);
            }
            if(obj.script != null){
                ((ScriptComponent)entity.getComponent(ScriptComponent.ID)).addScript(
                        GEConfig.SCRPT_ENG_SCRIPTS_DIR + obj.script
                );
            }
        }

        Logger.info("> Number of entity added: " + objects.size());
    }





    public EntityManager getEntityManager(){
        return this.ecsManager.getEntityManager();
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }


                             // EXCEPTIONS //

    private class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super("Could not find game screen : " + name + " File not found : " + GEConfig.SCRN_MNGR_DIR + name);
        }
    }
}
