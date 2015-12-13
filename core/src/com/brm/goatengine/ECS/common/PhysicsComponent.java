package com.brm.GoatEngine.ECS.common;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Physics.Collider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * All the physical properties of the entity so it can exist in a physical World
 * Dependencie: Box2D
 */
public class PhysicsComponent extends EntityComponent {

    public final static String ID = "PHYSICS_COMPONENT";

    private Body body;  //the physical body of the entity

    // TODO instead calculate from fixture sizes
    private float width;   //The width of the entity(in game units)
    private float height;  //The height of the entity (in game units)

    public PhysicsComponent(Map<String, String> map){ super(map); }

    /**
     * CTOR
     * @param world the world in which we want to add the body
     * @param bodyType Type of Box2D body
     * @param position the initial position
     * @param width the width
     * @param height the height
     */
    public PhysicsComponent(World world, BodyDef.BodyType bodyType, Vector2 position, float width, float height){
        super(true);
        this.setWidth(width);
        this.setHeight(height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position.x, position.y);

        this.body = world.createBody(bodyDef);
    }



    @Override
    public void onAttach(Entity entity){
        this.body.setUserData(entity);
    }

    @Override
    public void onDetach(Entity entity) {
        super.onDetach(entity);
        this.getBody().getWorld().destroyBody(this.body);
    }

    /**
     * Constructs a MapType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> physMap = new HashMap<String, String>();
        physMap.put("body_type", String.valueOf(this.body.getType()));
        physMap.put("position_x", String.valueOf(this.getPosition().x));
        physMap.put("position_y", String.valueOf(this.getPosition().y));
        return physMap;
    }

    /**
     * Builds the current object from a Map representation
     *
     * @param map the Map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
        // Create Body
        World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.valueOf(map.get("body_type"));
        bodyDef.position.set(Float.parseFloat(map.get("position_x")), Float.parseFloat(map.get("position_y")));

        this.body = world.createBody(bodyDef);

        this.setBodyType(BodyDef.BodyType.valueOf(map.get("body_type")));
    }

    /**
     * Returns a list of colliders for the current body
     * @return list of colliders
     */
    public ArrayList<Collider> getColliders(){
        ArrayList<Collider> colliders = new ArrayList<Collider>();
        Array<Fixture> fixtureArray = body.getFixtureList();
        for(int i=0; i<fixtureArray.size; i++){
            colliders.add((Collider) fixtureArray.get(i).getUserData());
        }
        return colliders;
    }

    /**
     * Returns the entity's BoundingBox
     * (The X,Y position of the entity corresponds to the bottom left of the bounding box)
     * @return
     */
    public Rectangle getBounds(){
        return new Rectangle(this.getPosition().x - this.getWidth(),
                this.getPosition().y-this.getHeight(),
                this.getWidth(),
                this.getHeight());
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    public void setPosition(float x, float y){
        this.body.setTransform(x,y, this.body.getAngle());
    }

    public Vector2 getVelocity(){return this.body.getLinearVelocity();}

    // TODO instead calculate from fixture sizes
    public float getWidth() {

        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    // TODO instead calculate from fixture sizes
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Body getBody() {
        return body;
    }

    public void setVelocity(float x, float y) {
        this.body.setLinearVelocity(x,y);
    }

    public void setBodyType(BodyDef.BodyType type) {
        if(body.getType() != type)
            body.setType(type);
    }

    public void setAngle(float angle){
        this.body.setTransform(getPosition(), angle);
    }

    public float getAngle(){
        return body.getAngle();
    }


    @Override
    public String getId() {
        return ID;
    }
}
