package com.goatgames.goatengine.ecs.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.ComponentMapper;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.physics.ColliderDef;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.gdk.GAssert;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Creates entities from prefab objects
 */
public class PrefabFactory {

    private static Map<String, Prefab> cache = new HashMap<>();

    private IPrefabLoader loader;


    /**
     * Creates an entity in an entity manager from a prefab
     * @param prefab prefab to use to create the entity
     * @param manager entity manager holding the newly created entity
     * @return Entity created
     */
    public Entity createEntity(Prefab prefab, EntityManager manager){
        Entity entity = null;


        return entity;
    }

    /**
     * Creates an entity from a pathToPrefab
     * @param pathToPrefab
     * @param entityManager
     * @return
     */
    public Entity createEntity(final String pathToPrefab, EntityManager entityManager){
        Prefab prefab = null;
        // if caching is enabled, load from cache otherwise load from pathToPrefab
        boolean cachingEnabled = GoatEngine.config.getBoolean("pathToPrefab.caching");
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

       /* Entity entity = null;
        Ini ini;
        try {
            if(GoatEngine.config.getBoolean("pathToPrefab.caching") && cache.containsKey(pathToPrefab)){
                ini = cache.get(pathToPrefab);
            }else{
                ini = new Ini(Gdx.files.internal(pathToPrefab).file());
                cache.put(pathToPrefab,ini);
            }
            HashMap<String, NormalisedEntityComponent> comps = getComponents(ini);
            // Create registered entity using manager
            entity = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().createEntity();
            Array<NormalisedEntityComponent> colliders = new Array<NormalisedEntityComponent>();
            for(NormalisedEntityComponent data: comps.values()){
                if (data.get("component_id").contains("COLLIDER")) {
                    colliders.add(data);
                } else {
                    EntityComponent comp = ComponentMapper.getComponent(data);
                    if(GAssert.notNull(comp, "comp == null, will not be added")){
                        assert comp != null;
                        entity.addComponent(comp, comp.getId());
                    }
                }
            }

            for(NormalisedEntityComponent map : colliders){
                // Read Colliders
                PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
                phys.getBodyDef().addColliderDef(ColliderDef.colliderDefFromNormalisedData(map));
            }
        } catch (IOException e) {
            GoatEngine.logger.error(e.getMessage());
            GoatEngine.logger.error(e);
            e.printStackTrace();
        }
        return entity;*/
    }

    /**
     * Returns list of component as Maps as read in the prefab file
     * @param ini
     * @return entity component maps
     */
    private  HashMap<String, NormalisedEntityComponent> getComponents(Ini ini) {
        HashMap<String, NormalisedEntityComponent> comps;
        comps = new HashMap<>();

        for(String componentKey: ini.keySet()){
            NormalisedEntityComponent map = new NormalisedEntityComponent();
            // fetch values for string substitution
            for(String key: ini.get(componentKey).keySet()){
                map.put(key, ini.fetch(componentKey,key));
            }
            map.put("component_id", componentKey.toUpperCase());
            comps.put(componentKey,map);
        }
        return comps;
    }
}
