package com.goatgames.goatengine.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityCollection;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.screenmanager.GameScreenConfig;
import com.goatgames.goatengine.utils.GAssert;

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
    private final static float FPS = 1/60f;

    private ArrayList<CollisionEvent> collisions = new ArrayList<CollisionEvent>();

    public PhysicsSystem() {
        Box2D.init();

        GameScreenConfig config = GoatEngine.gameScreenManager.getCurrentScreen().getConfig();
        //Gravity
        final float GRAVITY_X = config.getFloat("physics.gravity.x");
        final float GRAVITY_Y = config.getFloat("physics.gravity.y");
        world = new World(new Vector2(GRAVITY_X,GRAVITY_Y), true);
        world.setContactListener(this);
    }

    @Override
    public void init(){

    }

    @Override
    public void preUpdate() {
        createBodies();
    }

    @Override
    public void update(float dt) {


        applyTransform();
        updatePhysics();
        applyPhysics();

        // UPDATE CALLBACKS
        for(CollisionEvent event : collisions) {
            this.fireEvent(event);
        }
        collisions.clear();
    }

    @Override
    public void draw(){}

    /**
     * Create bodies as needed by looking a PhysicsComponents body definition
     */
    private void createBodies(){
        ObjectMap<String, EntityComponent> componentsWithEntity = getEntityManager().getComponentsWithEntity(PhysicsComponent.ID);
        for(ObjectMap.Entry<String, EntityComponent> entry : componentsWithEntity){
            PhysicsComponent phys = (PhysicsComponent) entry.value;
            // Create body
            if(phys.getBody() == null){
                PhysicsBodyDef bodyDef = phys.getBodyDef();
                Body body = world.createBody(bodyDef);
                phys.setBody(body,entry.key);
                // Colliders
                for(ColliderDef def : bodyDef.getColliderDefs()){
                    Collider.addCollider(phys,def);
                }
            }
        }
    }


    /**
     * Applies information from transform
     * component to physics body
     */
    private void applyTransform(){
        EntityCollection collection = getEntityManager().getEntitiesWithComponent(PhysicsComponent.ID);
        for(Entity entity : collection){
            // If entity does not have a Transform Component at this point add one
            if(!entity.hasComponent(TransformComponent.ID)){
                entity.addComponent(new TransformComponent(),TransformComponent.ID);
                continue;
            }
            PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
            TransformComponent transform = (TransformComponent)entity.getComponent(TransformComponent.ID);

            phys.setPosition(transform.getX(), transform.getY());
            //phys.setAngle(); // TODO Angle
        }
    }


    /**
     * Apply the changes of the physics computation
     * on the transform components using body information
     */
    private void applyPhysics(){

        EntityCollection collection = getEntityManager().getEntitiesWithComponent(PhysicsComponent.ID);
        // Take for granted that this entities have a Transform component if not GAssert and await crash
        for(Entity entity : collection){
            GAssert.that(entity.hasComponent(TransformComponent.ID),
                    "Entity has PhysicsComponent but no TransformComponent " + entity.getId());
            /*if(!entity.hasComponent(TransformComponent.ID)){
                return;
            }*/
            PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
            TransformComponent transform = (TransformComponent)entity.getComponent(TransformComponent.ID);

            transform.setX(phys.getPosition().x);
            transform.setY(phys.getPosition().y);
            transform.setSize(phys.getWidth(),phys.getHeight());
            // TODO Angle
        }

    }

    /**
     * Updates box2D physics
     */
    private void updatePhysics(){
        //Update the box2D world
        world.step(FPS, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }


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

        // Assertion
        GAssert.notNull(entityA, "Physics System Collision: Body 'A' does not have any user data");
        GAssert.notNull(entityB, "Physics System Collision: Body 'B' does not have any user data");

        GAssert.notNull(fixtureA, "Physics System Collision: Fixture A does not have any user data");
        GAssert.notNull(fixtureB, "Physics System Collision: Fixture B does not have any user data");

        // Add Events
        this.collisions.add(new CollisionEvent(entityA, fixtureA, entityB, fixtureB, describer));
        this.collisions.add(new CollisionEvent(entityB, fixtureB, entityA, fixtureA, describer));
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
