package com.brm.GoatEngine.ECS;

import com.badlogic.gdx.Gdx;
import com.brm.GoatEngine.AI.Components.AIComponent;
import com.brm.GoatEngine.ECS.common.CameraTargetComponent;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.common.TagsComponent;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GraphicsRendering.CameraComponent;
import com.brm.GoatEngine.GraphicsRendering.SpriteComponent;
import com.brm.GoatEngine.GraphicsRendering.SpriterAnimationComponent;
import com.brm.GoatEngine.LevelEditor.Components.EditorLabelComponent;
import com.brm.GoatEngine.Physics.Collider;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;
import com.brm.GoatEngine.Utils.Logger;
import org.ini4j.Ini;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Used to serialize entities in Ini format
 */
public class ECSIniSerializer {

    private String iniPath;
    private EntityManager entityManager;
    private Ini ini;
    private HashSet<String> entityIds = new HashSet<String>();
    private HashSet<String> keySet = new HashSet<String>(); // All properties in file

    public ECSIniSerializer(String iniPath, EntityManager entityManager){
        this.iniPath = iniPath;
        this.entityManager = entityManager;

        try {
            ini = new Ini(Gdx.files.internal(iniPath).file());
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.logStackTrace(e);
            e.printStackTrace();
        }
    }


    /**
     * Loads a manager from file
     */
    public void load(){
        Logger.info("Loading Level: " + iniPath);
        try {
            ini.getConfig().setTree(true);
            ini.getConfig().setMultiSection(true);
            ini.load();
            loadEntityIndex();
            for(String id: entityIds){
                loadEntity(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.info("Level: " + iniPath + " loaded");
    }


    /**
     * Saves a manager to file
     */
    public void save(){
        entityIds.clear();
        entityIds.addAll(entityManager.getEntityIds());
        writeEntityIndex();
        for(String id: entityIds){
            ini.putComment("#", "Entity BEGIN");
            HashMap<String, EntityComponent> components = entityManager.getComponentsForEntity(id);
            for(EntityComponent component: components.values()){
                writeComponent(id, component);
            }
            ini.putComment("#", "Entity END");
        }
        try {
            ini.store();
        } catch (IOException e) {
            Logger.error(e.getMessage());
            Logger.logStackTrace(e);
            e.printStackTrace();
        }
    }



    /**
     * Writes the entity index to file.
     * The index is used to make a list of all
     * the entities described in the file.
     * This is used to retrieve the info about
     * each entity in an easy way
     */
    private void writeEntityIndex(){

        Object[] array = entityIds.toArray();

        for(int i=0; i<array.length; i++){
            String id = (String) array[i];
            ini.put("entity_index",String.valueOf(i),id);
        }
    }


    /**
     * Writes a component to INI
     * @param entityId
     * @param component
     */
    private void writeComponent(String entityId, EntityComponent component) {
        String secName = entityId + "/" + component.getId().toLowerCase();
        Map<String, String> map = component.toMap();
        writeMap(secName, map);

        // Special Case
        // Physics Component's colliders
        if(component.getId().equals(PhysicsComponent.ID)){
            PhysicsComponent phys = (PhysicsComponent) component;
            ArrayList<Collider> colliders = phys.getColliders();
            for(Collider collider: colliders){
                writePhysicsCollider(entityId, collider, colliders.indexOf(collider));
            }
        }
    }

    /**
     * Write a PODType object
     * @param sectionName the name of the section to write
     * @param map the map object instance
     */
    private void writeMap(String sectionName, Map<String, String> map){
        for(String key: map.keySet()){
            ini.put(sectionName, key, map.get(key));
        }
    }

    /**
     * Physics Collider are not components per say
     * however they still need to be serialized
     */
    private void writePhysicsCollider(String entityId, Collider collider, int index){
        Map<String, String> map = collider.toMap();
        String colliderType = map.get("component_id").toLowerCase();
        String sectionName = entityId + "/" + "physics_collider_" + colliderType.toLowerCase() + index;
        writeMap(sectionName, map);
    }


    /**
     * Loads the entity index
     */
    private void loadEntityIndex(){
        Ini.Section index = ini.get("entity_index");
        entityIds.addAll(index.values());
        keySet.addAll(ini.keySet());
    }

    private void loadEntity(String id){
        HashSet<String> components = getComponentsForEntity(id);
        for(String key: components){
            if(key.toUpperCase().endsWith("_COMPONENT")){
                    EntityComponent comp = loadComponent(key);
                    entityManager.addComponent(comp.getId(),comp,id);
            }else{
                // Collider
                if(key.contains("physics_collider_")){
                    loadCollider(key);
                } // else we dont know what this is skip it
            }
        }
    }


    private EntityComponent loadComponent(String componentTitle){
        Map<String, String> map = ini.get(componentTitle);
        String componentId = map.get("component_id");
        if(componentId == null){
            throw new InvalidLevelDefinitionException("component_id is missing for " + componentTitle);
        }

        EntityComponent comp = null;

        // ScriptComponent
        if(isComponent(componentId, ScriptComponent.ID)){
            comp = new ScriptComponent(map);

        } // PhysicsComponent
        else if(isComponent(componentId, PhysicsComponent.ID)){
            comp = new PhysicsComponent(map);

        } // TagsComponent
        else if(isComponent(componentId, TagsComponent.ID)){
            comp = new TagsComponent(map);

        } // SpriteComponent
        else if(isComponent(componentId, SpriteComponent.ID)){
            comp = new SpriteComponent(map);

        } // SpriterAnimationComponent
        else if(isComponent(componentId, SpriterAnimationComponent.ID)){
            comp = new SpriterAnimationComponent(map);


        } // EditorLabelComponent
        else if(isComponent(componentId, EditorLabelComponent.ID)){
            comp = new EditorLabelComponent(map);

        }// CameraComponent
        else if(isComponent(componentId, CameraComponent.ID)){
            comp = new CameraComponent(map);

        } // CameraTargetComponent
        else if(isComponent(componentId, CameraTargetComponent.ID)){
            comp = new CameraTargetComponent(map);

        }  // AIComponent
        else if(isComponent(componentId, AIComponent.ID)){
            comp = new AIComponent(map);
        }

        return comp;
    }


    /**
     * Test if a string is a certain component
     * @param componentId
     * @param componentTest
     * @return
     */
    private boolean isComponent(String componentId, String componentTest){
        return componentId.toLowerCase().equals(componentTest.toLowerCase());
    }




    private Collider loadCollider(String colliderTitle){
        return null;
    }


    /**
     * Returns list of component names for a certain entity
     * @param entityId
     * @return
     */
    private HashSet<String> getComponentsForEntity(String entityId ) {
        HashSet<String> comps = new HashSet<String>();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String c = iterator.next();
            if (c.startsWith(entityId)){
                if(c.contains("/"))
                    comps.add(c);
                iterator.remove();
            }
        }
        return comps;
    }



    public class InvalidLevelDefinitionException extends RuntimeException{
        public InvalidLevelDefinitionException(String reason){
            super("Invalid Level Definition could not read level, reason: " + reason);
        }
    }


}
