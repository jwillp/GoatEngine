package com.goatgames.goatengine.ecs.core;


import com.goatgames.goatengine.eventmanager.EntityEvent;

public abstract class EntitySystem {

    private EntityManager entityManager;

    private EntitySystemManager systemManager;


    /**
     * Used to initialise the system
     */
    public abstract void init();

    /**
     * Called once per game frame
     * @param dt
     */
    public abstract void update(float dt);


    /**
     * Handles the input
     */
    public void preUpdate(){}


    /**
     * Handles the input for a given entity
     */
    private void handleInputForEntity(Entity entity){}


    /**
     * DeInitialise the system
     */
    public void deInit(){}

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
     * Fires an Event to all systems linked to this system
     * @param event
     */
    public void fireEvent(EntityEvent event){
        this.systemManager.fireEvent(event);
    }


    /**
     * Called when the system receives an Entity Event
     * @param event
     * @param <T>
     */ //TODO ABSTRACTING
    public <T extends EntityEvent>  void onEntityEvent(T event){}

    public void draw(){};
}
