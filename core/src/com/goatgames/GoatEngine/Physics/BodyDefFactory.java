package com.goatgames.goatengine.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * A Factory class to easily create all types of physics bodies
 */
public class BodyDefFactory {

    /**
     * Creates a body definition with a single box collider
     * @return a rectangle shaped body
     */
    public static PhysicsBodyDef createStaticBox(float width, float height, boolean sensor){
        PhysicsBodyDef bodyDef = new PhysicsBodyDef();

        // Body def
        bodyDef.allowSleep = false;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;

        // Box Collider
        BoxColliderDef boxDef = new BoxColliderDef();
        boxDef.height = height * 0.5f;
        boxDef.width = width * 0.5f;
        boxDef.isSensor = sensor;
        boxDef.x = 0;
        boxDef.y = 0;

        bodyDef.addColliderDef(boxDef);

        return bodyDef;
    }

    /**
     * Creates a body definition with a single box collider acting as trigger
     * @return a rectangle shaped body
     */
    public static PhysicsBodyDef createTriggerBox(float width, float height){
       return createStaticBox(width,height,true);
    }



}
