package com.goatgames.goatengine.input;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.graphicsrendering.CameraComponent;
import com.goatgames.goatengine.input.events.EntityReleasedEvent;
import com.goatgames.goatengine.input.events.EntityTouchedEvent;
import com.goatgames.goatengine.input.events.MousePressEvent;
import com.goatgames.goatengine.input.events.MouseReleasedEvent;
import com.goatgames.goatengine.utils.Logger;

/**
 * System used to process some input related events. For example clicked and dragged entities
 */
public class InputSystem extends EntitySystem implements GameEventListener{
    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        GoatEngine.eventManager.registerListener(this);
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {

    }




    @Override
    public void deInit() {
        GoatEngine.eventManager.unregisterListener(this);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof MousePressEvent){
            onMousePress((MousePressEvent) e); return;
        }
        if(e instanceof MouseReleasedEvent){
            onMouseRelease((MouseReleasedEvent) e); return;
        }
    }



    /**
     * When a mouse press event is captured, try to find an entity
     * under the mouse pointer
     * @param e the event
     */
    private void onMousePress(MousePressEvent e){
        String entityId = findEntityUnderMousePos(e.screenX, e.screenY);
        if(entityId != null) {
            Entity entity  = getEntityManager().getEntityObject(entityId);
            if(entity.hasComponentEnabled(TouchableComponent.ID)){
                TouchableComponent touchableComponent = (TouchableComponent) entity.getComponent(TouchableComponent.ID);
                touchableComponent.setTouched(true);
                fireEvent(new EntityTouchedEvent(entityId));
                Logger.debug("Entity Selection yeah!");
            }
            getEntityManager().freeEntity(entity);
        }
    }


    /**
     * Called when the mouse is released
     * @param e the event
     */
    private void onMouseRelease(MouseReleasedEvent e){
        String entityId = findEntityUnderMousePos(e.screenX, e.screenY);
        if(entityId != null) {
            Entity entity  = getEntityManager().getEntityObject(entityId);
            if(entity.hasComponentEnabled(TouchableComponent.ID)){
                TouchableComponent touchableComponent = (TouchableComponent) entity.getComponent(TouchableComponent.ID);
                touchableComponent.setTouched(false);
                fireEvent(new EntityReleasedEvent(entityId));
            }
            getEntityManager().freeEntity(entity);
        }
    }


    /**
     * Finds the entity under mouse pos
     * @param x of mouse
     * @param y of mouse
     */
    private String findEntityUnderMousePos(float x, float y){
        // Get the Camera
        CameraComponent cam = (CameraComponent) getEntityManager().getComponents(CameraComponent.ID).get(0);

        // Translate the mouse coordinates to world coordinates
        final Vector3 pos = new Vector3();
        cam.getCamera().unproject(pos.set(x, y, 0));

        // Ask the world wichi body is within the mouse pointer's bouding box
        World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
        final Body[] hitBody = {null};
        float mousePointerSize =  0.0001f;
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if(fixture != null) {
                    hitBody[0] = fixture.getBody();
                    return true;
                }
                return false;
            }
        }, pos.x - mousePointerSize, pos.y - mousePointerSize, pos.x + mousePointerSize, pos.y + mousePointerSize);
        // If we hit something
        String entityId = null;
        if (hitBody[0] != null) entityId = (String) hitBody[0].getUserData();

        return entityId;
    }






}
