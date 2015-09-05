package com.brm.GoatEngine.ECS.common;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brm.GoatEngine.ECS.EntityXMLFactory;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.Physics.Hitbox.Hitbox;
import com.brm.GoatEngine.Utils.Logger;

/**
 * All the physical properties of the entity so it can exist in a physical World
 * Dependencie: Box2D
 */
public class PhysicsComponent extends EntityComponent {

    public final static String ID = "PHYSICS_COMPONENT";



    //The directions an entity can face
    public enum Direction{
        LEFT,  //RIGHT
        RIGHT, //LEFT
    }

    private Body body;  //the physical body of the entity
    private Vector2 acceleration = new Vector2(0,0);   // The acceleration rate
    private final Vector2 MAX_SPEED = new Vector2(18f, 18f); //The max velocity the entity can go

    private boolean isGrounded = false; //Whether or not the entity's feet touch the ground

    private Direction direction = Direction.LEFT;

    private float width;   //The width of the entity(in game units)
    private float height;  //The height of the entity (in game units)


    /**
     * CTOR
     * @param world the world in which we want to add the body
     * @param bodyType Type of Box2D body
     * @param position the initial position
     * @param width the width
     * @param height the height
     */
    public PhysicsComponent(World world, BodyDef.BodyType bodyType, Vector2 position, float width, float height){

        this.setWidth(width);
        this.setHeight(height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position.x, position.y);

        this.body = world.createBody(bodyDef);
        isGrounded = false;
    }


    public PhysicsComponent(Element componentData, World world, Entity entity){
        this.deserialize(componentData, world, entity);
        isGrounded = false;
    }






    @Override
    public void onDetach(Entity entity) {
        super.onDetach(entity);
        this.getBody().getWorld().destroyBody(this.body);
    }



    /**
     * Returns the entity's BoundingBox
     * (The X,Y position of the entity corresponds to the bottom left of the bounding box)
     * @return
     */
    public Rectangle getBounds(){

        return new Rectangle(this.getPosition().x - this.getWidth(), this.getPosition().y-this.getHeight(), this.getWidth(), this.getHeight());
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    public void setPosition(float x, float y){
        this.body.setTransform(x,y, this.body.getAngle());
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity(){return this.body.getLinearVelocity();}



    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    public boolean isGrounded() {
        return isGrounded;
    }

    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getMaxSpeed() {
        return MAX_SPEED;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }






    /**
     * Desiralizes a component
     *
     * @param componentData the data as an XML element
     */
    @Override
    public void deserialize(Element componentData) {}



    public void deserialize(Element componentData, World world, Entity e) {

        //Params
        for(Element param: componentData.getChildrenByName("param")){
            String name = param.getAttribute("name");
            String value = param.getText();

            if(name.equals("direction")){
                this.direction = Direction.valueOf(value);
                continue;
            }
            if(name.equals("height")){
                this.height = value.equals("EDITOR_VALUE") ? EntityXMLFactory.editorProperty.height : Float.parseFloat(value);
                continue;
            }
            if(name.equals("width")){
                this.width = value.equals("EDITOR_VALUE") ? EntityXMLFactory.editorProperty.width : Float.parseFloat(value);
            }
        }


        //Body
        Element bodyEl = componentData.getChildByName("body");
        String colliderType = bodyEl.getAttribute("colliderType"); //The type of body collider
        String bodyType = bodyEl.getAttribute("type");

        //Create the base body
        //Readjust position so it is not positioned according to the middle, but rather the bottom left corner
        Vector2 pos = EntityXMLFactory.editorProperty.position;
        pos.x += this.width/2;
        pos.y += this.height/2;
        this.body.setUserData(e);

        //Add the necessary fixtures
        if(colliderType.equals("capsule")){
            this.createCapsuleFromXML(bodyEl);
        }
        if(colliderType.equals("box")){
            this.createBoxFromXML(bodyEl);
        }






    }


    /**
     * Creates a capsule body
     */
    private void createCapsuleFromXML(Element bodyEl){
        Hitbox head = null, torso = null, legs = null;
        for(Element fixture: bodyEl.getChildrenByName("fixture")){
            String fixtureName = fixture.getAttribute("name");
            Hitbox box;
            Element hitbox = fixture.getChildByName("hitbox");
            box = new Hitbox(
                    Hitbox.Type.valueOf(hitbox.getChildByName("type").getText()),
                    hitbox.getChildByName("label").getText()
            );
            if(fixtureName.equals("head")){
                head = box;
            }else if(fixtureName.equals("torso")){
                torso = box;
            }else{
                legs = box;
            }
        }

        //Colliders.createCapsule(this.body, width, height, head, torso, legs);

    }


    private void createBoxFromXML(Element bodyEl){
        Element hitboxEl = bodyEl.getChildByName("fixture").getChildByName("hitbox");
        Hitbox hitbox = new Hitbox(
                Hitbox.Type.valueOf(hitboxEl.getChildByName("type").getText()),
                hitboxEl.getChildByName("label").getText()
        );
        //Colliders.createBox(this.body, width, height, hitbox);
    }





}
