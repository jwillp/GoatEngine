package com.brm.GoatEngine.Physics;

/**
 * A circular collider
 */
public class CircleCollider extends Collider{

    /**
     * Returns the radius
     * @return
     */
    public float getRadius(){
        return this.fixture.getShape().getRadius();
    }



}
