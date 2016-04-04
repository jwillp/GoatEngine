package com.goatgames.goatengine.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

/**
 * An improved BodyDef class adding support for
 * collider definition
 */
public class PhysicsBodyDef extends BodyDef {

    private Array<ColliderDef> colliderDefs = new Array<>();


    public void addColliderDef(ColliderDef def){
        colliderDefs.add(def);
    }

    public Array<ColliderDef> getColliderDefs() {
        return colliderDefs;
    }

    public void setColliderDefs(Array<ColliderDef> colliderDefs) {
        this.colliderDefs = colliderDefs;
    }
}
