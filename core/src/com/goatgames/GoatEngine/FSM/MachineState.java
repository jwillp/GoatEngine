package com.goatgames.goatengine.fsm;

import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;

/**
 *  A Finite State Machine State
 */
public class MachineState {

    /**
     * Called for update of the state
     * @param dt delta time
     */
    public void update(final float dt){

    }

    /**
     * Called when the state is considered the current active state of the
     * state machine
     * @param oldStateName the name of the state that was previously the active state of the machine
     * @param oldState the state that was previously the active state of the machine
     */
    public void onEnter(final String oldStateName, final MachineState oldState){

    }

    /**
     * Called when the state is no longer considered as the current active state
     * of the state machine
     * @param newStateName the name of the state that will become the new active state of the machine
     * @param newState the state that will become the new active state of the machine
     */
    public void onExit(final String newStateName, final MachineState newState){

    }

    /**
     * Called for input events
     * @param event represents the input event
     */
    public void onInputEvent(InputEvent event){

    }

    /**
     * Called for game events
     * @param event represents the game event
     */
    public void onGameEvent(GameEvent event){

    }

    /**
     * Called for collision events
     * @param event represents the collision event
     */
    public void onCollisionEvent(CollisionEvent event){

    }
}
