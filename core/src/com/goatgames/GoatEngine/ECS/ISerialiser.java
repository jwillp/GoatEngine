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
public interface ISerialiser {

    /**
     * Serialises an Entity and returns it as a String
     * @param e entity to serialise
     * @return serialised entity as a String
     */
    String serialiseEntity(Entity e);

    /**
     * Serialises a Component and returns it as a String
     * @param c component to serialise
     * @return serialised component as a string
     */
    String serialiseComponent(EntityComponent c);

    /**
     * Serialises a Group of Entities
     * @param entities an array of entities
     * @return serialised group of entities as a string
     */
    String serialiseEntities(Array<Entity> entities);

    /**
     * Serialises every entity of an EntityManager
     * and returns it as a string
     * @param manager the manager to serialise
     * @return serialised manager as a string
     */
    String serialiseLevel(EntityManager manager);

    /**
     * Deserialises an entity and returns it as a Map of normalised entity component
     * where the key is the component id and the value the map representation of that
     * particular component
     * @param e entity to deserialise
     * @return deserialised entity as a String
     */
    Map<String, NormalisedEntityComponent> deserialiseEntity(String e);

    /**
     * Deserialises a Component and returns it as a Map representation
     * @param c component to deserialise
     * @return deserialised component as a string
     */
    NormalisedEntityComponent deserialiseComponent(String c);

    /**
     * Deserialises a Group of Entities and returns it as a Map of Map of normalised entity
     * component representation.
     * The key of the enclosing map is the id of the entity.
     * The second map is a map where the key is the entity and the value it's components
     * @param entities an array of entities
     * @return deserialised group of entities as a string
     */
    Map<String, Map<String, NormalisedEntityComponent>> deserialiseEntities(String entities);

    /**
     * Deserialises every entity of an EntityManager
     * and returns it as a string
     * @param level level string to deserialise
     * @return deserialised manager as a string
     */
    Map<String, Map<String, NormalisedEntityComponent>> deserialiseLevel(String level);
}
