package com.goatgames.goatengine.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.physics.PhysicsComponent;

/**
 * Tween accessor to hadle Physics Component
 */
public class PhysicsCompAccessor implements TweenAccessor<PhysicsComponent> {

    public static final int VELOCITY_X = 0;
    public static final int VELOCITY_Y = 1;

    @Override
    public int getValues(PhysicsComponent target, int tweenType, float[] returnValues) {
        if(!GAssert.notNull(target, "target == null")) return -1;
        switch (tweenType){
            case VELOCITY_X:
                returnValues[0] = target.getVelocity().x;
                return 1;
            case VELOCITY_Y:
                returnValues[0] = target.getVelocity().y;
                return 1;
            default:
                GAssert.that(false, String.format("Invalid tween type %d", tweenType));
                return -1;
        }
    }

    @Override
    public void setValues(PhysicsComponent target, int tweenType, float[] newValues) {
        if(!GAssert.notNull(target, "target == null")) return;
        switch (tweenType){
            case VELOCITY_X:
                target.setVelocityX(newValues[0]);
                break;
            case VELOCITY_Y:
                target.setVelocityY(newValues[0]);
                break;
            default:
                GAssert.that(false, String.format("Invalid tween type %d", tweenType));
                break;
        }
    }
}
