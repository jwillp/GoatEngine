package com.goatgames.goatengine.input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.goatgames.gdk.eventdispatcher.IEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.goatengine.graphicsrendering.ZIndexComponent;
import com.goatgames.goatengine.input.events.EntityReleasedEvent;
import com.goatgames.goatengine.input.events.EntityTouchedEvent;
import com.goatgames.goatengine.input.events.InputClickPressEvent;
import com.goatgames.goatengine.input.events.InputClickReleaseEvent;
import com.goatgames.goatengine.physics.PhysicsSystem;
import com.goatgames.goatengine.scriptingengine.UtilAPI;

/**
 * System used to process some input related events. For example clicked and dragged entities
 */
public class InputSystem extends EntitySystem implements IEventListener {
    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        GoatEngine.eventManager.register(this);
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
        GoatEngine.eventManager.register(this);
    }

    @Override
    public boolean onEvent(Event e) {
        if(e instanceof InputClickPressEvent){
            onInputClickPress((InputClickPressEvent) e); return false;
        }
        if(e instanceof InputClickReleaseEvent){
            onInputClickRelease((InputClickReleaseEvent) e); return false;
        }

        return false;
    }



    /**
     * When a mouse press event is captured, try to find an entity
     * under the mouse pointer
     * @param e the event
     */
    private void onInputClickPress(InputClickPressEvent e){
        String entityId = findForeMostEntity(findEntitiesFromCamPOV(e.screenX, e.screenY));
        if(entityId != null) {
            Entity entity  = getEntityManager().getEntityObject(entityId);
            if(entity.hasComponentEnabled(TouchableComponent.ID)){
                TouchableComponent touchableComponent = (TouchableComponent) entity.getComponent(TouchableComponent.ID);
                touchableComponent.setTouched(true);
                fireEvent(new EntityTouchedEvent(entityId));
                GoatEngine.logger.debug("Entity Selection yeah!");
            }
            getEntityManager().freeEntityObject(entity);
        }
    }


    /**
     * Called when the mouse is released
     * @param e the event
     */
    private void onInputClickRelease(InputClickReleaseEvent e){
        String entityId = findForeMostEntity(findEntitiesFromCamPOV(e.screenX, e.screenY));
        if(entityId != null) {
            Entity entity  = getEntityManager().getEntityObject(entityId);
            if(entity.hasComponentEnabled(TouchableComponent.ID)){
                TouchableComponent touchableComponent = (TouchableComponent) entity.getComponent(TouchableComponent.ID);
                touchableComponent.setTouched(false);
                fireEvent(new EntityReleasedEvent(entityId));
            }
            getEntityManager().freeEntityObject(entity);
        }
    }

    /**
     * Finds entities at positon according to the camera's point of view using
     * physics Bodies. Translates a camera's coordinate point to a world coordinate.
     * @param screenX of mouse
     * @param screenY of mouse
     */
    private Array<String> findEntitiesFromCamPOV(float screenX, float screenY){
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();

        OrthographicCamera cam = new UtilAPI().getCamera();
        if(cam == null) return new Array<>();

        final Vector3 pos = new Vector3();
        // Translate the mouse coordinates to world coordinates
        cam.unproject(pos.set(screenX, screenY, 0));

        // Ask the world which bodies are within the given
        // Bounding box around the mouse pointer
        World world = GoatEngine.gameScreenManager.getCurrentScreen()
                .getEntitySystemManager()
                .getSystem(PhysicsSystem.class)
                .getWorld();
        final Array<Body> hitBodies = new Array<>();
        float mousePointerSize =  0.0001f;
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if(fixture != null) {
                    hitBodies.add(fixture.getBody());
                    return true;
                }
                return false;
            }
        }, pos.x - mousePointerSize, pos.y - mousePointerSize, pos.x + mousePointerSize, pos.y + mousePointerSize);
        // If we hit something
        Array<String> entityIds = new Array<>(hitBodies.size);
        for(Body b: hitBodies){
            if(b.getUserData() != null){
                entityIds.add((String) b.getUserData());
            }
        }
        return entityIds;
    }

    /**
     * Returns the fore most entity
     * @param entityIds
     * @return
     */
    private String findForeMostEntity(Array<String> entityIds){
        if(entityIds.size != 0) {
            // Get an array of entities
            Array<Entity> noZEntities = new Array<>();
            Array<Entity> ZEntities = new Array<>();
            // Separate Z Indexed entities and non Z indexed
            for (String s : entityIds) {
                // Make sure it is registered
                Entity entity = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().getEntityObject(s);
                if (entity.hasComponent(ZIndexComponent.ID)) {
                    /*ZIndexComponent zc = (ZIndexComponent) entity.getComponent(ZIndexComponent.ID);
                    if(zc.index >= editor.getMinZ()) {
                        ZEntities.add(entity);
                    }*/
                    ZEntities.add(entity);
                } else {
                    noZEntities.add(entity);
                }
            }

            // First Sort entities By ZIndex.
            Sort.instance().sort(ZEntities, new ZIndexComponent.ZIndexComparator());
            ZEntities.reverse();
            // If they do not have a Z index place them at the front of the list
            if (noZEntities.size != 0) {
                for (Entity e : noZEntities) {
                    ZEntities.insert(0, e);
                }
            }
            // The top One is the one we want
            if (ZEntities.size != 0) return ZEntities.first().getId();
        }
        return null;
    }
}
