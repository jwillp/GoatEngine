package com.goatgames.goatengine.ecs.core;

import com.goatgames.gdk.GAssert;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.gdk.eventdispatcher.IEventDispatcher;
import com.goatgames.goatengine.eventmanager.EntityEvent;

import java.util.LinkedHashMap;

/**
 * A Class Managing multiple Entity systems
 * This way any System could access another System's data
 */
public class EntitySystemManager {

    /**
     * All the systems managed by this Manager
     */
    private LinkedHashMap<Class, EntitySystem> systems;

    /**
     * Entity Manager containing the entities these system should
     * handle
     */
    private final EntityManager entityManager;

    /**
     * Since the systems can fire events, they be to be aware
     * of the dispatcher to which they need to fire events.
     */
    private final IEventDispatcher eventDispatcher;

    public EntitySystemManager(final EntityManager entityManager, final IEventDispatcher eventDispatcher) {
        this.entityManager = entityManager;
        this.eventDispatcher = eventDispatcher;
        systems = new LinkedHashMap<>();
    }

    /**
     * Returns a System according to its type
     *
     * @param systemType type of the system to return
     * @return the system or null if none found
     */
    @SuppressWarnings("unchecked")
    public <T extends EntitySystem> T getSystem(Class<T> systemType) {
        return (T) this.systems.get(systemType);
    }

    /**
     * Adds a System to the list of systems THE ORDER IS IMPORTANT
     *
     * @param system system to register
     */
    public void addSystem(EntitySystem system) {
        system.setSystemManager(this);
        system.setEntityManager(entityManager);
        this.systems.put(system.getClass(), system);
    }

    /**
     * Initialises all systems in order
     */
    public void initSystems() {
        for (Object system : systems.values().toArray()) {
            ((EntitySystem) system).init();
        }
    }

    /**
     * Deinitialises all systems in order
     */
    public void deInitSystems() {
        for (EntitySystem system : systems.values()) {
            system.deInit();
        }
    }

    /**
     * Handles the input for all systems in order
     */
    public void preUpdate() {
        for (EntitySystem system : systems.values()) {
            system.preUpdate();
        }
    }

    /**
     * Updates all systems in order
     */
    public void update(float deltaTime) {
        for (EntitySystem system : systems.values()) {
            system.update(deltaTime);
        }
    }

    /**
     * Fires an event to all Systems
     *
     * @param event
     */
    public void fireEvent(Event event) {
        eventDispatcher.fireEvent(event);
    }

    /**
     * Manages the draw calls of the systems
     */
    public void draw() {
        for (EntitySystem system : systems.values()) {
            system.draw();
        }
    }

    /**
     * Clears the System Manager
     */
    public void clear() {
        deInitSystems();
        this.systems.clear();
    }

    /**
     * Returns the system manager's event dispatcher
     * @return
     */
    public IEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
}
