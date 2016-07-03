package com.goatgames.goatengine.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.gdk.GAssert;

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


                             // STATIC METHODS // TODO Move in a factory?
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
        addBoxCollider((PhysicsComponent)entity.getComponent(PhysicsComponent.ID), boxDef);
    }

    /**
     * Adds a Box collider to a physics component
     * @param phys
     */
    public static void addBoxCollider(PhysicsComponent phys, BoxColliderDef boxDef){
        BoxCollider box = new BoxCollider();

        // Set the tag
        box.setTag(boxDef.tag);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(boxDef.width, boxDef.height, new Vector2(boxDef.x, boxDef.y) ,0);
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
        addCircleCollider((PhysicsComponent) entity.getComponent(PhysicsComponent.ID),circleDef);
    }

    /**
     * Adds a Circle collider to a physics component
     * @param phys
     * @param circleDef
     */
    public static void addCircleCollider(PhysicsComponent phys, CircleColliderDef circleDef){

        CircleCollider circleCollider = new CircleCollider();


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


    /**
     * Adds a collider to an entity
     * @param entity
     * @param colliderDef
     */
    public static void addCollider(Entity entity, ColliderDef colliderDef){
        addCollider((PhysicsComponent) entity.getComponent(PhysicsComponent.ID), colliderDef);
    }



    /**
     * Adds a collider to a physics component
     * @param phys
     * @param colliderDef
     */
    public static void addCollider(PhysicsComponent phys, ColliderDef colliderDef){
        if(colliderDef instanceof BoxColliderDef){
            addBoxCollider(phys, (BoxColliderDef) colliderDef);
        }
        else if(colliderDef instanceof CircleColliderDef){
            addCircleCollider(phys, (CircleColliderDef) colliderDef);
        }
        else if(colliderDef instanceof CapsuleColliderDef){
            addCapsuleCollider(phys, (CapsuleColliderDef) colliderDef);
        }
        phys.setDirty(true);
    }


    /**
     * Adds a cpasule collider to an entity
     * @param entity
     * @param def
     */
    public static void addCapsuleCollider(Entity entity, CapsuleColliderDef def) {
        addCapsuleCollider((PhysicsComponent)entity.getComponent(PhysicsComponent.ID),def);
    }


        /**
         * Adds a cpasule collider to a physics component
         * @param phys
         * @param def
         */
    public static void addCapsuleCollider(PhysicsComponent phys, CapsuleColliderDef def){

        ///MIDDLE
        BoxColliderDef middleDef = def.middleColliderDef;
        middleDef.width = def.width * 0.90f;
        middleDef.height = def.height * 0.5f;
        middleDef.x = def.x;
        middleDef.y = def.y;
        middleDef.isSensor = def.isSensor;
        addBoxCollider(phys, middleDef);


        // Circle TOP (HEAD)
        CircleColliderDef topDef = def.topColliderDef;
        topDef.radius = def.width;
        topDef.x = def.x;
        topDef.y = def.y + def.height*0.5f;
        topDef.isSensor = def.isSensor;
        addCircleCollider(phys, topDef);


        // Circle BOTTOM (LEGS)
        CircleColliderDef bottomDef = def.bottomColliderDef;
        bottomDef.radius = def.width;
        bottomDef.x = def.x;
        bottomDef.y = def.y  - def.height*0.5f;
        bottomDef.isSensor = def.isSensor;
        addCircleCollider(phys, bottomDef);

    }



    /**
     * Removes a specific collider object from the body of an entity
     * @param entity then entity to remove the collider from
     * @param collider the collider object
     */
    public static void removeCollider(Entity entity, Collider collider){
       removeCollider((PhysicsComponent) entity.getComponent(PhysicsComponent.ID), collider);
    }

    /**
     * Removes a specific collider object from the body of a Physics component
     * @param phys the physics component to update
     * @param collider the collider object
     */
    public static void removeCollider(PhysicsComponent phys, Collider collider){
        for(Iterator<Fixture> iterator = phys.getBody().getFixtureList().iterator(); iterator.hasNext(); ) {
            Fixture fix = iterator.next();
            if(fix.getUserData() == collider){
                iterator.remove();
            }
        }
        phys.setDirty(true);
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
        phys.setDirty(true);
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

    // TODO make something like entityComponent with normalise instead
    public abstract NormalisedEntityComponent normalise();

    /**
     * Updates a collider according to a new Definition, must be of same type
     * @param collider
     * @param newDef
     */
    public static void updateCollider(Entity entity, Collider collider, ColliderDef newDef) {
       // removeCollider(entity, collider);
        //addCollider(entity, newDef);
        updateCollider((PhysicsComponent) entity.getComponent(PhysicsComponent.ID), collider, newDef);
    }

    /**
     * Updates a collider according to a new Definition, must be of same type
     * @param physComp
     * @param collider
     * @param newDef
     */
    public static void updateCollider(PhysicsComponent physComp, Collider collider, ColliderDef newDef){
        removeCollider(physComp, collider);
        addCollider(physComp, newDef);
    }


    /**
     * Makes a collider def from a Map
     * @param colliderData
     * @return
     */
    public static ColliderDef defFromMap(Map<String,String> colliderData){
        GAssert.that(colliderData.containsKey("type"), "Invalid Collider Def : No Type found");
        String colType = colliderData.get("type");
        ColliderDef colDef;
        // Circle Collider //
        if(colType.equals("circle")){
            colDef = new CircleColliderDef();
            CircleColliderDef circDef = (CircleColliderDef)colDef;
            circDef.radius = Float.parseFloat(colliderData.get("radius"));

            // Box Collider //
        }else if(colType.equals("box")){
            colDef = new BoxColliderDef();
            BoxColliderDef boxDef = (BoxColliderDef)colDef;
            boxDef.width = Float.parseFloat(colliderData.get("width"));
            boxDef.height = Float.parseFloat(colliderData.get("height"));

        }else if(colType.equals("capsule")){
            colDef = new CapsuleColliderDef();
            CapsuleColliderDef capDef = (CapsuleColliderDef)colDef;
            capDef.width = Float.parseFloat(colliderData.get("width"));
            capDef.height = Float.parseFloat(colliderData.get("height"));
        }else{
            // Throw Unknown Collider Type Exception
            throw new ColliderDef.UnknownColliderTypeException(colType);
        }

        // Shared attributes among collider types
        colDef.tag = colliderData.get("tag");
        colDef.isSensor = Boolean.parseBoolean(colliderData.get("is_sensor"));
        colDef.x = Float.parseFloat(colliderData.get("position_x"));
        colDef.y = Float.parseFloat(colliderData.get("position_y"));
        return colDef;
    }

}
