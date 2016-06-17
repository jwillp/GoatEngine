package com.goatgames.goatengine.scriptingengine;

import com.goatgames.goatengine.ecs.core.Entity;
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
     * @param entity
     */
    void init(Entity entity);

    /**
     * Called every gameloop tick
     * @param dt delta time
     */
    void update(Entity entity, float dt);

    /**
     *  Called on user input events
     * @param event
     */
    void onInputEvent(Entity entity, InputEvent event);

    /**
     * Called when a collision occurs with the entity
     * @param collisionEvent
     */
    void onCollision(Entity entity, CollisionEvent collisionEvent);

    /**
     * Called when a game event occurs
     * @param event
     */
    void onGameEvent(Entity entity, GameEvent event);

    /**
     * Called when the script is detached from the entity
     */
    void onDetach(Entity entity);

    void postRender(Entity entity);

    void preRender(Entity entity);

    void lateUpdate(Entity entity);

    /**
     * Indicates wether or not the script is initialised
     * @return true if the script is initialised
     */
    boolean isInitialised();

    /**
     * Returns a string uniquely identifying the script (not the script instance)
     * @return
     */
    String getName();
}
