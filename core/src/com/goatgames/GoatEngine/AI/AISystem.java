package com.goatgames.goatengine.ai;

import com.goatgames.goatengine.ai.pathfinding.Pathfinder;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.utils.Timer;

/**
 *  Use to process AI logic
 */
public class AISystem extends EntitySystem {


    public final static Pathfinder pathfinder = new Pathfinder();

    Timer scanTimer = new Timer(Timer.FIVE_SECONDS);

    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        scanTimer.start();
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {
        // UPDATE PATHFINDER (RESCAN MAP)
       /* if(scanTimer.isDone()){ // TODO Find an efficient way to rescan
            pathfinder.scanMap(getEntityManager().getEntitiesWithTag("platform"));
            scanTimer.reset();
        }*/
    }
}
