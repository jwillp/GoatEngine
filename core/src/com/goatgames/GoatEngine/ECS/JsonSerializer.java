package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentMap;
import com.goatgames.goatengine.ecs.core.EntityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * PRovides functionalites to convert entities to JSON
 */
public class JsonSerializer implements Serializer{


    /**
     * Converts a group of entities to JSON
     * @param entities an array of entities to convert
     * @return JsonObject representation of entities
     */
    public JsonArray entitiesToJson(Array<Entity> entities){
        JsonArray jsonEntities = new JsonArray();
        for(Entity e: entities){
            jsonEntities.add(entityToJson(e));
        }
        return jsonEntities;
    }


    /**
     * Converts an entity with it's component to JSON
     * @param entity the entity to convert
     * @return JsonObject representation of entity
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
     * Converts a component to JSON
     * @param component the component to convert
     * @return JsonObject representation of component
     */
    public JsonObject componentToJson(EntityComponent component){
        Map<String,String> map = component.toMap();
        JsonObject jsonComponent = Json.object();
        for(String key: map.keySet()){
            jsonComponent.add(key, map.get(key));
        }
        return jsonComponent;
    }


    /**
     * Converts a component to JSON
     * @param manager the component to convert
     * @return JsonObject representation of components
     */
    public JsonObject levelToJson(EntityManager manager){
        Array<Entity> entities = manager.getEntities();
        JsonArray jEntities = entitiesToJson(entities);
        JsonObject jLevel = Json.object();
        for(Entity e : entities){
            manager.freeEntityObject(e);
        }
        return jLevel.add("entities", jEntities);
    }

    @Override
    public String serializeEntity(Entity e) {
        return entityToJson(e).toString();
    }

    @Override
    public String serializeComponent(EntityComponent c) {
        return componentToJson(c).toString();
    }

    @Override
    public String serializeEntities(Array<Entity> entities) {
        return entitiesToJson(entities).toString();
    }

    @Override
    public String serializeLevel(EntityManager manager) {
        return levelToJson(manager).toString();
    }


    // DESERIALIZATION //

    @Override
    public EntityComponentMap deserializeComponent(String c) {
        // Call Factory
        // Convert json string to map representation
        return deserializeComponent(Json.parse(c).asObject());
    }

    private EntityComponentMap deserializeComponent(JsonObject jsComponent){
        EntityComponentMap map = new EntityComponentMap();
        for(JsonObject.Member member: jsComponent){
            map.put(member.getName(), member.getValue().asString());
        }
        return map;
    }



    @Override
    public Map<String, EntityComponentMap> deserializeEntity(String e) {
        return deserializeEntity(Json.parse(e).asObject());
    }

    private Map<String, EntityComponentMap> deserializeEntity(JsonObject jsEntity){
        JsonArray jsComponents = jsEntity.get("components").asArray();
        Map<String, EntityComponentMap> components = new HashMap<>(jsComponents.size());
        for(JsonValue jsComponent: jsComponents){
            EntityComponentMap map = deserializeComponent(jsComponent.asObject());
            components.put(map.get("component_id"), map);
        }
        return components;
    }


    @Override
    public Map<String, Map<String, EntityComponentMap>> deserializeEntities(String entities) {
        return deserializeEntities(Json.parse(entities).asArray());
    }

    private Map<String, Map<String, EntityComponentMap>> deserializeEntities(JsonArray jsEntities){
        Map<String, Map<String, EntityComponentMap>> entities = new HashMap<>(jsEntities.size());
        for(JsonValue v : jsEntities){
            JsonObject entity = v.asObject();
            entities.put(entity.get("id").toString(),deserializeEntity(entity));
        }
        return entities;
    }

    @Override
    public Map<String, Map<String, EntityComponentMap>> deserializeLevel(String level) {
        JsonObject jsLevel = Json.parse(level).asObject();
        JsonArray jsEntities = jsLevel.get("entities").asArray();
        return deserializeEntities(jsEntities);
    }


    /**
     * Converts a map to JSON string
     * @return
     */
    public static String mapToJsonString(Map<String, String> map){
        return mapToJson(map).toString();
    }

    public static JsonObject mapToJson(Map<String, String> map){
        JsonObject jsonMap = Json.object();
        for(String key: map.keySet()){
            jsonMap.add(key, map.get(key));
        }
        return jsonMap;
    }


}
