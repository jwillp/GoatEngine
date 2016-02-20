package com.goatgames.goatengine.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.JsonSerializer;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

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

    // Variable used in order not to calculate the size all the time
    private boolean dirtyWidth = true;
    private boolean dirtyHeight = true;
    private float width;
    private float height;






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
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position.x, position.y);
        this.body = world.createBody(bodyDef);
        body.setSleepingAllowed(false);
    }



    @Override
    public void onAttach(Entity entity){
        this.body.setUserData(entity.getID());
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
        physMap.put("fixed_rotation", String.valueOf(body.isFixedRotation()));

        // Convert colliders to a Json string and save it such in the map
        JsonArray jsonArray = new JsonArray();
        for(Collider c: this.getColliders()){
            jsonArray.add(JsonSerializer.mapToJson(c.toMap()));
        }
        physMap.put("colliders", jsonArray.toString());
        return physMap;
    }

    /**
     * Builds the current object from a Map representation
     * @param map the Map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
        setDirty(true);
        // Create Body
        World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.valueOf(map.get("body_type"));
        bodyDef.position.set(Float.parseFloat(map.get("position_x")), Float.parseFloat(map.get("position_y")));

        this.body = world.createBody(bodyDef);

        this.setBodyType(BodyDef.BodyType.valueOf(map.get("body_type")));
        body.setSleepingAllowed(false);

        if(map.containsKey("fixed_rotation")){
            body.setFixedRotation(Boolean.parseBoolean(map.get("fixed_rotation")));
        }


        // Load colliders
        String collidersStr = map.get("colliders");
        if(collidersStr != null && !collidersStr.isEmpty()){
            JsonArray jsColliders = Json.parse(collidersStr).asArray();
            for(JsonValue v: jsColliders.values()){
                //String ok = v.asString();
                JsonObject jsCollider = v.asObject();
                Map<String, String> colMap = new HashMap<>();
                //Create a map for that collider
                for(JsonObject.Member member: jsCollider){
                    colMap.put(member.getName(), member.getValue().asString());
                }
                ColliderDef def = Collider.defFromMap(colMap);
                Collider.addCollider(this,def);
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


    /**
     * Returns the width of the entity (in game units)
     * @return
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
     * @return
     */
    public float getHeight() {
        // Return last calculation
        if(!dirtyHeight)
            return height;

        float furthestY = -750.0f;
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



    public void setDirty(boolean dirty){
        this.dirtyHeight = dirty;
        this.dirtyWidth = dirty;
    }


    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(componentId.equals(PhysicsComponent.ID),
                    "Component Factory Mismatch: PhysicsComponent.ID != " + componentId);
            PhysicsComponent component = new PhysicsComponent(map);
            return component;
        }
    }


}
