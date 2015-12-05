package com.brm.GoatEngine.ECS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Physics.BoxColliderDef;
import com.brm.GoatEngine.Physics.CircleColliderDef;
import com.brm.GoatEngine.Physics.Collider;
import com.brm.GoatEngine.Physics.ColliderDef;
import com.brm.GoatEngine.Utils.Logger;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.HashMap;

import static com.brm.GoatEngine.Physics.ColliderDef.*;


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
        Ini ini = null;
        try {
            if(prefabs.containsKey(prefab)){
                prefabs.get(prefab);
            }else{
                ini = new Ini(Gdx.files.internal(prefab).file());
                prefabs.put(prefab,ini);
            }
            EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
            entity = manager.createEntity();
            processPhysics(entity, ini);


        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.logStackTrace(e);
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * Processes physics settings (physics component, colliders)
     * to create a physics Component
     * @param entity the entity to update
     * @param ini the Ini instance of the prefab
     */
    private void processPhysics(Entity entity, Ini ini){
        // Read Physics Component // // TODO Find a way to throw exception when property does not exist
        Ini.Section physCompSec = ini.get("physics_component");
        World physWorld = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
        BodyDef.BodyType bodyType = BodyDef.BodyType.valueOf(physCompSec.get("body_type"));
        Vector2 position = new Vector2(physCompSec.fetch("position_x", float.class),
                                       physCompSec.fetch("position_y", float.class)
        );
        PhysicsComponent phys = new PhysicsComponent(physWorld,bodyType, position, 1,1);
        entity.addComponent(phys, PhysicsComponent.ID);


        // Read Colliders //
        for(String sectionName: ini.keySet()){
            if(sectionName.contains("physics_collider_")){
                Ini.Section colliderSec = ini.get(sectionName);
                String colType = colliderSec.get("type");
                ColliderDef colDef = null;
                                                            // Circle Collider //
                if(colType.equals("circle")){
                    colDef = new CircleColliderDef();
                    CircleColliderDef circDef = (CircleColliderDef)colDef;
                    circDef.radius = colliderSec.fetch("radius", float.class);
                    Collider.addCircleCollider(entity, circDef);

                                                            // Box Collider //
                }  else if(colType.equals("box")){
                    colDef = new BoxColliderDef();
                    BoxColliderDef boxDef = (BoxColliderDef)colDef;
                    boxDef.width = colliderSec.fetch("width", float.class);
                    boxDef.height = colliderSec.fetch("height", float.class);
                    Collider.addBoxCollider(entity, boxDef);

                }else{
                    // Throw Unknown Collider Type Exception
                    throw new UnkownColliderTypeException(colType);
                }

                // Shared attributes among types
                colDef.tag = colliderSec.fetch("tag");
                colDef.isSensor = colliderSec.fetch("is_sensor", boolean.class);
                colDef.x = colliderSec.fetch("x", float.class);
                colDef.y = colliderSec.fetch("y", float.class);
            }
        }



    }



    










}
