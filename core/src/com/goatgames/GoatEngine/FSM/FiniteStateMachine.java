package com.goatgames.goatengine.fsm;

import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.utils.GAssert;

/**
 * A Very simple Finite State Machine
 */
public class FiniteStateMachine {

    private ObjectMap<String, MachineState> states;
    private String currentStateName;

    /**
     * Default constructor Constructor
     */
    public FiniteStateMachine(){
        states = new ObjectMap<>();
        currentStateName = null;
    }

    /**
     * Adds a state
     * @param stateName
     * @param state
     */
    public FiniteStateMachine addState(final String stateName, MachineState state){
        if(GAssert.notNull(state, "state == null") && GAssert.notNull(stateName, "stateName == null"))
            states.put(stateName, state);
        return this;
    }

    /**
     * Initialises the state machine with a certain state
     * @param stateName
     */
    public void initialise(final String stateName){
        if(GAssert.notNull(stateName, "stateName == null")) {
            if (GAssert.that(states.containsKey(stateName),
                    String.format("State machine does not have a state named : %s", stateName))) {
                this.currentStateName = stateName;
            }
        }
    }

    /**
     * Returns the current state instance
     * @return an instance of the current state
     */
    public MachineState getCurrentState(){
        return this.states.get(currentStateName);
    }

    /**
     * Returns the current state name
     * @return the current state name
     */
    public final String getCurrentStateName(){
        return this.currentStateName;
    }

    /**
     * Changes the current state.
     * @param stateName
     */
    public void changeState(final String stateName){
        // If the currentStateName is null, it means that the method initialise was not called yet.
        // in such conditions
        if(GAssert.notNull(currentStateName, "State Machine was not initialised")) {
            if (GAssert.that(states.containsKey(stateName),
                    String.format("State machine does not have a state named : %s", stateName))) {

                final String oldStateName = currentStateName;
                MachineState oldState = states.get(oldStateName);

                MachineState newState = states.get(stateName);

                oldState.onExit(stateName, newState);
                newState.onEnter(oldStateName, oldState);

                currentStateName = stateName;
            }
        }
    }

    /**
     * Indicates if the current state machine has a state with the specified name
     * @param stateName the name of the state to check.
     * @return true if the machine has a state with specified name, otherwise false.
     */
    public boolean hasState(final String stateName){
        return this.states.containsKey(stateName);
    }
    /**
     * Clears any added state
     */
    public void clear(){
        states.clear();
    }

    /**
     * Called for update of the state
     * @param dt delta time
     */
    public void update(float dt){
        this.getCurrentState().update(dt);
    }

    /**
     * Called for input events
     * @param event represents the input event
     */
    public void onInputEvent(InputEvent event){
        this.getCurrentState().onInputEvent(event);
    }

    /**
     * Called for game events
     * @param event represents the game event
     */
    public void onGameEvent(GameEvent event){
        this.getCurrentState().onGameEvent(event);
    }

    /**
     * Called for collision events
     * @param event represents the collision event
     */
    public void onCollisionEvent(CollisionEvent event){
        this.getCurrentState().onCollisionEvent(event);
    }
}
