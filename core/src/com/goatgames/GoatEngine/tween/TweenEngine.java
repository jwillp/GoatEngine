package com.goatgames.goatengine.tween;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.goatgames.goatengine.physics.PhysicsComponent;

/**
 * Tween Engine to bve used by game screen willing to incorporate tween effects
 */
public class TweenEngine {

    static {
        Tween.registerAccessor(PhysicsComponent.class, new PhysicsCompAccessor());
    }


    /**
     * Used for tweening
     */
    private TweenManager tweenManager;

    public TweenEngine(){
        tweenManager = new TweenManager();
    }

    /**
     * Updates the tween engine
     *
     * @param deltaTime time elapsed in seconds since last frame
     */
    public void update(float deltaTime){
        tweenManager.update(deltaTime);
    }

}
