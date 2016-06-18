package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.physics.BoxCollider;
import com.goatgames.goatengine.physics.CircleCollider;
import com.goatgames.goatengine.physics.Collider;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.utils.Logger;
import org.ini4j.Ini;

import java.io.IOException;
import java.util.*;

/**
 * Used to serialize entities in Ini format
 */
public class IniSerializer {

    private String iniPath;
    private EntityManager entityManager;
    private Ini ini;
    private ObjectSet<String> entityIds = new ObjectSet<String>();

    // All properties (components) in file, used to iterate faster.
    private HashSet<String> componentsSection = new HashSet<String>();

    public IniSerializer(String iniPath, EntityManager entityManager){
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
                Entity e = LegacyEntityFactory.createFromMap(getComponentsForEntity(id));
                entityManager.freeEntityObject(e);
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
        NormalisedEntityComponent data = component.normalise();
        writeNormalisedObject(secName, data);

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
     * Write a normalised entity component object
     * @param sectionName the name of the section to write
     * @param normalisedEntityComponent the normalisedEntityComponent object instance
     */
    private void writeNormalisedObject(String sectionName, NormalisedEntityComponent normalisedEntityComponent){
        for(String key: normalisedEntityComponent.keySet()){ ini.put(sectionName, key, normalisedEntityComponent.get(key)); }
    }

    /**
     * Physics Collider are not components per say
     * however they still need to be serialized
     */
    private void writePhysicsCollider(String entityId, Collider collider, int index){
        NormalisedEntityComponent data = collider.normalise();

        // Determine collider name
        String colliderType = "";
        if(collider instanceof CircleCollider){
            colliderType = "circle_collider";
        }else if(collider instanceof BoxCollider){
            colliderType = "box_collider";
        }
        data.put("type", colliderType.toLowerCase().replace("_collider", ""));
        String sectionName = entityId + "/" + "physics_collider_" + colliderType.toLowerCase() + index;
        writeNormalisedObject(sectionName, data);
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
    private  HashMap<String, NormalisedEntityComponent> getComponentsForEntity(String entityId) {
        // HashMap<ComponentId, ComponentData>
        HashMap<String, NormalisedEntityComponent> comps = new HashMap<String, NormalisedEntityComponent>();
        for (Iterator<String> iterator = componentsSection.iterator(); iterator.hasNext(); ) {
            String c = iterator.next();
            if (c.startsWith(entityId)){
                if(c.contains("/")){
                    String componentName = c.replace(entityId + "/", "");
                    NormalisedEntityComponent map = new NormalisedEntityComponent();
                    // fetch values for string substitution
                    for(String key: ini.get(c).keySet()){
                        map.put(key, ini.fetch(c,key));
                    }
                    comps.put(componentName,map);
                }
                // ELSE the only case in which this happens is for entity index values?
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
