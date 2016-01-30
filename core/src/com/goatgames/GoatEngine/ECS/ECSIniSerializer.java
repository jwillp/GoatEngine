package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.physics.BoxCollider;
import com.goatgames.goatengine.physics.Collider;
import com.goatgames.goatengine.utils.Logger;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.physics.CircleCollider;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.*;

import static com.goatgames.goatengine.ecs.core.EntityComponent.EntityComponentMap;

/**
 * Used to serialize entities in Ini format
 */
public class ECSIniSerializer {

    private String iniPath;
    private EntityManager entityManager;
    private Ini ini;
    private ObjectSet<String> entityIds = new ObjectSet<String>();

    // All properties (components) in file, used to iterate faster.
    private HashSet<String> componentsSection = new HashSet<String>();

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
     * Saves a manager to file
     */
    public void save(){
        entityIds.clear();
        entityIds = entityManager.getEntityIds();
        writeEntityIndex();
        for(String id: entityIds){
            ini.putComment("#", "Entity BEGIN");
            ObjectMap<String, EntityComponent> components = entityManager.getComponentsForEntity(id);
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
                Entity e = EntityFactory.createFromMap(getComponentsForEntity(id));
                entityManager.freeEntity(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.info("Level: " + iniPath + " loaded");
    }


    public void dispose(){
        this.ini.clear();
        this.ini = null;
        this.entityIds.clear();
        this.entityIds = null;
    }
    /**
     * Writes the entity index to file.
     * The index is used to make a list of all
     * the entities described in the file.
     * This is used to retrieve the info about
     * each entity in an easy way
     */
    private void writeEntityIndex(){
        int i = 0;
        for(String id: entityIds){
            ini.put("entity_index",String.valueOf(i),id);
            i++;
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
     * Write a Map object
     * @param sectionName the name of the section to write
     * @param map the map object instance
     */
    private void writeMap(String sectionName, Map<String, String> map){
        for(String key: map.keySet()){ ini.put(sectionName, key, map.get(key)); }
    }

    /**
     * Physics Collider are not components per say
     * however they still need to be serialized
     */
    private void writePhysicsCollider(String entityId, Collider collider, int index){
        Map<String, String> map = collider.toMap();

        // Determine collider name
        String colliderType = "";
        if(collider instanceof CircleCollider){
            colliderType = "circle_collider";
        }else if(collider instanceof BoxCollider){
            colliderType = "box_collider";
        }
        map.put("type", colliderType.toLowerCase().replace("_collider", ""));
        String sectionName = entityId + "/" + "physics_collider_" + colliderType.toLowerCase() + index;
        writeMap(sectionName, map);
    }


    /**
     * Loads the entity index
     */
    private void loadEntityIndex(){
        Ini.Section index = ini.get("entity_index");
        index.values();
        // Get all entity Ids contained in the file
        for(Object o: index.values()){
            entityIds.add((String) o);
        }
        componentsSection.addAll(ini.keySet());
    }


    /**
     * Returns list of component as Maps for a certain entity with the specified ID
     * @param entityId
     * @return entity component maps
     */
    private  HashMap<String, EntityComponentMap> getComponentsForEntity(String entityId) {
        // HashMap<ComponentId, ComponentData>
        HashMap<String, EntityComponentMap> comps = new HashMap<String, EntityComponentMap>();
        for (Iterator<String> iterator = componentsSection.iterator(); iterator.hasNext(); ) {
            String c = iterator.next();
            if (c.startsWith(entityId)){
                if(c.contains("/")){
                    String componentName = c.replace(entityId + "/", "");
                    EntityComponentMap map = new EntityComponentMap();
                    // fetch values for string substitution
                    for(String key: ini.get(c).keySet()){
                        map.put(key, ini.fetch(c,key));
                    }
                    comps.put(componentName,map);
                }
                // ELSE should never happen, warning? ignore?
                iterator.remove(); // remove the list of available componentSections
                // the more we search, the faster it'll get.
            }
        }
        return comps;
    }

    /**
     * Returns if a component name is a certain component
     * @param c
     * @param targetComp
     * @return
     */
    public boolean isComponent(String c, String targetComp){
        return c.toUpperCase().equals(targetComp.toUpperCase());
    }


                                // EXCEPTION //
    public class InvalidLevelDefinitionException extends RuntimeException{
        public InvalidLevelDefinitionException(String reason){
            super("Invalid Level Definition could not read level, reason: " + reason);
        }
    }






}
