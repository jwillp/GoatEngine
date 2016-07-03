package com.goatgames.goatengine.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.PrefabFactory;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.physics.BodyDefFactory;
import com.goatgames.goatengine.physics.PhysicsBodyDef;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.scriptingengine.lua.LuaEntityScriptComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class responsible for loading TMX Maps into ECS
 */
public class TmxMapLoader{

    private final EntityManager entityManager;
    private PrefabFactory prefabFactory;

    public TmxMapLoader(EntityManager entityManager){
        this.entityManager = entityManager;
        // Prefab factory if an object has a prefab
        prefabFactory = new PrefabFactory();

    }


    /**
     * Loads a TMX File
     * @param tmxFile
     * @return the loaded map
     */
    public TiledMap loadMap(String tmxFile){
        TiledMap map = GoatEngine.resourceManager.getMap(tmxFile);

        // Get map objects
        MapObjects mapObjects = map.getLayers().get("objects").getObjects();
        int tileSize = map.getProperties().get("tilewidth", Integer.class);


        int mapObjectsCount = mapObjects.getCount();
        for(int i = 0; i< mapObjectsCount; i++) {
            Entity entity = loadEntity(mapObjects.get(i), tileSize);
            entityManager.freeEntityObject(entity);
        }

        return map;
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
        float width = rect.getWidth() / tileSize;
        float height = rect.getHeight() / tileSize;
                                              // Box2D body Position is in center, Tiled is bottom left
        float posX = rect.getX() / tileSize + width * 0.5f;
        float posY = rect.getY() / tileSize + height *0.5f;


        Entity entity;

        // 1. If there is a prefab create from prefab
        String prefab = objProperties.get("prefab", "", String.class);
        if(!prefab.isEmpty()){
            GAssert.that(GoatEngine.fileManager.getFileHandle(prefab).exists(),
                         String.format("Cannot create entity from prefab '%s'. File does not exist", prefab));
            entity = prefabFactory.createEntity(prefab);
        }else{
            entity = entityManager.createEntity();
        }
        if(!GAssert.notNull(entity, "entity == null")){
            return null;
        }
        // 2. handle transform component. (it is automatically added)
        // We don't make a check to see if it was already added (by prefab). We want to override it
        // to use the editor's data.
        TransformComponent transformComponent = new TransformComponent(posX, posY, width, height);
        entity.addComponent(transformComponent, TransformComponent.ID);

        // 3. handle scripts
        String scripts = objProperties.get("scripts", "",String.class);
        if(!scripts.isEmpty()){
            LuaEntityScriptComponent scriptComponent;
            if (entity.hasComponent(LuaEntityScriptComponent.ID))
                scriptComponent = (LuaEntityScriptComponent) entity.getComponent(LuaEntityScriptComponent.ID);
            else
                scriptComponent = new LuaEntityScriptComponent(true);

            scriptComponent.addScripts(new ArrayList<>(Arrays.asList(scripts.split(";"))));
            entity.addComponent(scriptComponent,LuaEntityScriptComponent.ID);
        }

        // 4. handle physics
        boolean hasPhysics = Boolean.parseBoolean(objProperties.get("physics", "false", String.class));
        if(hasPhysics){
            String bodyType = objProperties.get("bodyType", "StaticBody", String.class);
            boolean isSensor = objProperties.get("isSensor", false, Boolean.class);
            // Create body
            PhysicsBodyDef bodyDef = BodyDefFactory.createStaticBox(width, height,isSensor);
            bodyDef.type = BodyDef.BodyType.valueOf(bodyType);
            PhysicsComponent physicsComponent = new PhysicsComponent(bodyDef);
            entity.addComponent(physicsComponent, PhysicsComponent.ID);
        }

        return entity;
    }




}











