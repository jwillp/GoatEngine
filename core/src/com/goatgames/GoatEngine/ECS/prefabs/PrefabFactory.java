package com.goatgames.goatengine.ecs.prefabs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.ComponentMapper;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.physics.ColliderDef;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.gdk.GAssert;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;


/**
 * Creates entities from prefab objects
 */
public class PrefabFactory {

    private static HashMap<String, Ini> prefabs = new HashMap<String, Ini>();

    private IPrefabLoader loader;

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
        return entity;
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
