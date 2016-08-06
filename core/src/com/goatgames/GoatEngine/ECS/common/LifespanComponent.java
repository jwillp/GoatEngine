package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.utils.Timer;

/**
 * Used to schedule the destruction of an entity
 */
public class LifespanComponent extends EntityComponent{
    public static final String ID = "LIFESPAN_COMPONENT";

    private Timer timer;

    /**
     * Ctor initialising the component to schedule removal of entity
     * at a certain delay
     * @param delay the delay in ms
     */
    public LifespanComponent(int delay){
        super(true);
        timer = new Timer(delay);
        timer.start();
    }

    @Override
    public String getId() {
        return ID;
    }


    public boolean isFinished() {
        return timer.isDone();
    }
}
