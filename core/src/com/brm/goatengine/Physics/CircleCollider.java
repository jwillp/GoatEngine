package com.brm.GoatEngine.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.brm.GoatEngine.Utils.PODType;

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


    @Override
    public PODType toPODType() {
        CircleColliderDef def = new CircleColliderDef();
        def.radius = getRadius();
        def.isSensor = this.isSensor();
        def.tag = this.tag;
        // TODO get real position

        Vector2 pos = ((CircleShape)fixture.getShape()).getPosition();;

        def.x = pos.x;
        def.y = pos.y;
        return def;
    }
}
