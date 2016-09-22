package com.goatgames.goatengine.ecs.io;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides functionality to convert entities to JSON
 */
public class JsonSerialiser implements IECSSerialiser {

    /**
     * Converts a map to JSON string
     *
     * @return
     */
    public static String mapToJsonString(Map<String, String> map) {
        return mapToJson(map).toString();
    }

    public static JsonObject mapToJson(Map<String, String> map) {
        JsonObject jsonMap = Json.object();
        for (String key : map.keySet()) {
            jsonMap.add(key, map.get(key));
        }
        return jsonMap;
    }

    /**
     * Converts a group of entities to JSON
     *
     * @param entities an array of entities to convert
     * @return JsonObject representation of entities
     */
    public JsonArray entitiesToJson(Array<Entity> entities) {
        JsonArray jsonEntities = new JsonArray();
        for (Entity e : entities) {
            jsonEntities.add(entityToJson(e));
        }
        return jsonEntities;
    }

    /**
     * Converts an entity with it's component to JSON
     *
     * @param entity the entity to convert
     * @return JsonObject representation of entity
     */
    public JsonObject entityToJson(Entity entity) {
        ObjectMap<String, EntityComponent> components = entity.getComponents();
        JsonObject jsonEntity = Json.object();
        JsonArray jsonComponents = new JsonArray();
        jsonEntity.add("id", entity.getId());
        jsonEntity.add("components", jsonComponents);
        for (EntityComponent c : components.values()) {
            jsonComponents.add(componentToJson(c));
        }
        return jsonEntity;
    }

    /**
     * Converts a component to JSON
     *
     * @param component the component to convert
     * @return JsonObject representation of component
     */
    public JsonObject componentToJson(EntityComponent component) {
        Map<String, String> map = component.normalise();
        JsonObject jsonComponent = Json.object();
        for (String key : map.keySet()) {
            jsonComponent.add(key, map.get(key));
        }
        return jsonComponent;
    }

    /**
     * Converts a component to JSON
     *
     * @param manager the component to convert
     * @return JsonObject representation of components
     */
    public JsonObject levelToJson(EntityManager manager) {
        Array<Entity> entities = manager.getEntities();
        JsonArray jEntities = entitiesToJson(entities);
        JsonObject jLevel = Json.object();
        for (Entity e : entities) {
            manager.freeEntityObject(e);
        }
        return jLevel.add("entities", jEntities);
    }

    @Override
    public String serialiseEntity(Entity e) {
        return entityToJson(e).toString();
    }

    @Override
    public String serialiseComponent(EntityComponent c) {
        return componentToJson(c).toString();
    }


    // DESERIALIZATION //

    @Override
    public String serialiseEntities(Array<Entity> entities) {
        return entitiesToJson(entities).toString();
    }

    @Override
    public String serialiseLevel(EntityManager manager) {
        return levelToJson(manager).toString();
    }

    @Override
    public NormalisedEntityComponent deserialiseComponent(String c) {
        // Call Factory
        // Convert json string to map representation
        return deserializeComponent(Json.parse(c).asObject());
    }

    private NormalisedEntityComponent deserializeComponent(JsonObject jsComponent) {
        NormalisedEntityComponent map = new NormalisedEntityComponent();
        for (JsonObject.Member member : jsComponent) {
            map.put(member.getName(), member.getValue().asString());
        }
        return map;
    }

    @Override
    public Map<String, NormalisedEntityComponent> deserialiseEntity(String e) {
        return deserializeEntity(Json.parse(e).asObject());
    }

    private Map<String, NormalisedEntityComponent> deserializeEntity(JsonObject jsEntity) {
        JsonArray jsComponents = jsEntity.get("components").asArray();
        Map<String, NormalisedEntityComponent> components = new HashMap<>(jsComponents.size());
        for (JsonValue jsComponent : jsComponents) {
            NormalisedEntityComponent map = deserializeComponent(jsComponent.asObject());
            components.put(map.get("component_id"), map);
        }
        return components;
    }

    @Override
    public Map<String, Map<String, NormalisedEntityComponent>> deserialiseEntities(String entities) {
        return deserializeEntities(Json.parse(entities).asArray());
    }

    private Map<String, Map<String, NormalisedEntityComponent>> deserializeEntities(JsonArray jsEntities) {
        Map<String, Map<String, NormalisedEntityComponent>> entities = new HashMap<>(jsEntities.size());
        for (JsonValue v : jsEntities) {
            JsonObject entity = v.asObject();
            entities.put(entity.get("id").asString(), deserializeEntity(entity));
        }
        return entities;
    }

    @Override
    public Map<String, Map<String, NormalisedEntityComponent>> deserialiseLevel(String level) {
        if (level.isEmpty()) return new HashMap<>();
        JsonObject jsLevel = Json.parse(level).asObject();
        JsonArray jsEntities = jsLevel.get("entities").asArray();
        return deserializeEntities(jsEntities);
    }


}
