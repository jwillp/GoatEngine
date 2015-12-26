package com.brm.GoatEngine.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.*;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.GraphicsRendering.SpriterAnimationComponent;
import com.brm.GoatEngine.Physics.*;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;
import com.brm.GoatEngine.Utils.Logger;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import static com.brm.GoatEngine.Physics.ColliderDef.UnknownColliderTypeException;


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
            EntityFactory.createFromMap(getComponents(ini));

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
    private  HashMap<String, EntityComponent.EntityComponentMap> getComponents(Ini ini) {
        HashMap<String, EntityComponent.EntityComponentMap> comps;
        comps = new HashMap<String, EntityComponent.EntityComponentMap>();

        for(String componentKey: ini.keySet()){
            EntityComponent.EntityComponentMap map = new EntityComponent.EntityComponentMap();
            // fetch values for string substitution
            for(String key: ini.get(componentKey).keySet()){
                map.put(key, ini.fetch(componentKey,key));
            }
            comps.put(componentKey,map);
        }
        return comps;
    }
}
