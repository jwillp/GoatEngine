package com.goatgames.goatengine.scriptingengine.common;

import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.gdk.eventdispatcher.EventDispatcher;
import com.goatgames.gdk.eventdispatcher.IEventDispatcher;
import com.goatgames.goatengine.GoatEngine;
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
    void init(final Entity entity);

    /**
     * Called every gameloop tick
     *
     * @param deltaTime delta time
     */
    void update(final Entity entity, float deltaTime);

    /**
     *  Called on user input events
     *
     * @param entity entity owning the this instance of the script
     * @param event input event
     */
    void onInputEvent(final Entity entity, InputEvent event);

    /**
     * Called when a collision occurs with the entity
     *
     * @param entity
     * @param collisionEvent collision event instance
     */
    void onCollision(final Entity entity, CollisionEvent collisionEvent);

    /**
     * Called when a game event occurs
     *
     * @param entity entity owning the this instance of the script
     * @param event event
     */
    void onGameEvent(final Entity entity, GameEvent event);

    /**
     * Called when an event concerning the current entity occurs.
     *
     * @param entity the current entity object
     * @param event the event instance
     */
    void onEntityEvent(final Entity entity, EntityEvent event);

    /**
     * Called when the script is detached from the entity
     */
    void onDetach(final Entity entity);

    void postRender(final Entity entity);

    void preRender(final Entity entity);

    void lateUpdate(final Entity entity);

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

    /**
     * Fires an event to the right event dispatcher for the current context
     *
     * @param event event to fire
     */
    void fireEvent(final Event event);

    /**
     * Sets the event dispatcher so the script can fire events
     * @param eventDispatcher
     */
    public void setEventDispatcher(final IEventDispatcher eventDispatcher);
}
