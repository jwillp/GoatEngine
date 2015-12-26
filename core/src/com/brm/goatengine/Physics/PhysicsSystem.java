package com.brm.GoatEngine.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntitySystem;

import java.util.ArrayList;

/**
 * Responsible for checking collisions, making the entities move
 * and apply gravity to the necessary entities
 */
public class PhysicsSystem extends EntitySystem implements ContactListener {

    private World world;        // The Physics World

    // Recommended Settings
    private final static int VELOCITY_ITERATIONS = 6;
    private final static int POSITION_ITERATIONS = 2;

    private ArrayList<CollisionEvent> collisions = new ArrayList<CollisionEvent>();

    public PhysicsSystem() {
        Box2D.init();
        world = new World(new Vector2(0, -40f), true);  // TODO get gravity from Screen Config File
        world.setContactListener(this);
    }

    @Override
    public void init(){}

    @Override
    public void update(float dt) {
        //Update the box2D world
        world.step(1 / 60f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        // UPDATE CALLBACKS
        for(CollisionEvent event : collisions) {
            this.fireEvent(event);
        }
        collisions.clear();
    }

    @Override
    public void draw(){}


    // CONTACT LISTENING
    /**
     * Dispatches the contact between two entities in their respective PhysicsComponent
     * to be used by other systems, to accomplish certain tasks accordingly
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        onContact(contact, CollisionEvent.Describer.BEGIN);
    }

    /**
     * Removes the contact between two entities in their respective PhysicsComponent
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
        onContact(contact, CollisionEvent.Describer.END);
    }

    /**
     * Sends collision events to the Event Manager
     */
    private void onContact(Contact contact, CollisionEvent.Describer describer){

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        String entityA = (String) fixtureA.getBody().getUserData();
        String entityB = (String) fixtureB.getBody().getUserData();


        if(fixtureA.getUserData() != null && fixtureA.getBody().getUserData() != null) {
            this.collisions.add(new CollisionEvent(
                            entityA,
                            fixtureA,
                            entityB,
                            fixtureB,
                            describer)
            );
        }
        if(fixtureB.getUserData() != null && fixtureB.getBody().getUserData() != null) {
            this.collisions.add(new CollisionEvent(
                            entityB,
                            fixtureB,
                            entityA,
                            fixtureA,
                            describer)
            );
        }
    }







    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}


    // GETTERS AND SETTERS //

    public World getWorld() {
        return world;
    }


    public void setGravity(Vector2 gravity) {
        this.getWorld().setGravity(gravity);
    }
}
