package com.goatgames.goatengine.ecs.core;


import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.gdk.eventdispatcher.IEventListener;
import com.goatgames.goatengine.eventmanager.EntityEvent;

import java.util.List;

public abstract class EntitySystem implements IEventListener{

    private EntityManager entityManager;

    private EntitySystemManager systemManager;


    /**
     * Used to initialise the system
     */
    public abstract void init();

    /**
     * Handles the input
     */
    public void preUpdate() {

    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    public abstract void update(float dt);

    public void draw() {

    }

    /**
     * DeInitialise the system
     */
    public void deInit() {

    }

    /**
     * Fires an Event to all systems linked to this system
     *
     * @param event
     */
    public void fireEvent(Event event) {
        this.systemManager.fireEvent(event);
    }

    public EntitySystemManager getSystemManager() {
        return systemManager;
    }
    public void setSystemManager(EntitySystemManager systemManager) {
        this.systemManager = systemManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Registers this system for a certain event with this entities system Manager event dispatcher
     *
     * @param eventClass class of the event to register
     */
    public <T extends Event> void registerForEvent(Class<T> eventClass) {
        this.getSystemManager().getEventDispatcher().register(this, eventClass);
    }

    /**
     * Registers this system for all types of events
     */
    public void registerForAllEvents() {
        this.getSystemManager().getEventDispatcher().register(this);
    }

    @Override
    public boolean onEvent(Event event){
        return false;
    }
}
