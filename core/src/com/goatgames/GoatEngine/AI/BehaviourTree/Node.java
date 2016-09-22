package com.goatgames.goatengine.ai.behaviourtree;

import java.util.Map;

/**
 *
 * @author FireRaccoon
 */
public abstract class Node {

    /**
     * The possible states of a node
     */
    public enum State{
        INVALID,  //Needs a precondition in order to be executed
        SUCCESS, //Success at executing
        FAILED,  //Failed at executing
        RUNNING  //Has not finished yet (needs another tick)
    }

    protected  State state;
    protected Node(){}

    /**
     * Updates the state of a node
     * @param blackBoard the black board to use
     * @return
     */
    public abstract State update(Map<String, Object> blackBoard);

    /**
     * Precondition in order for the node to be updated
     * @return
     */
    public boolean precondition(Map<String, Object> blackBoard){ return true; }

    /**
     * Called when a node is initialized
     */
    public void onInitialize(){}

    /**
     * Called when a node is Terminated
     * @param state
     */
    public void onTerminate(State state){}

    /**
     * Main method for a loop
     * @param blackBoard the blackboard to use for the current node
     * @return the final STATE of the Node
     */
    public State tick(Map<String, Object> blackBoard){

        if(!precondition(blackBoard)){
            return State.FAILED;
        }


        if(state == State.INVALID) {
            onInitialize();
        }

        state = update(blackBoard);

        if(state != State.RUNNING)
            onTerminate(state);

        return state;
    }

}
