package com.goatgames.goatengine.ecs;

import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.ecs.common.TagsComponent;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.ecs.core.EntityComponentMap;
import com.goatgames.goatengine.ecs.core.GameComponent;
import com.goatgames.goatengine.graphicsrendering.*;
import com.goatgames.goatengine.graphicsrendering.camera.CameraComponent;
import com.goatgames.goatengine.input.TouchableComponent;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;
import com.goatgames.goatengine.scriptingengine.nativescripts.NativeScriptComponent;
import com.goatgames.goatengine.utils.GAssert;

/**
 * Maps component map representation to component instances
 */
public class ComponentMapper {

    /**
     * The linked factories Map<ComponentId, Factory>
     */
    private static ObjectMap<String,EntityComponentFactory> factories = new ObjectMap<>();

    static {

        linkFactory(TagsComponent.ID, new TagsComponent.Factory());
        linkFactory(CameraComponent.ID, new CameraComponent.Factory());
        linkFactory(LightComponent.ID, new LightComponent.Factory());
        linkFactory(ParticleEmitterComponent.ID, new ParticleEmitterComponent.Factory());
        linkFactory(SpriteComponent.ID, new SpriteComponent.Factory());
        linkFactory(SpriterAnimationComponent.ID, new SpriterAnimationComponent.Factory());
        linkFactory(ZIndexComponent.ID, new ZIndexComponent.Factory());
        linkFactory(TouchableComponent.ID, new TouchableComponent.Factory());
        linkFactory(PhysicsComponent.ID, new PhysicsComponent.Factory());
        linkFactory(ScriptComponent.ID, new ScriptComponent.Factory());
        linkFactory(TransformComponent.ID, new TransformComponent.Factory());
        linkFactory(NativeScriptComponent.ID, new NativeScriptComponent.Factory());
    }

    /**
     * Links a factory to a Component Id
     * @param componentName
     * @param factory
     */
    private static void linkFactory(final String componentName, final EntityComponentFactory factory){
        factories.put(componentName, factory);
    }



    public static EntityComponent getComponent(EntityComponentMap map){
        String compId = map.get("component_id");
        // If the component has no linked factory it is a GameComponent (most probably)
        GAssert.that(factories.containsKey(compId),
                String.format("Component Mapper: No Factory for the specified Component: %s", compId));
        factories.get(compId);
        EntityComponentFactory f = factories.containsKey(compId) ? factories.get(compId) : new GameComponent.Factory();
        return f.processMapData(compId, map);
    }









}
