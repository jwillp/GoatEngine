package com.goatgames.goatengine.screenmanager;

import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.gdk.eventdispatcher.EventDispatcher;
import com.goatgames.gdk.eventdispatcher.IEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.common.EntityDestructionSystem;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.ecs.core.EntitySystemManager;
import com.goatgames.goatengine.graphicsrendering.RenderingSystem;
import com.goatgames.goatengine.input.InputSystem;
import com.goatgames.goatengine.physics.PhysicsSystem;
import com.goatgames.goatengine.scriptingengine.EntityScriptSystem;
import com.goatgames.goatengine.scriptingengine.ScriptSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of an GameScreen making use
 * of the Entity Component System
 */
public abstract class ECSGameScreen implements IGameScreen, IEventListener {

    /**
     * Used for handling systems
     */
    private EntitySystemManager systemManager;

    /**
     * Used to handle entities
     */
    private EntityManager entityManager;

    /**
     * To be able to dispatch event, in the current scope of this screen.
     * That if a system from this current screen fires an Event the systems
     * of another screen wont be able to capture them
     */
    private EventDispatcher eventDispatcher;

    public ECSGameScreen(){
        eventDispatcher = new EventDispatcher();
        entityManager = new EntityManager();
        systemManager = new EntitySystemManager(entityManager, eventDispatcher);
    }

    /**
     * Used to specify the different systems needed for the screen to work.
     * This should be overridden by child classes to define their own set of systems
     * to be used by the system manager
     */
    protected List<EntitySystem> getInitialSystems(){
        List<EntitySystem> systems = new ArrayList<>(10);
        systems.add(new InputSystem());
        systems.add(new EntityScriptSystem());
        systems.add(new ScriptSystem());
        systems.add(new EntityDestructionSystem());
        systems.add(new PhysicsSystem());
        systems.add(new RenderingSystem());

        return systems;
    }

    /**
     * Loads the screen configuration
     */
    public abstract void loadConfig();

    @Override
    public void init(GameScreenManager screenManager) {

        // Load screen configuration
        loadConfig();

        // Add Initial Systems
        for(EntitySystem system : getInitialSystems()) {
            systemManager.addSystem(system);
        }

        systemManager.initSystems();

        GoatEngine.eventManager.register(this);

    }

    @Override
    public void cleanUp() {
        GoatEngine.eventManager.unregister(this);
        eventDispatcher.clear();
        entityManager.clear();
        systemManager.clear();
    }

    @Override
    public void pause(GameScreenManager screenManager) {
        GoatEngine.eventManager.unregister(this);
    }

    @Override
    public void resume(GameScreenManager screenManager) {
        GoatEngine.eventManager.register(this);
    }

    @Override
    public void preUpdate(GameScreenManager screenManager) {
        systemManager.preUpdate();
    }

    @Override
    public void draw(GameScreenManager screenManager, float deltaTime) {
        systemManager.draw();
    }

    @Override
    public void update(GameScreenManager screenManager, float deltaTime) {
        systemManager.update(deltaTime);
    }

    @Override
    public boolean onEvent(Event e) {
        // Redispatch Engine Events to the screens dispatcher
        this.eventDispatcher.fireEvent(e);
        return false;
    }

    public EntitySystemManager getSystemManager() {
        return systemManager;
    }

    public void setSystemManager(EntitySystemManager systemManager) {
        this.systemManager = systemManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }
}
