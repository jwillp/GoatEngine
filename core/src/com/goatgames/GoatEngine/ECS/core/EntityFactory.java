package com.goatgames.goatengine.ecs.core;

import com.goatgames.goatengine.ecs.io.ComponentMapper;

import java.util.Map;

/**
 * Responsible of creating entities from Map representations
 */
public class EntityFactory {

    public void fromLevelData(Map<String, Map<String, NormalisedEntityComponent>> levelData, EntityManager manager){

        for(String entityId: levelData.keySet()){
            Entity e = fromComponentMap(entityId,levelData.get(entityId), manager);
            manager.freeEntityObject(e);
        }

    }

    /**
     * Creates an entity from a components map
     * @param components the components to add to the newly created entity
     * @param manager the entity manager that will store the entity and its component
     * @return
     */
    public Entity fromComponentMap(String id, Map<String, NormalisedEntityComponent> components, EntityManager manager){
        // Create registered entity using manager
        Entity entity = manager.getEntityObject(id);

        // Read each component
        for(String componentId: components.keySet()){
            EntityComponent component = ComponentMapper.getComponent(components.get(componentId));
            entity.addComponent(component,component.getId());
        }
        return entity;
    }

}
