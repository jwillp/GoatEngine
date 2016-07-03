package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.prefabs.PrefabFactory;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.graphicsrendering.camera.CameraComponent;
import com.goatgames.goatengine.screenmanager.GameScreen;
import com.goatgames.gdk.GAssert;

/**
 * Native CtxAPI API
 */
public class CtxAPI {

    private final Entity entity;

    public CtxAPI(Entity entity) {
        this.entity = entity;
        GAssert.notNull(entity, "Entity was null cannot be added to CtxAPI");
    }

    public Entity getEntity(){
        return entity;
    }
    public GameScreen getCurrentGameScreen(){
        return GoatEngine.gameScreenManager.getCurrentScreen();
    }

    public EntityManager getEntityManager(){
        return getCurrentGameScreen().getEntityManager();
    }

    public World getPhsyicsWorld(){
        return getCurrentGameScreen().getPhysicsSystem().getWorld();
    }

    public OrthographicCamera getCamera() {
        Array<EntityComponent> comps = getCurrentGameScreen().getEntityManager().getComponents(CameraComponent.ID);
        CameraComponent camComp = (CameraComponent) comps.get(0);
        GAssert.notNull(camComp, "Camera Component was null, cannot create Lua Library Ctx");
        return camComp.getCamera();
    }

    /**
     * Create an entity from a prefab
     */
    public Entity createEntityFromPrefab(String prefabFile) {
        String prefab = prefabFile;
        final String DATA_DIR = GoatEngine.config.getString("data_directory");
        if(!prefab.startsWith(DATA_DIR + "prefabs")){
            prefab = DATA_DIR + "prefabs/" + prefab;
        }
        return GoatEngine.prefabFactory.createEntity(prefab,getEntityManager());
    }

    public Entity createEntity() {
        GameScreen currentGameScreen = GoatEngine.gameScreenManager.getCurrentScreen();
        return currentGameScreen.getEntityManager().createEntity();
    }

    public void destroyEntity(String entityId){
        GameScreen currentGameScreen = GoatEngine.gameScreenManager.getCurrentScreen();
        currentGameScreen.getEntityManager().deleteEntity(entityId);
    }
}
