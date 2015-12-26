package com.goatgames.goatengine.graphicsrendering;

import com.brashmonkey.spriter.Spriter;
import com.goatgames.goatengine.ecs.core.EntitySystem;

/**
 * Responsible for managing Animations
 * Spriter Animations, other animations
 */
public class AnimationSystem extends EntitySystem {


    public AnimationSystem() {}

    @Override
    public void init(){}


    @Override
    public void update(float dt){

        //Update Spriter Animations
        Spriter.update();

    }
}