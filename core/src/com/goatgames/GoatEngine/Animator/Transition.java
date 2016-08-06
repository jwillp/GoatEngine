package com.goatgames.goatengine.animator;

import com.goatgames.goatengine.utils.Timer;

/**
 * A transition
 */
public class Transition {

    private final AnimState currentState;
    private final AnimState nextState;
    private final Condition condition;
    private Timer duration; //The duration of a transition

    public Transition(AnimState from, AnimState to, Condition condition) {
        currentState = from;
        nextState = to;
        this.condition = condition;
    }

    /**
     * Returns whether or not the transition can change state
     *
     * @return
     */
    public boolean canChange() {
        return this.condition.evaluate();
    }


    public AnimState getCurrentState() {
        return currentState;
    }

    public AnimState getNextState() {
        return nextState;
    }

    public Timer getDuration() {
        return duration;
    }

    public void setDuration(Timer duration) {
        this.duration = duration;
    }

    public Condition getCondition() {
        return condition;
    }


}
