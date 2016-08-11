package com.goatgames.goatengine.scriptingengine.common;

import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.eventmanager.EntityEvent;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;

/**
 * Represents a script
 */
public interface IEntityScript {

    /**
     * Called when the script instance is considered initialised by the engine.
     * it can also be considered as a method to call when the script instance is attached to the entity
     *
     * @param entity entity owning the this instance of the script
     */
    void init(Entity entity);

    /**
     * Called every gameloop tick
     *
     * @param deltaTime delta time
     */
    void update(Entity entity, float deltaTime);

    /**
     *  Called on user input events
     *
     * @param entity entity owning the this instance of the script
     * @param event input event
     */
    void onInputEvent(Entity entity, InputEvent event);

    /**
     * Called when a collision occurs with the entity
     *
     * @param entity
     * @param collisionEvent collision event instance
     */
    void onCollision(Entity entity, CollisionEvent collisionEvent);

    /**
     * Called when a game event occurs
     *
     * @param entity entity owning the this instance of the script
     * @param event event
     */
    void onGameEvent(Entity entity, GameEvent event);

    /**
     * Called when an event concerning the current entity occurs.
     *
     * @param entity the current entity object
     * @param event the event instance
     */
    void onEntityEvent(Entity entity, EntityEvent event);

    /**
     * Called when the script is detached from the entity
     */
    void onDetach(Entity entity);

    void postRender(Entity entity);

    void preRender(Entity entity);

    void lateUpdate(Entity entity);

    /**
     * Indicates whether or not the script is initialised
     * @return true if the script is initialised, false otherwise
     */
    boolean isInitialised();

    /**
     * Returns a string uniquely identifying the script (not the script instance)
     * @return the name of the script
     */
    String getName();
}
