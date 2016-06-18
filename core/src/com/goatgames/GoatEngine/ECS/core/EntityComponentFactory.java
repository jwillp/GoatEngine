package com.goatgames.goatengine.ecs.core;

/**
 * The role of a Component Factory is to take a map representation and return a fully
 * constructed Component instance
 */
public interface EntityComponentFactory{

    /**
     * Takes a data map and tries to construct a component with it
     * if the data map was incompatible with the factory
     * return null
     * @param data a map representation of a component
     * @return A Constructed Component or null if it could not be constructed
     */
    EntityComponent processMapData(String componentId, NormalisedEntityComponent data);
}
