package com.goatgames.goatengine.ecs.core;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.goatgames.gdk.GAssert;

public class Entity implements Pool.Poolable {

    protected String ID;
    protected EntityManager manager;


    public Entity() {
    }

    public Entity(String id) {
        this.setId(id);
    }

    public String getId() {
        return ID;
    }

    public void setId(String ID) {
        this.ID = ID;
    }

    /**
     * WRAPPER METHOD adds a component to the entity in the entity manager
     *
     * @param cp     the component
     * @param compId the id of the Component
     * @return this for chaining
     */
    public Entity addComponent(EntityComponent cp, String compId) {
        if (!GAssert.notNull(manager,
                String.format("manager == null, Unregistered entity %s", this.getId())))
            throw new UnregisteredEntityException();
        manager.addComponent(compId, cp, getId());
        return this;
    }


    // Wrapper methods //

    /**
     * WRAPPER METHOD removes a component from an entity
     *
     * @param componentId id of the component to remove
     * @return this for chaining
     */
    public Entity removeComponent(String componentId) {
        if (!GAssert.notNull(manager, "manager == null")) throw new UnregisteredEntityException();
        manager.removeComponent(componentId, getId());
        return this;
    }

    /**
     * WRAPPER METHOD Gets a component using its ID
     *
     * @param componentId the id of the component
     * @return the component
     */
    public EntityComponent getComponent(String componentId) {
        if (!GAssert.notNull(manager, "manager == null")) throw new UnregisteredEntityException();

        EntityComponent component = manager.getComponent(componentId, getId());
        if (!GAssert.notNull(component, "component == null"))
            throw new EntityComponentNotFoundException(componentId);
        return component;
    }

    /**
     * WRAPPER METHOD Returns whether or not the entity has a certain Component
     *
     * @param componentId the id of the component
     * @return true if has the component, false otherwise
     */
    public boolean hasComponent(String componentId) {
        if (!GAssert.notNull(manager, "manager == null")) {
            throw new UnregisteredEntityException();
        }
        return manager.hasComponent(componentId, getId());
    }

    /**
     * Returns if the entity has a certain Component and if that component is Enabled
     * If it does not have the component or that component is disabled returns false
     * otherwise true
     *
     * @return true if has the component and it is enabled, false otherwise
     */
    public boolean hasComponentEnabled(String componentId) {
        return hasComponent(componentId) && getComponent(componentId).isEnabled();
    }

    /**
     * Enables a Component
     */
    public void enableComponent(String componentId) {
        this.getComponent(componentId).setEnabled(true);
    }

    /**
     * Disables a Component
     */
    public void disableComponent(String componentId) {
        this.getComponent(componentId).setEnabled(false);
    }

    /**
     * Returns the manager of the entity
     *
     * @return the manager of the entity
     */
    public EntityManager getManager() {
        return manager;
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    /**
     * Returns the component of the entity
     *
     * @return ObjectMap containing the components of the entity
     */
    public ObjectMap<String, EntityComponent> getComponents() {
        return manager.getComponentsForEntity(this.getId());
    }

    /**
     * Resets the object for reuse. Object references should be set to null and fields may be set to default values.
     */
    @Override
    public void reset() {
        this.setManager(null);
        this.setId("");
    }

    /// EXCEPTIONS ///

    /**
     * Thrown when an unregistered entity tries to access a null EntityManager
     */
    public static class UnregisteredEntityException extends RuntimeException {
        public UnregisteredEntityException() {
            super("The Entity is not registered to any EntityManager");
        }
    }

    /**
     * Exception thrown when the game engine tries to use a component from an entity
     * that does not poses that particular component
     */
    public static class EntityComponentNotFoundException extends RuntimeException {
        //Constructor that accepts a message
        public EntityComponentNotFoundException(String componentName) {
            super(String.format("The Component \"%s\" was not found in Entity", componentName));
        }
    }
}

