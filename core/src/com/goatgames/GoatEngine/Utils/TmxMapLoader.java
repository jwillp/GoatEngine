package com.goatgames.goatengine.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.PrefabFactory;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.files.FileSystem;
import com.goatgames.goatengine.physics.BodyDefFactory;
import com.goatgames.goatengine.physics.PhysicsBodyDef;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class responsible for loading TMX Maps into ECS
 */
public class TmxMapLoader{

    private final EntityManager entityManager;

    public TmxMapLoader(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    /**
     * Loads a TMX File
     * @param tmxFile
     * @return true if successful loading
     */
    public boolean loadMap(String tmxFile){
        GAssert.that(FileSystem.getFile(tmxFile).exists(), String.format("TMX Map file does not exist: %s", tmxFile));
        TiledMap map = GoatEngine.resourceManager.getMap(tmxFile);

        // Get map objects
        MapObjects mapObjects = map.getLayers().get("objects").getObjects();
        int tileSize = map.getProperties().get("tilewidth", Integer.class);

        // Prefab factory if an object has a prefab
        PrefabFactory prefabFactory = new PrefabFactory();


        int mapObjectsCount = mapObjects.getCount();
        for(int i = 0; i< mapObjectsCount; i++) {
            Entity entity = loadEntity(mapObjects.get(i), tileSize);
            entityManager.freeEntityObject(entity);
        }

        return true;
    }


    /**
     * Creates an entity from a map object
     * @param mapObject the map object to convert to an entity
     */
    public Entity loadEntity(MapObject mapObject, int tileSize){
        GAssert.notNull(mapObject, "mapObject == null. Cannot construct an entity from a null map object");

        // Map object data
        RectangleMapObject obj = (RectangleMapObject) mapObject;
        MapProperties objProperties = obj.getProperties();

        Rectangle rect = obj.getRectangle();
        float posX = rect.getX() / tileSize;
        float posY = rect.getY() / tileSize;

        Entity entity;

        // 1. If there is a prefab create from prefab
        String prefab = objProperties.get("prefab", "", String.class);
        if(!prefab.isEmpty()){
            GAssert.that(FileSystem.getFile(prefab).exists(),
                         String.format("Cannot create entity from prefab '%s'. File does not exist", prefab));
            entity = new PrefabFactory().createEntity(prefab);
        }else{
            entity = entityManager.createEntity();
        }

        // 2. handle transform component. (it is automatically added)
        // We don't make a check to see if it was already added (by prefab). We want to override it
        // to use the editor's data.
        TransformComponent transformComponent = new TransformComponent(posX, posY, rect.getWidth(), rect.getHeight());
        entity.addComponent(transformComponent, TransformComponent.ID);

        // 3. handle scripts
        String scripts = objProperties.get("scripts", "",String.class);
        if(!scripts.isEmpty()){
            ScriptComponent scriptComponent;
            scriptComponent = entity.hasComponent(ScriptComponent.ID) ?
                    (ScriptComponent) entity.getComponent(ScriptComponent.ID) :
                    new ScriptComponent(true);

            scriptComponent.addScripts(new ArrayList<>(Arrays.asList(scripts.split(";"))));
        }

        // 4. handle physics
        boolean hasPhysics = objProperties.get("physics", false, Boolean.class);
        if(hasPhysics){
            String bodyType = objProperties.get("bodyType", "StaticBody", String.class);
            boolean isSensor = objProperties.get("isSensor", false, Boolean.class);
            // Create body
            World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
            PhysicsBodyDef bodyDef = BodyDefFactory.createStaticBox(rect.getWidth(), rect.getHeight(),isSensor);
            bodyDef.type = BodyDef.BodyType.valueOf(bodyType);
            PhysicsComponent physicsComponent = new PhysicsComponent(bodyDef);
            entity.addComponent(physicsComponent, PhysicsComponent.ID);
        }

        return entity;
    }




}











