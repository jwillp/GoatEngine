package com.goatgames.goatengine.scriptingengine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.common.LifespanComponent;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.graphicsrendering.camera.CameraComponent;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.physics.PhysicsSystem;
import com.goatgames.goatengine.screenmanager.IGameScreen;
import com.goatgames.goatengine.scriptingengine.common.IScriptingAPI;

/**
 * A Sample Game Specific API, contains utility function to ease some tasks
 */
public class UtilAPI implements IScriptingAPI {

    /**
     * Returns the active camera for the current game screen
     * @return the active camera, null if none found
     */
    public OrthographicCamera getCamera(){
        Array<EntityComponent> comps = GoatEngine.gameScreenManager.getCurrentScreen()
                .getEntityManager().getComponents(CameraComponent.ID);
        return (comps.size != 0) ? ((CameraComponent) comps.first()).getCamera() : null;
    }

    /**
     * Makes the camera shake
     * @param shakeStrength the strength of the shake. THe greater the value, the greater the offset
     * @param makeShakeOffset the max possible offset in world units
     * @param duration the duration of the shake in milliseconds
     */
    public void shakeCamera(float shakeStrength, float makeShakeOffset, int duration){
        OrthographicCamera cam = getCamera();
        if(GAssert.notNull(cam, "No Camera was found")){
            // cam.setShaking(true);
        }
    }

    /**
     * Makes the camera follow an entity with a lerp
     */
    public void cameraFollow(Entity entity, float speed, float delta){
        if(!GAssert.notNull(entity, "entity == null"))
            return;

        OrthographicCamera cam = getCamera();
        if (!GAssert.notNull(cam, "cam == null"))
            return;

        float lerpProgress = speed * delta;
        Vector2 entityPos = getPosition(entity);
        if (entityPos != null) {
            cam.position.x = MathUtils.lerp(cam.position.x, entityPos.x, lerpProgress);
            cam.position.y = MathUtils.lerp(cam.position.y, entityPos.y, lerpProgress);
        }
    }

    /**
     * Returns the position of an entity.
     * (Entity must have a transform component)
     * @param entity the entity of which to return the position
     * @return a Vector2 representing the position of the entity
     */
    private Vector2 getPosition(Entity entity) {
        if (!GAssert.notNull(entity, "entity == null")) return null;
        if (!GAssert.that(entity.hasComponent(TransformComponent.ID), "entity does not have TransformComponent"))
            return null;
        TransformComponent transform = (TransformComponent) entity.getComponent(TransformComponent.ID);
        return new Vector2(transform.getX(), transform.getY());
    }

