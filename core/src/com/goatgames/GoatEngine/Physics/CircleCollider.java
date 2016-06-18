package com.goatgames.goatengine.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

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
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent def = new NormalisedEntityComponent();
        def.put("radius",String.valueOf(getRadius()));
        def.put("is_sensor",String.valueOf(this.isSensor()));
        def.put("tag",String.valueOf(this.tag));
        // TODO get real position
        Vector2 pos = ((CircleShape)fixture.getShape()).getPosition();
        def.put("position_x",String.valueOf(pos.x));
        def.put("position_y",String.valueOf(pos.y));
        def.put("type", "circle");
        return def;
    }
}
