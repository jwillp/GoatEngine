package com.goatgames.goatengine.leveleditor.commands;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.EntityFactory;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.goatgames.goatengine.physics.BoxCollider;
import com.goatgames.goatengine.physics.CircleCollider;
import com.goatgames.goatengine.physics.Collider;
import com.goatgames.goatengine.physics.PhysicsComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Command Duplicating an entity
 */
public class DuplicateEntityCommand extends EditorCommand {
    Entity selectedEntity;
    LevelEditor editor;
    public DuplicateEntityCommand(Entity selectedEntity, LevelEditor levelEditor) {
        super();
        this.selectedEntity = selectedEntity;
        this.editor = levelEditor;
    }

    @Override
    public void exec() {

        ObjectMap<String, EntityComponent> comps = selectedEntity.getComponents();
        Map<String, EntityComponent.EntityComponentMap> map = new HashMap<>(comps.size);

        for(String key : comps.keys()){
            EntityComponent.EntityComponentMap eMap = new EntityComponent.EntityComponentMap(comps.get(key).toMap());
            map.put(key, eMap);

            // Physics Component Collider
            if(key.equals(PhysicsComponent.ID)){
                PhysicsComponent phys = (PhysicsComponent)comps.get(key);
                ArrayList<Collider> colliders = phys.getColliders();
                for (int i = 0; i < colliders.size(); i++) {
                    Collider collider = colliders.get(i);
                    Map<String, String> colMap = collider.toMap();
                    // Determine collider name
                    String colliderType = "";
                    if (collider instanceof CircleCollider) {
                        colliderType = "circle_collider";
                    } else if (collider instanceof BoxCollider) {
                        colliderType = "box_collider";
                    }
                    colMap.put("type", colliderType.toLowerCase().replace("_collider", ""));
                    key = "physics_collider_" + colliderType.toLowerCase() + i;
                    map.put(key, new EntityComponent.EntityComponentMap(colMap));
                }
            }
            
        }

        Entity newEntity = EntityFactory.createFromMap(map);

    }
}