    /**
     * Sets the position of an entity
     * @param entity the entity of which to set the position
     * @param x X axis value
     * @param y Y axis value
     */
    public void setPosition(Entity entity, float x, float y){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(TransformComponent.ID), "entity does not have TransformComponent"))
            return;
        TransformComponent transform = (TransformComponent) entity.getComponent(TransformComponent.ID);
        transform.setX(x);
        transform.setY(y);
    }

    /**
     * Sets the position of an entity on the X axis
     * @param entity the entity of which to set the position
     * @param x the X axis value
     */
    public void setPositionX(Entity entity, float x){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(TransformComponent.ID), "entity does not have TransformComponent"))
            return;
        TransformComponent transform = (TransformComponent) entity.getComponent(TransformComponent.ID);
        transform.setX(x);
    }

    /**
     * Sets the position of an entity on the Y axis
     * @param entity the entity of which to set the position
     * @param y the Y axis value
     */
    public void setPositionY(Entity entity, float y){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(TransformComponent.ID), "entity does not have TransformComponent"))
            return;
        TransformComponent transform = (TransformComponent) entity.getComponent(TransformComponent.ID);
        transform.setY(y);
    }

    /**
     * Returns the velocity of an entity
     * @param entity the entity of which to get the value
     * @return a Vector2 representing the velocity of the entity
     */
    public Vector2 getVelocity(Entity entity){
        if (!GAssert.notNull(entity, "entity == null")) return null;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return null;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        return comp.getVelocity();
    }

    /**
     * Sets the velocity of an entity on both axis
     * (Entity must have a physics component)
     * @param entity entity entity of which we want to set the velocity
     * @param x x value
     * @param y y value
     */
    public void setVelocity(Entity entity, float x, float y){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        comp.setVelocity(x,y);
    }

    /**
     * Sets the velocity of the entity on the X axis
     * (Entity must have a physics component)
     * @param entity entity entity of which we want to set the velocity
     * @param vx value
     */
    public void setVelocityX(Entity entity, float vx){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        comp.setVelocityX(vx);
    }

    /**
     * Sets the velocity of the entity on the Y axis
     * (Entity must have a physics component)
     * @param entity entity of which we want to set the velocity
     * @param vy value
     */
    public void setVelocityY(Entity entity, float vy){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        comp.setVelocityY(vy);
    }

    /**
     * Moves an entity on the X axis at a given speed
     * @param entity the entity to move
     * @param speed the speed at which to move the entity
     */
    public void moveEntityX(Entity entity, float speed){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        comp.setVelocity(comp.getVelocity().x + speed, comp.getVelocity().y);
    }

    /**
     * Moves an entity on the Y axis at a given speed
     * @param entity the entity to move
     * @param speed the speed at which to move the entity
     */
    public void moveEntityY(Entity entity, float speed){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        comp.setVelocity(comp.getVelocity().x, comp.getVelocity().y + speed);
    }

    /**
     * Moves an entity on the X axis at a given speed
     * @param entity the entity to move
     * @param speed the speed at which to move the entity
     * @param maxSpeed maximum speed at which the entity can go (abs value)
     */
    public void moveEntityX(Entity entity, float speed, float maxSpeed){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        float resultingVelocity = comp.getVelocity().x + speed;
        if(Math.abs(resultingVelocity) > maxSpeed){
            resultingVelocity = (resultingVelocity > 0) ? maxSpeed : -maxSpeed;
        }
        comp.setVelocity(resultingVelocity, comp.getVelocity().y);
    }

    /**
     * Moves an entity on the Y axis at a given speed
     * @param entity the entity to move
     * @param speed the speed at which to move the entity
     * @param maxSpeed maximum speed at which the entity can go
     */
    public void moveEntityY(Entity entity, float speed, float maxSpeed){
        if (!GAssert.notNull(entity, "entity == null")) return;
        if (!GAssert.that(entity.hasComponent(PhysicsComponent.ID), "entity does not have PhysicsComponent"))
            return;
        PhysicsComponent comp = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        float resultingVelocity = comp.getVelocity().y + speed;
        if(Math.abs(resultingVelocity) > maxSpeed){
            resultingVelocity = resultingVelocity > 0 ? maxSpeed : -maxSpeed;
        }
        comp.setVelocity(comp.getVelocity().x, resultingVelocity);
    }


    /**
     * Returns the game screen currently processed by the engine
     * @return current game screen
     */
    public IGameScreen getCurrentGameScreen(){
        return GoatEngine.gameScreenManager.getCurrentScreen();
    }

    /**
     * Returns the world gravity
     * @return a vector2 representing the world gravity
     */
    public Vector2 getWorldGravity(){
        PhysicsSystem physicsSystem = getCurrentGameScreen().getEntitySystemManager().getSystem(PhysicsSystem.class);
        return physicsSystem.getWorld().getGravity();
    }

    /**
     * Sets the gravity of the world
     * @param x X axis value in m/s
     * @param y Y axis value in m/s
     */
    public void setWorldGravity(float x, float y){
        PhysicsSystem physicsSystem = getCurrentGameScreen().getEntitySystemManager().getSystem(PhysicsSystem.class);
        physicsSystem.getWorld().setGravity(new Vector2(x,y));
    }

    /**
     * Sets the Gravity of the world on the X axis
     * @param x the value in m/s
     */
    public void setWorldGravityX(float x){
        setWorldGravity(x, getWorldGravity().y);
    }

    /**
     * Sets the Gravity of the world on the Y axis
     * @param y the value in m/s
     */
    public void setWorldGravityY(float y){
        setWorldGravity(getWorldGravity().x, y);
    }

    /**
     * Generates a random float number between range
     * @param min minimum value
     * @param max maximum value
     */
    public float randomFloat(float min, float max){
        return MathUtils.random(min, max);
    }

    /**
     * Generates a random float number between range
     * @param min minimum value
     * @param max maximum value
     */
    public int randomInt(int min, int max){
        return MathUtils.random(min, max);
    }

    /**
     * Generates a random boolean with a certain a certain chance of obtening true value.
     * E.g.: if change 0.6 ==> 60% chance of generating true
     * For 1/2 chance specify 0.5
     * @param chance the chance of generating true
     * @return a randomly generated boolean
     */
    public boolean randomBool(float chance){
        return MathUtils.randomBoolean(chance);
    }

    /**
     * Generates a random boolean with a 1/2 change of having either true or false
     * @return a randomly generated boolean
     */
    public boolean randomBool(){
        return randomBool(0.5f);
    }

    /**
     * Schedules the destruction of an entity
     * @param time time from now in ms. 5000 means that the entity will be destroyed in 5 secs
     */
    public void scheduleEntityDestroy(Entity entity, int time){
        entity.addComponent(new LifespanComponent(time), LifespanComponent.ID);
    }
}
