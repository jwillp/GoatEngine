package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.ecs.core.*;
import com.goatgames.goatengine.graphicsrendering.*;
import com.goatgames.goatengine.input.TouchableComponent;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.ecs.common.TagsComponent;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.leveleditor.components.EditorLabelComponent;
import com.goatgames.goatengine.physics.*;

import java.util.Map;

import static com.goatgames.goatengine.ecs.core.EntityComponent.EntityComponentMap;

/**
 * Class used to create instances of Entities in such ways:
 *  - Creates entity using a Collection of Component as Maps
 */
public class EntityFactory{
    /**
     * The linked factories Map<ComponentId, Factory>
     */
    private static ObjectMap<String,EntityComponentFactory> factories = new ObjectMap<>();

    public static void linkFactories(){

        linkFactory(TagsComponent.ID, new TagsComponent.Factory());
        linkFactory(CameraComponent.ID, new CameraComponent.Factory());
        linkFactory(LightComponent.ID, new LightComponent.Factory());
        linkFactory(ParticleEmitterComponent.ID, new ParticleEmitterComponent.Factory());
        linkFactory(SpriteComponent.ID, new SpriteComponent.Factory());
        linkFactory(SpriterAnimationComponent.ID, new SpriterAnimationComponent.Factory());
        linkFactory(ZIndexComponent.ID, new ZIndexComponent.Factory());
        linkFactory(TouchableComponent.ID, new TouchableComponent.Factory());
        linkFactory(EditorLabelComponent.ID, new EditorLabelComponent.Factory());
        linkFactory(PhysicsComponent.ID, new PhysicsComponent.Factory());

        //
        //linkFactory(GameComponent.ID, new GameComponent.Factory());
    }

    /**
     * Links a factory to a Component Id
     * @param componentName
     * @param factory
     */
    private static void linkFactory(final String componentName, final EntityComponentFactory factory){
        factories.put(componentName, factory);
    }




    /**
     * Creates a registered entity from a Map of componentMaps
     */
    public static Entity createFromMap(Map<String, EntityComponentMap> components){
        EntityManager entityManager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();


        

        // Create registered entity using manager
        Entity entity = entityManager.createEntity();

        // Read each component
        for(String componentId: components.keySet()){
            componentId = componentId.toUpperCase();
            EntityComponentMap map = components.get(componentId);



            // Depending on the type create the component
            if(processPhysicsComponent(componentId, entity, map, components)) continue;
            if(processGameComponent(componentId, entity, map)) continue;
        }
        return entity;
    }


    /**
     * Processes a PhysicsComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processPhysicsComponent(String componentId, Entity entity,
                                                   EntityComponentMap componentData,
                                                   Map<String, EntityComponentMap> components)
    {
        // Read Colliders
        for(String colliderKey: components.keySet()){
            if(!colliderKey.contains("physics_collider_")) continue;
            EntityComponentMap colliderData = components.get(colliderKey);
            Collider.addCollider(entity,Collider.defFromMap(colliderData));
        }
        return true;
    }


    /**
     * Processes a generic game component
     * @param componentId
     * @param entity the entity to update
     * @param componentMap the map containing data bout the component
     */
    private static boolean processGameComponent(String componentId, Entity entity, EntityComponentMap componentMap){
        if(!componentMap.containsKey("component_id_internal")) return false;
        entity.addComponent(new GameComponent(componentMap), componentId);
        return true;
    }


}
