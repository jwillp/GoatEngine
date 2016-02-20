package com.goatgames.goatengine.ecs;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ai.components.AIComponent;
import com.goatgames.goatengine.ecs.common.TagsComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponentMap;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.GameComponent;
import com.goatgames.goatengine.graphicsrendering.*;
import com.goatgames.goatengine.input.TouchableComponent;
import com.goatgames.goatengine.leveleditor.components.EditorLabelComponent;
import com.goatgames.goatengine.physics.*;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;

import java.util.Map;

/**
 * Class used to create instances of Entities in such ways:
 *  - Creates entity using a Collection of Component as Maps
 */
public class LegacyEntityFactory {

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
            if(processScriptComponent(componentId, entity, map)) continue;
            if(processPhysicsComponent(componentId, entity, map, components)) continue;
            if(processTagsComponent(componentId, entity, map)) continue;
            if(processSpriteComponent(componentId, entity, map)) continue;
            if(processSpriterAnimationComponent(componentId, entity, map)) continue;
            if(processEditorLabelComponent(componentId, entity, map)) continue;
            if(processCameraComponent(componentId, entity, map)) continue;
            if(processAIComponent(componentId, entity, map)) continue;
            if(processZIndexComponent(componentId, entity, map)) continue;
            if(processFakeLightComponent(componentId, entity, map)) continue;
            if(processTouchableComponent(componentId, entity, map)) continue;
            if(processGameComponent(componentId, entity, map)) continue;
        }
        return entity;
    }

    /**
     * Processes a TouchableComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processTouchableComponent(String componentId, Entity entity, EntityComponentMap componentData) {
        if(!isComponent(componentId, TouchableComponent.ID)){return false;}
        entity.addComponent(new TouchableComponent(componentData), TouchableComponent.ID);
        return true;
    }

    /**
     * Processes a FakeLightComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processFakeLightComponent(String componentId, Entity entity, EntityComponentMap componentData) {
        if(!isComponent(componentId, LightComponent.ID)){return false;}
        entity.addComponent(new LightComponent(componentData), LightComponent.ID);
        return true;
    }

    /**
     * Processes a ScriptComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processScriptComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, ScriptComponent.ID)){
            return false;
        }
        ScriptComponent scriptComp = new ScriptComponent(componentData);
        entity.addComponent(scriptComp, ScriptComponent.ID);
        return true;
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
        if(!isComponent(componentId, PhysicsComponent.ID)){ return false; }
        entity.addComponent(new PhysicsComponent(componentData), PhysicsComponent.ID);

        // Read Colliders
        for(String colliderKey: components.keySet()){
            if(!colliderKey.contains("physics_collider_")) continue;
            EntityComponentMap colliderData = components.get(colliderKey);
            Collider.addCollider(entity,colliderDefFromMap(colliderData));
        }
        return true;
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
     * Processes a TagsComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processTagsComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, TagsComponent.ID)){ return false; }
        entity.addComponent(new TagsComponent(componentData), TagsComponent.ID);
        return true;
    }

    /**
     * Processes a SpriteComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processSpriteComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, SpriteComponent.ID)){ return false; }
        entity.addComponent(new SpriteComponent(componentData), SpriteComponent.ID);
        return true;
    }

    /**
     * Processes a SpriterAnimationComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processSpriterAnimationComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, SpriterAnimationComponent.ID)){ return false; }
        entity.addComponent(new SpriterAnimationComponent(componentData), SpriterAnimationComponent.ID);
        return true;
    }

    /**
     * Processes a EditorLabelComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processEditorLabelComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, EditorLabelComponent.ID)){ return false; }
        entity.addComponent(new EditorLabelComponent(componentData), EditorLabelComponent.ID);
        return true;
    }

    /**
     * Processes a CameraComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processCameraComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, CameraComponent.ID)){ return false; }
        entity.addComponent(new CameraComponent(componentData), CameraComponent.ID);
        return true;
    }

    /**
     * Processes a AIComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processAIComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, AIComponent.ID)){ return false; }
        entity.addComponent(new AIComponent(componentData), AIComponent.ID);
        return true;
    }


    /**
     * Processes a ZComponent
     * @param componentId the id of the component as found in the component list
     * @param entity the entity to update
     * @param componentData the map containing data bout the component
     */
    private static boolean processZIndexComponent(String componentId, Entity entity, EntityComponentMap componentData){
        if(!isComponent(componentId, ZIndexComponent.ID)) return false;
        entity.addComponent(new ZIndexComponent(componentData), ZIndexComponent.ID);
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