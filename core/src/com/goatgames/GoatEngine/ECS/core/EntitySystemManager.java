package com.goatgames.goatengine.ecs.core;

import com.badlogic.gdx.Gdx;
import com.goatgames.gdk.eventdispatcher.IEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.eventmanager.EntityEvent;
import com.goatgames.gdk.eventdispatcher.Event;

import java.util.LinkedHashMap;

/**
 * A Class Managing multiple managers
 * This way any System could access another System's data
 */
public class EntitySystemManager implements IEventListener {

    private ECSManager ecsManager;
    private LinkedHashMap<Class, EntitySystem> systems;


    public EntitySystemManager(ECSManager manager) {
        ecsManager = manager;
        systems = new LinkedHashMap<>();
    }

    /**
     * Returns a System
     *
     * @param systemType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends EntitySystem> T getSystem(Class<T> systemType) {
        return (T) this.systems.get(systemType);
    }

    /**
     * Adds a System to the list of systems THE ORDER IS IMPORTANT
     *
     * @param systemType
     * @param system
     * @param <T>
     */
    public <T extends EntitySystem> void addSystem(Class<T> systemType, EntitySystem system) {
        system.setSystemManager(this);
        system.setEntityManager(this.ecsManager.getEntityManager());
        this.systems.put(systemType, system);
    }

    /**
     * Inits all systems in order
     */
    public void initSystems() {
        for (Object system : systems.values().toArray()) {
            ((EntitySystem) system).init();
        }
    }

    /**
     * Deinits all systems in order
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
    public void update() {
        for (EntitySystem system : systems.values()) {
            system.update(Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Fires an event to all Systems
     *
     * @param event
     */
    public void fireEvent(Event event) {
        // TODO use current screen or get Event Manager as a reference in ctor
        GoatEngine.eventManager.fireEvent(event);
    }

    @Override
    public boolean onEvent(Event e) {
        for (EntitySystem system : this.systems.values()) {
            if (e instanceof EntityEvent)  // TODO this is a Quickfix
                system.onEntityEvent((EntityEvent) e);
        }
        return false;
    }

    public void draw() {
        for (EntitySystem system : systems.values()) {
            system.draw();
        }
    }
}
