package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;

import java.util.Map;

/**
 * PRovides functionalites to convert entities to JSON
 */
public class ECSJsonSerializer{
    // The file to output the serialization
    private String outputPath;
    // The manager containing the data to export
    private EntityManager entityManager;



    public ECSJsonSerializer(final String outputPath, EntityManager manager){
        this.outputPath = outputPath;
        this.entityManager = manager;
    }



    public void writeFile(String file){

    }



    /**
     * Writes a group of entities to json
     * @param entities an array of entities to convert
     * @return
     */
    public JsonArray entitiesToJson(Array<Entity> entities){
        JsonArray jsonEntities = new JsonArray();
        for(Entity e: entities){
            jsonEntities.add(entityToJson(e));
        }
        return jsonEntities;
    }


    /**
     * Writes an entity with it's component to JSON
     * @param entity the entity to convert
     * @return
     */
    public JsonObject entityToJson(Entity entity){

        ObjectMap<String,EntityComponent> components = entity.getComponents();
        JsonObject jsonEntity = Json.object();
        JsonArray jsonComponents = new JsonArray();
        jsonEntity.add("id", entity.getID());
        jsonEntity.add("components", jsonComponents);
        for(EntityComponent c: components.values()){
            jsonComponents.add(componentToJson(c));
        }
        return jsonEntity;
    }

    /**
     * Writes a component to JSON
     * @param component the component to convert
     * @return
     */
    public JsonObject componentToJson(EntityComponent component){
        Map<String,String> map = component.toMap();
        JsonObject jsonComponent = Json.object();
        for(String key: map.keySet()){
            jsonComponent.add(key, map.get(key));
        }
        return jsonComponent;
    }




}
