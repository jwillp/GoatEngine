package com.goatgames.goatengine.ecs.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.EntityFactory;
import com.goatgames.goatengine.ecs.ISerialiser;
import com.goatgames.goatengine.ecs.IniSerializer;
import com.goatgames.goatengine.ecs.JsonSerialiser;
import com.goatgames.goatengine.ecs.common.TagsComponent;

import java.util.UUID;

/**
 * Allows the retrieval of entityPool and their components
 */
public class EntityManager {

    //ObjectMap<COMPONENT_ID, ObjectMap<ENTITY_ID, COMPONENT_INSTANCE>>
    private ObjectMap<String, ObjectMap<String, EntityComponent>> components = new ObjectMap<String, ObjectMap<String, EntityComponent>>();


    private final EntityPool entityPool = new EntityPool();



    public EntityManager(){}

    /**
     * Generates a unique ID for the entityPool
     */
    public String generateId(){
        return UUID.randomUUID().toString();
    }

    /**
     * Creates a new registered Entity instance and returns it
     * The entity has a ScriptComponent
     * @return
     */
    public Entity createEntity(){
        Entity entity =  getEntityObject("null");
        registerEntity(entity);
        //entity.addComponent(new ScriptComponent(true), ScriptComponent.ID);
        entity.addComponent(new TagsComponent(true), TagsComponent.ID);
        return entity;
    }

    /**
     * Frees an entity
     * @param entity
     */
    public void freeEntityObject(Entity entity){
        if(GAssert.notNull(entity, "entity == null, object will not be freed"))
           entityPool.free(entity);
    }


    /**
     * Registers an Entity in the manager and gives it an ID
     * @param entity the entity to register
     * @return returns the id of the newly registered entity
     */
    public String registerEntity(Entity entity) {
        entity.setId(generateId());
        entity.setManager(this);
        return entity.getId();
    }



    /**
     *  Adds a Property to an Entity
     * @param componentId: The Id of the component to add
     * @param component: The instance of the component to add
     * @param entityId: The Id of the entity
     * @return EntityManager for chaining
     */
    @SuppressWarnings("unchecked")
    public EntityManager addComponent(String componentId, EntityComponent component, String entityId){
        GAssert.notNull(entityId, "Tried to add a component to an entity without an ENTITY_ID");
        GAssert.notNull(componentId, "Tried to add a component to an entity without a COMPONENT_ID");
        ObjectMap componentContainer = this.components.get(componentId);
        if(componentContainer == null){
            componentContainer = new ObjectMap<String, EntityComponent>();
        }
        componentContainer.put(entityId, component);
        this.components.put(componentId, componentContainer);

        //Call on attach
        Entity e = getEntityObject(entityId);
        component.onAttach(e);
        freeEntityObject(e);
        return this;
    }

    /**
     * Removes a component from an entity
     * @param componentId the id of the component
     * @param entityId the id of the entity
     * @return this for chaining
     */
    public EntityManager removeComponent(String componentId, String entityId){
        ObjectMap<String, EntityComponent> componentEntry = this.components.get(componentId);

        //Test if the entity has the component
        if(this.hasComponent(componentId, entityId)) {
            //Call onDetach
            Entity e = getEntityObject(entityId);
            componentEntry.get(entityId).onDetach(e);
            componentEntry.remove(entityId);
            freeEntityObject(e);
        }
        return this;
    }



    /**
     * Returns the component instance of a certain component type belonging to an entity
     * @param componentId: the Id of the component to retrieve
     * @param entityId: The id of the entity of which we want the component
     * @return Component: The desired component instance
     */
    public EntityComponent getComponent(String componentId, String entityId){
        EntityComponent component;

        ObjectMap<String, EntityComponent> componentEntry = this.components.get(componentId);

        component = componentEntry.get(entityId);
        return component;
    }

    /**
     * Returns all the components of a certain entity
     * @param entityId : the id of the entity
     * @return "ObjectMap<ComponentID,ComponentInstance>"
     */
    public ObjectMap<String, EntityComponent> getComponentsForEntity(String entityId){
        ObjectMap<String, EntityComponent> components = new ObjectMap<String, EntityComponent>();
        for(String compId: this.components.keys()){
            if(this.components.get(compId).containsKey(entityId)){
                components.put(compId, this.components.get(compId).get(entityId));
            }
        }
        return components;
    }



    /**
     * Returns whether or not an entity has a certain component
     * @param componentId the id of the component
     * @param entityId the Id of the entity
     * @return the component
     */
    public boolean hasComponent(String componentId, String entityId) {
        return this.components.containsKey(componentId) && this.components.get(componentId).containsKey(entityId);
    }

