package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentMap;
import com.goatgames.goatengine.physics.Collider;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.utils.Logger;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;


/**
 * Creates entities from reading prefab files
 */
public class PrefabFactory {

    private static HashMap<String, Ini> prefabs = new HashMap<String, Ini>();

    /**
     * Creates an entity by reading a prefab file
     * @param prefab
     * @return
     */
    public Entity createEntity(final String prefab){

        Entity entity = null;
        Ini ini;
        try {
            if(GoatEngine.config.getBoolean("prefab.caching") && prefabs.containsKey(prefab)){
                ini = prefabs.get(prefab);
            }else{
                ini = new Ini(Gdx.files.internal(prefab).file());
                prefabs.put(prefab,ini);
            }
            HashMap<String, EntityComponentMap> comps = getComponents(ini);
            // Create registered entity using manager
            entity = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().createEntity();
            Array<EntityComponentMap> colliders = new Array<EntityComponentMap>();
            for(EntityComponentMap map: comps.values()){
                if (map.get("component_id").contains("COLLIDER")) {
                    colliders.add(map);
                } else {
                    EntityComponent comp = ComponentMapper.getComponent(map);
                    entity.addComponent(comp, comp.getId());
                }
            }

            for(EntityComponentMap map : colliders){
                // Read Colliders
                String colliderKey = map.get("component_id");
                Collider.addCollider(entity,LegacyEntityFactory.colliderDefFromMap(map));
            }



        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.logStackTrace(e);
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * Returns list of component as Maps as read in the prefab file
     * @param ini
     * @return entity component maps
     */
    private  HashMap<String, EntityComponentMap> getComponents(Ini ini) {
        HashMap<String, EntityComponentMap> comps;
        comps = new HashMap<>();

        for(String componentKey: ini.keySet()){
            EntityComponentMap map = new EntityComponentMap();
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
