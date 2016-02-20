package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponentMap;
import com.goatgames.goatengine.utils.Logger;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;


/**
 * Creates entities from reading prefab files
 */
// TODO Convert process* methods to Classes (PrefabFactoryProcessor)
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
            if(prefabs.containsKey(prefab)){
                ini = prefabs.get(prefab);
            }else{
                ini = new Ini(Gdx.files.internal(prefab).file());
                prefabs.put(prefab,ini);
            }
            entity = LegacyEntityFactory.createFromMap(getComponents(ini));

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
        comps = new HashMap<String, EntityComponentMap>();

        for(String componentKey: ini.keySet()){
            EntityComponentMap map = new EntityComponentMap();
            // fetch values for string substitution
            for(String key: ini.get(componentKey).keySet()){
                map.put(key, ini.fetch(componentKey,key));
            }
            comps.put(componentKey,map);
        }
        return comps;
    }
}
