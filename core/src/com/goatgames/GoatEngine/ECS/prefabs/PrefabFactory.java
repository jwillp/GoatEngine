package com.goatgames.goatengine.ecs.prefabs;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.ComponentMapper;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.physics.ColliderDef;
import com.goatgames.goatengine.physics.PhysicsComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Creates entities from prefab objects
 */
public class PrefabFactory {

    private static Map<String, Prefab> cache = new HashMap<>();
    private final IPrefabLoader loader;

    /**
     * Constructor, taking a prefab loader
     * @param prefabLoader loader to use for the factory
     */
    public PrefabFactory(IPrefabLoader prefabLoader){
        loader = prefabLoader;
    }

    /**
     * Creates an entity in an entity manager from a prefab
     * @param prefab prefab to use to create the entity
     * @param manager entity manager holding the newly created entity
     * @return Entity created
     */
    public Entity createEntity(Prefab prefab, EntityManager manager){
        // Create registered entity using manager
        Entity entity = manager.createEntity();
        List<NormalisedEntityComponent> colliders = new ArrayList<>();

        for(NormalisedEntityComponent data: prefab.getComponents()){
            if (data.get("component_id").contains("COLLIDER")) {
                colliders.add(data);
                continue;
            }

            EntityComponent comp = ComponentMapper.getComponent(data);
            if(GAssert.notNull(comp, "comp == null, will not be added")){
                assert comp != null;
                entity.addComponent(comp, comp.getId());
            }
        }
        // Read Colliders
        for(NormalisedEntityComponent map : colliders){
            PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
            phys.getBodyDef().addColliderDef(ColliderDef.colliderDefFromNormalisedData(map));
        }
        return entity;
    }

    /**
     * Creates an entity from a prefab
     * @param pathToPrefab path to the prefab
     * @param entityManager manager to use for entity creation
     * @return newly created entity from prefab, or null if none could be created
     */
    public Entity createEntity(final String pathToPrefab, EntityManager entityManager){
        Prefab prefab;
        // if caching is enabled, load from cache otherwise load from pathToPrefab
        boolean cachingEnabled = GoatEngine.config.prefab.caching;
        if(cachingEnabled) {
            if (cache.containsKey(pathToPrefab)) {
                prefab = cache.get(pathToPrefab);
            } else {
                prefab = this.loader.load(pathToPrefab);
                cache.put(pathToPrefab, prefab);
            }
        }
        else{
            prefab = this.loader.load(pathToPrefab);
        }
        // Load prefab from path to prefab
        return createEntity(prefab, entityManager);
    }
}
