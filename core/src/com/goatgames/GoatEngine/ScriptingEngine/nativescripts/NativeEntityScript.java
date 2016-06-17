package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.scriptingengine.IEntityScript;

/**
 * Abstract Native Script class to be implemented by Native Scripts
 */
public abstract class NativeEntityScript implements IEntityScript {

    private boolean initialised;

    /**
     * Called when the script instance is considered initialised by the engine.
     * it can also be considered as a method to call when the script instance is attached to the entity
     * @param entity
     */
    public abstract void init(Entity entity);

    /**
     * Called every gameloop tick
     * @param dt delta time
     */
    public abstract void update(float dt);

    /**
     *  Called on user input events
     * @param event
     */
    public abstract void onInputEvent(InputEvent event);

    /**
     * Called when a collision occurs with the entity
     * @param collisionEvent
     */
    public abstract void onCollision(CollisionEvent collisionEvent);

    /**
     * Called when a game event occurs
     * @param event
     */
    public abstract void onGameEvent(GameEvent event);

    /**
     * Called when the script is detached from the entity
     */
    public abstract void onDetach();

    public boolean isInitialised() {
        return initialised;
    }

    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    public abstract void postRender();

    public abstract void preRender();

    public abstract void lateUpdate();

    /**
     * Returns a string uniquely identifying the script (not the script instance)
     * @return
     */
    public String getName(){
        return this.getClass().getName();
    }
}
