package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;

import java.util.Map;

/**
 * Class responsible for Serializing an entity
 */
public interface Serializer {

    /**
     * Serializes an Entity and returns it as a String
     * @param e entity to serialize
     * @return serialized entity as a String
     */
    String serializeEntity(Entity e);

    /**
     * Serializes a Component and returns it as a String
     * @param c component to serialize
     * @return serialized component as a string
     */
    String serializeComponent(EntityComponent c);

    /**
     * Serializes a Group of Entities
     * @param entities an array of entities
     * @return serialized group of entities as a string
     */
    String serializeEntities(Array<Entity> entities);

    /**
     * Serializes every entity of an EntityManager
     * and returns it as a string
     * @param manager the manager to serialize
     * @return serialized manager as a string
     */
    String serializeLevel(EntityManager manager);


    /**
     * Deserialize an entity and returns it as a Map of map representation
     * where the key is the component id and the value the map representation of that
     * particular component
     * @param e entity to deserialize
     * @return deserialized entity as a String
     */
    Map<String, NormalisedEntityComponent> deserializeEntity(String e);

    /**
     * Deserializes a Component and returns it as a Map representation
     * @param c component to deserialize
     * @return deserialized component as a string
     */
    NormalisedEntityComponent deserializeComponent(String c);

    /**
     * Deserializes a Group of Entities and returns it as an map of Map of map representation
     * The key of the enclosing map is the id of the entity.
     * The second map is a map where the key is the entity and the value it's components
     * @param entities an array of entities
     * @return deserialized group of entities as a string
     */
    Map<String, Map<String, NormalisedEntityComponent>> deserializeEntities(String entities);

    /**
     * Deserializes every entity of an EntityManager
     * and returns it as a string
     * @param level level string to deserialize
     * @return deserialized manager as a string
     */
    Map<String, Map<String, NormalisedEntityComponent>> deserializeLevel(String level);


}
