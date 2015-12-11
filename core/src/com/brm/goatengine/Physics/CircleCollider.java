package com.brm.GoatEngine.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> toMap() {

        Map<String, String> def = new HashMap<String, String>();
        def.put("radius",String.valueOf(getRadius()));
        def.put("isSensor",String.valueOf(this.isSensor()));
        def.put("tag",String.valueOf(this.tag));
        // TODO get real position

        Vector2 pos = ((CircleShape)fixture.getShape()).getPosition();

        def.put("x",String.valueOf(pos.x));
        def.put("y",String.valueOf(pos.y));
        return def;
    }
}
