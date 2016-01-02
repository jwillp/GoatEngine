package com.goatgames.goatengine.ecs;

import com.goatgames.goatengine.ai.components.AIComponent;
import com.goatgames.goatengine.ecs.common.CameraTargetComponent;
import com.goatgames.goatengine.graphicsrendering.*;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.ecs.common.TagsComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.leveleditor.components.EditorLabelComponent;
import com.goatgames.goatengine.physics.*;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;

import java.util.Map;

import static com.goatgames.goatengine.ecs.core.EntityComponent.EntityComponentMap;

/**
 * Class used to create instances of Entities in such ways:
 *  - Creates entity using a Collection of Component as Maps
 */
public class EntityFactory{

    /**
     * Creates a registered entity from a Map of componentMaps
     */
    public static Entity createFromMap(Map<String, EntityComponentMap> components){
        EntityManager entityManager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();

        // Create registered entity using manager
        Entity entity = entityManager.createEntity();

        // Read each component
        for(String componentId: components.keySet()){

            EntityComponentMap map = components.get(componentId);

            // Depending on the type create the component
            processScriptComponent(componentId, entity, map);
            processPhysicsComponent(componentId, entity, map, components);
            processTagsComponent(componentId, entity, map);
            processSpriteComponent(componentId, entity, map);
            processSpriterAnimationComponent(componentId, entity, map);
            processEditorLabelComponent(componentId, entity, map);
            processCameraComponent(componentId, entity, map);
            processCameraTargetComponent(componentId, entity, map);
            processAIComponent(componentId, entity, map);
            processZIndexComponent(componentId, entity, map);
            processFakeLightComponent(componentId, entity, map);
        }
        return entity;
    }

    private static void processFakeLightComponent(String componentId, Entity entity, EntityComponentMap componentData) {
        if(!isComponent(componentId, LightComponent.ID)){return;}
        entity.addComponent(new LightComponent(componentData), LightComponent.ID);
    }

    /**
     * Processes a script ScriptComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processScriptComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, ScriptComponent.ID)){ return; }
            ScriptComponent scriptComp = new ScriptComponent(componentData);
            entity.addComponent(scriptComp, ScriptComponent.ID);
    }

    /**
     * Processes a script PhysicsComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processPhysicsComponent(String componentId, Entity entity,
                                                EntityComponentMap componentData,
                                                Map<String, EntityComponentMap> components)
    {
        if(!isComponent(componentId, PhysicsComponent.ID)){ return; }
        entity.addComponent(new PhysicsComponent(componentData), PhysicsComponent.ID);

        // Read Colliders
        for(String colliderKey: components.keySet()){
            if(!colliderKey.contains("physics_collider_")) continue;
            EntityComponentMap colliderData = components.get(colliderKey);
            Collider.addCollider(entity,colliderDefFromMap(colliderData));
        }
    }


    /**
     * Makes a collider def from a Map
     * TODO move else where (In Collider Maybe?)
     * @param colliderData
     * @return
     */
    public static ColliderDef colliderDefFromMap(Map<String,String> colliderData){
        String colType = colliderData.get("type");
        ColliderDef colDef;
        // Circle Collider //
        if(colType.equals("circle")){
            colDef = new CircleColliderDef();
            CircleColliderDef circDef = (CircleColliderDef)colDef;
            circDef.radius = Float.parseFloat(colliderData.get("radius"));

            // Box Collider //
        }else if(colType.equals("box")){
            colDef = new BoxColliderDef();
            BoxColliderDef boxDef = (BoxColliderDef)colDef;
            boxDef.width = Float.parseFloat(colliderData.get("width"));
            boxDef.height = Float.parseFloat(colliderData.get("height"));

        }else if(colType.equals("capsule")){
            colDef = new CapsuleColliderDef();
            CapsuleColliderDef capDef = (CapsuleColliderDef)colDef;
            capDef.width = Float.parseFloat(colliderData.get("width"));
            capDef.height = Float.parseFloat(colliderData.get("height"));
        }else{
            // Throw Unknown Collider Type Exception
            throw new ColliderDef.UnknownColliderTypeException(colType);
        }

        // Shared attributes among collider types
        colDef.tag = colliderData.get("tag");
        colDef.isSensor = Boolean.parseBoolean(colliderData.get("is_sensor"));
        colDef.x = Float.parseFloat(colliderData.get("position_x"));
        colDef.y = Float.parseFloat(colliderData.get("position_y"));
        return colDef;
    }



    /**
     * Processes a script TagsComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processTagsComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, TagsComponent.ID)){ return; }
        entity.addComponent(new TagsComponent(componentData), TagsComponent.ID);
    }

    /**
     * Processes a script SpriteComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processSpriteComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, SpriteComponent.ID)){ return; }
        entity.addComponent(new SpriteComponent(componentData), SpriteComponent.ID);
    }

    /**
     * Processes a script SpriterAnimationComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processSpriterAnimationComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, SpriterAnimationComponent.ID)){ return; }
        entity.addComponent(new SpriterAnimationComponent(componentData), SpriterAnimationComponent.ID);
    }

    /**
     * Processes a script EditorLabelComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processEditorLabelComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, EditorLabelComponent.ID)){ return; }
        entity.addComponent(new EditorLabelComponent(componentData), EditorLabelComponent.ID);
    }

    /**
     * Processes a script CameraComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processCameraComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, CameraComponent.ID)){ return; }
        entity.addComponent(new CameraComponent(componentData), CameraComponent.ID);
    }

    /**
     * Processes a script CameraTargetComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processCameraTargetComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, CameraTargetComponent.ID)){ return; }
        entity.addComponent(new CameraTargetComponent(componentData), CameraTargetComponent.ID);
    }

    /**
     * Processes a script AIComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static void processAIComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, AIComponent.ID)){ return; }
        entity.addComponent(new AIComponent(componentData), AIComponent.ID);
    }


    private static void processZIndexComponent(String componentId, Entity entity, EntityComponentMap componentMap){
        if(!isComponent(componentId, ZIndexComponent.ID)) return;
        entity.addComponent(new ZIndexComponent(componentMap), ZIndexComponent.ID);
    }

    /**
     * Returns if a component name is a certain component
     * @param c he id of the component as found in the component list
     * @param targetCompId
     * @return
     */
    private static boolean isComponent(String c, String targetCompId){
        return c.toUpperCase().equals(targetCompId.toUpperCase());
    }

}