    /**
     * Get a list of entities having a certain property
     * @param componentId : ID of the property
     * @return as ArrayList of Entities object
     */

    public EntityCollection getEntitiesWithComponent(String componentId){
        EntityCollection entities = new EntityCollection();
        if(this.components.containsKey(componentId)){
            for(String enId : this.components.get(componentId).keys()){
                entities.add(getEntityObject(enId));
            }
        }
        return entities;
    }

    /**
     * Returns all the entities having a certain component and having that component enabled
     * @param componentId
     * @return
     */
    public EntityCollection getEntitiesWithComponentEnabled(String componentId){
        EntityCollection entities = new EntityCollection();
        if(this.components.containsKey(componentId)){
            for(String enId : this.components.get(componentId).keys()){
                if(this.components.get(componentId).get(enId).isEnabled()){
                    entities.add(getEntityObject(enId));
                }
            }
        }
        return entities;
    }


    /**
     * Returns entities having a specific tag
     * @param tag
     * @return
     */
    public EntityCollection getEntitiesWithTag(String tag){
        EntityCollection entitiesWithTag = new EntityCollection();
        for(Entity e: getEntitiesWithComponent(TagsComponent.ID)){
            if(((TagsComponent)e.getComponent(TagsComponent.ID)).hasTag(tag)){
                entitiesWithTag.add(e);
            }
        }
        return entitiesWithTag;
    }

    /**
     * Returns all the components instance of a certain type along with the Id of the entity
     * in the form of a ObjectMap
     * @param compId: the Id of the component
     * @return ObjectMap<String, Component>
     */
    public ObjectMap<String, EntityComponent> getComponentsWithEntity(String compId){
        // Return empty object map if nothing found (instead of null)
        return this.components.get(compId, new ObjectMap<String, EntityComponent>());
    }


    /**
     * Removes all references to a certain entity
     * @param entityId
     */
    public void deleteEntity(String entityId){
        for(String compId: this.components.keys()){
            this.removeComponent(compId, entityId);
            this.components.get(compId).remove(entityId);
        }
    }

    /**
     * Returns an entity object with Id
     * @param entityId
     * @return
     */
    public Entity getEntityObject(String entityId){
        Entity e = entityPool.obtain();
        e.setId(entityId);
        e.setManager(this);
        return e;
    }


    /**
     * Returns all entityPool as Entity Object
     * @return
     */
    public EntityCollection getEntities(){
        ObjectMap<String, Entity> entities = new ObjectMap<String, Entity>();
        for(String compId: this.components.keys()){
            for(Entity entity: this.getEntitiesWithComponent(compId)){
                entities.put(entity.getId(), entity);
            }
        }
        return new EntityCollection(entities.values().toArray());
    }


    /**
     * Returns all entities Id
      * @return
     */
    public ObjectSet<String> getEntityIds(){
        ObjectSet<String> ids = new ObjectSet<String>();
        for(String compId : this.components.keys()){
            ids.addAll(components.get(compId).keys().toArray());
        }
        return ids;
    }



    /**
     * Returns all the components instance of a certain type
     * @param compId: the Id of the component
     * @return ArrayList of Components or an empty array list if no instance of component type exist.
     */
    public Array<EntityComponent> getComponents(String compId){

        if(this.components.containsKey(compId)){
            return new Array<EntityComponent>(this.components.get(compId).values().toArray());
        }else{
            return new Array<EntityComponent>(); //Empty ArrayList
        }

    }


    /**
     * Returns the number of entities
     * @return
     */
    public int getEntityCount(){
        return this.getEntityIds().size;
    }

    /**
     * Indicates if an entity exists
     * @param entityId
     * @return true if an entity exists
     */
    public boolean entityExists(String entityId){
       return getEntityIds().contains(entityId);
    }

    /**
     * Deletes all the entities
     */
    public void clear(){
        for(String id: this.getEntityIds()){
            this.deleteEntity(id);
        }
    }


    public void saveIni(String outputPath) {
        IniSerializer serializer = new IniSerializer(outputPath, this);
        serializer.save();
    }

    /**
     * Loads a level
     * @param levelConfig
     */
    public void loadLevel(String levelConfig) {
        ISerialiser serializer = null;
        serializer = new JsonSerialiser();
        String levelData = Gdx.files.internal(levelConfig).readString();
        EntityFactory factory = new EntityFactory();
        factory.fromLevelData(serializer.deserialiseLevel(levelData), this);
    }

    public void saveLevel(String levelFile) {
        ISerialiser serializer = null;
        serializer = new JsonSerialiser();
        String levelData = serializer.serialiseLevel(this);
        Gdx.files.local(levelFile).writeString(levelData,false);
    }
}
