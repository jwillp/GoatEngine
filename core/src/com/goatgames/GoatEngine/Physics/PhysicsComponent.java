package com.goatgames.goatengine.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.goatgames.goatengine.ecs.JsonSerializer;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * All the physical properties of the entity so it can exist in a physical World
 * Dependency: Box2D
 */
public class PhysicsComponent extends EntityComponent {

    public final static String ID = "PHYSICS_COMPONENT";

    // Variable used in order not to calculate the size all the time. size caching
    private boolean dirtyWidth = true;

    private boolean dirtyHeight = true;
    private float width;
    private float height;

    // Body definition
    private PhysicsBodyDef bodyDef;


    private Body body;  //the physical body of the entity



    public PhysicsComponent(NormalisedEntityComponent data){ super(data); }

    /**
     * CTOR
     * @param bodyType Type of Box2D body
     * @param position the initial position
     * @param colliderDefs the potential collider definitions
     */
    public PhysicsComponent(BodyDef.BodyType bodyType, Vector2 position, Array<ColliderDef> colliderDefs){
        super(true);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position.x, position.y);
        this.bodyDef.setColliderDefs(colliderDefs);
    }

    public PhysicsComponent(PhysicsBodyDef bodyDef){
        super(true);
        this.bodyDef = bodyDef;
    }

    @Override
    public void onAttach(Entity entity){

    }

    @Override
    public void onDetach(Entity entity) {
        super.onDetach(entity);
        this.getBody().getWorld().destroyBody(this.body);
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent physMap = super.normalise();
        physMap.put("body_type", String.valueOf(this.body.getType()));
        physMap.put("position_x", String.valueOf(this.getPosition().x));
        physMap.put("position_y", String.valueOf(this.getPosition().y));
        physMap.put("fixed_rotation", String.valueOf(body.isFixedRotation()));

        // Convert colliders to a Json string and save it such in the map
        JsonArray jsonArray = new JsonArray();
        for(Collider c: this.getColliders()){
            jsonArray.add(JsonSerializer.mapToJson(c.normalise()));
        }
        physMap.put("colliders", jsonArray.toString());
        return physMap;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);

        // Force size update
        setDirty(true);

        // Create Body definition
        bodyDef = new PhysicsBodyDef();
        bodyDef.type = BodyDef.BodyType.valueOf(data.getOrDefault("body_type", "StaticBody"));
        bodyDef.position.set(Float.parseFloat(data.get("position_x")), Float.parseFloat(data.get("position_y")));
        bodyDef.fixedRotation = Boolean.parseBoolean(data.getOrDefault("fixed_rotation", "true"));
        bodyDef.allowSleep = Boolean.parseBoolean(data.getOrDefault("allow_sleep", "false"));

        // Create collider definitions
        String collidersStr = data.get("colliders");
        if(collidersStr != null && !collidersStr.isEmpty()) {
            JsonArray jsColliders = Json.parse(collidersStr).asArray();
            for (JsonValue v : jsColliders.values()) {
                //String ok = v.asString();
                JsonObject jsCollider = v.asObject();
                Map<String, String> colMap = new HashMap<>();
                //Create a map for that collider
                for (JsonObject.Member member : jsCollider) {
                    colMap.put(member.getName(), member.getValue().asString());
                }
                ColliderDef def = Collider.defFromMap(colMap);
                bodyDef.addColliderDef(def);
            }
        }
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
        return new Rectangle(getPosition().x - getWidth(), getPosition().y - getHeight(), getWidth(), getHeight());
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    public void setPosition(float x, float y){
        this.body.setTransform(x,y, this.body.getAngle());
    }

    /**
     * Returns the width of the entity (in game units)
     * @return the width
     */
    public float getWidth() {
        // Return last calculation
        if(!dirtyWidth)
            return width;

        float furthestX = -0.0f;
        Array<Fixture> fixtureArray = body.getFixtureList();
        for(int i=0; i<fixtureArray.size; i++){
           Fixture fixture = fixtureArray.get(i);
            if(fixture.getShape() instanceof PolygonShape){
                PolygonShape shape = (PolygonShape) fixture.getShape();
                Vector2 pos = new Vector2();
                shape.getVertex(1, pos);
                if(furthestX < pos.x) furthestX = pos.x;
            }else{
                CircleShape shape = (CircleShape) fixture.getShape();
                float reach = shape.getPosition().x + shape.getRadius();
                if(reach > furthestX) furthestX = reach;
            }

        }
        dirtyWidth = false;
        width = Math.abs(furthestX);
        return width;
    }


    /**
     * Returns the height of the entity (in game units)
     * @return the height
     */
    public float getHeight() {
        // Return last calculation
        if(!dirtyHeight)
            return height;

        float furthestY = -750.0f; // why 750? and not float_MAX
        Array<Fixture> fixtureArray = body.getFixtureList();
        for(int i=0; i<fixtureArray.size; i++){
            Fixture fixture = fixtureArray.get(i);
            if(fixture.getShape() instanceof PolygonShape){
                PolygonShape shape = (PolygonShape) fixture.getShape();
                Vector2 pos = new Vector2();
                shape.getVertex(1, pos);
                if(furthestY < pos.y) furthestY = pos.y;
            }else{
                CircleShape shape = (CircleShape) fixture.getShape();
                float reach = shape.getPosition().y + shape.getRadius();
                if(reach > furthestY) furthestY = reach;
            }
        }
        dirtyHeight = false;
        height = Math.abs(furthestY);
        return height;
    }

    /**
     * Returns the body
     * @return
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets the body
     * @param body
     */
    public void setBody(Body body, String entityId){
        this.body = body;
        this.body.setUserData(entityId);
    }

    /**
     * Sets the velocity of the body
     * @param x velocity in X
     * @param y velocity in Y
     */
    public void setVelocity(float x, float y) {
        this.body.setLinearVelocity(x,y);
    }

    /**
     * Sets the X velocity
     * @param x
     */
    public void setVelocityX(float x){
        setVelocity(x,body.getLinearVelocity().y);
    }

    /**
     * Sets the Y velocity
     * @param y
     */
    public void setVelocityY(float y){
        setVelocity(body.getLinearVelocity().x,y);
    }

    public Vector2 getVelocity(){return this.body.getLinearVelocity();}

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

    /**
     * Forces the size to be calculated again
     * @param dirty
     */
    public void setDirty(boolean dirty){
        this.dirtyHeight = dirty;
        this.dirtyWidth = dirty;
    }

    /**
     * Used to clone a component
     *
     * @return
     */
    @Override
    public EntityComponent clone() {
        return new PhysicsComponent(normalise());
    }

    /**
     * Returns this physics Component body def.
     * @return
     */
    public PhysicsBodyDef getBodyDef() {
        return bodyDef;
    }

}
