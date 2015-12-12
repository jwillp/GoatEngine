package com.brm.GoatEngine.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;

import java.util.Iterator;
import java.util.Map;

/**
 * Used for Colliders
 */
public abstract class Collider{
    protected String tag;
    protected Fixture fixture;
    protected Object userData;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    /**
     * Returns whether or not the collider is a sensor
     * @return true if sensor
     */
    public boolean isSensor(){
        return fixture.isSensor();
    }





                             // STATIC METHODS //
    /**
    * Creates a body according to some specifications
    * @param world the Box2D world in which to create the body
    * @param bodyType the type of the Body
    */
    public static Body createBody(World world, BodyDef.BodyType bodyType, Vector2 position){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        return world.createBody(bodyDef);
        // Attach new Fixture to Body
        //World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
    }


    /**
     * Adds a Box collider to an entity
     * @param entity
     */
    public static void addBoxCollider(Entity entity, BoxColliderDef boxDef){

        BoxCollider box = new BoxCollider();
        PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);

        // Set the tag
        box.setTag(boxDef.tag);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(boxDef.width, boxDef.height);
        box.setSize(boxDef.width, boxDef.height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = boxDef.isSensor;

        box.setFixture(phys.getBody().createFixture(fixtureDef));
        box.getFixture().setUserData(box);

        box.setUserData(boxDef.userdata);
    }

    /**
     * Adds a Circle collider to an entity
     * @param entity
     * @param circleDef
     */
    public static void addCircleCollider(Entity entity, CircleColliderDef circleDef){

        CircleCollider circleCollider = new CircleCollider();
        PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);

        // Set the tag
        circleCollider.setTag(circleDef.tag);


        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circleDef.radius);
        circleShape.setPosition(new Vector2(circleDef.x, circleDef.y));


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = circleDef.isSensor;

        // TODO Move in the Collider definition
        fixtureDef.density = 0.1f;



        circleCollider.setFixture(phys.getBody().createFixture(fixtureDef));
        circleCollider.getFixture().setUserData(circleCollider);

        circleCollider.setUserData(circleDef.userdata);

    }

    public static void addCollider(Entity entity, ColliderDef colliderDef){
        if(colliderDef instanceof BoxColliderDef){
            addBoxCollider(entity, (BoxColliderDef) colliderDef);
        }
        else if(colliderDef instanceof CircleColliderDef){
            addCircleCollider(entity, (CircleColliderDef) colliderDef);
        }
        else if(colliderDef instanceof CapsuleColliderDef){
            addCapsuleCollider(entity, (CapsuleColliderDef) colliderDef);
        }

    }

    public static void addCapsuleCollider(Entity entity, CapsuleColliderDef def){

        ///MIDDLE
        BoxColliderDef middleDef = def.middleColliderDef;
        middleDef.width = def.width * 0.90f;
        middleDef.height = def.height * 0.5f;
        middleDef.x = def.x;
        middleDef.y = def.y;
        middleDef.isSensor = def.isSensor;
        addBoxCollider(entity, middleDef);


        // Circle TOP (HEAD)
        CircleColliderDef topDef = def.topColliderDef;
        topDef.radius = def.width;
        topDef.x = def.x;
        topDef.y = def.y + def.height*0.5f;
        topDef.isSensor = def.isSensor;
        addCircleCollider(entity, topDef);


        // Circle BOTTOM (LEGS)
        CircleColliderDef bottomDef = def.bottomColliderDef;
        bottomDef.radius = def.width;
        bottomDef.x = def.x;
        bottomDef.y = def.y  - def.height*0.5f;
        bottomDef.isSensor = def.isSensor;
        addCircleCollider(entity, bottomDef);


        //FEET FIXTURE
        /*PolygonShape footSensor = new PolygonShape();
        footSensor.setAsBox(0.1f,0.1f, new Vector2(0, -height), 0);*/
    }



    /**
     * Removes a specific collider object from the body
     * @param entity then entity to remove the collider from
     * @param collider the collider object
     */
    public static void removeCollider(Entity entity, Collider collider){
        PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        for(Iterator<Fixture> iterator = phys.getBody().getFixtureList().iterator(); iterator.hasNext(); ) {
            Fixture fix = iterator.next();
            if(fix.getUserData() == collider){
                iterator.remove();
            }
        }
    }

    /**
     * Removes a collider from an entity
     * @param entity the entity to remove the collider from
     * @param tag the tag of the collider
     */
    public static void removeCollider(Entity entity, String tag){
        PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        for(Iterator<Fixture> iterator = phys.getBody().getFixtureList().iterator(); iterator.hasNext(); ) {
            Fixture fix = iterator.next();
            Collider col = (Collider) fix.getUserData();
            if(col.getTag().equals(tag)){
                iterator.remove();
            }
        }
    }


    /**
     * Removes all Colliders/fixtures from a body
     */
    public static void removeColliders(Entity entity){
        PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        for(Iterator<Fixture> iterator = phys.getBody() .getFixtureList().iterator(); iterator.hasNext(); ) {
            iterator.remove();
        }
    }

    // TODO make something like entityComponent with makeMap instead
    public abstract Map<String, String> toMap();
}
