package com.goatgames.goatengine.ecs.io;

import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.common.*;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.GameComponent;
import com.goatgames.goatengine.ecs.core.LuaGameComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.graphicsrendering.*;
import com.goatgames.goatengine.graphicsrendering.camera.CameraComponent;
import com.goatgames.goatengine.input.TouchableComponent;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.scriptingengine.nativescripts.NativeScriptComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Maps component map representation to component classes for easy deserialisation
 */
public class ComponentMapper {

    /**
     * The linked classes
     */
    private static ObjectMap<String, Class> classes = new ObjectMap<>();

    /**
     * Statically link classes
     */
    static {
        linkClass(TagsComponent.ID, TagsComponent.class);
        linkClass(CameraComponent.ID, CameraComponent.class);
        linkClass(LightComponent.ID, LightComponent.class);
        linkClass(ParticleEmitterComponent.ID, ParticleEmitterComponent.class);
        linkClass(SpriteComponent.ID, SpriteComponent.class);
        linkClass(SpriterAnimationComponent.ID, SpriterAnimationComponent.class);
        linkClass(ZIndexComponent.ID, ZIndexComponent.class);
        linkClass(TouchableComponent.ID, TouchableComponent.class);
        linkClass(PhysicsComponent.ID, PhysicsComponent.class);
        linkClass(TransformComponent.ID, TransformComponent.class);
        linkClass(NativeScriptComponent.ID, NativeScriptComponent.class);
        linkClass(LabelComponent.ID, LabelComponent.class);
        linkClass(EditorComponent.ID, EditorComponent.class);
        linkClass(VariantComponent.ID, VariantComponent.class);
    }

    /**
     * Links a component ID to a class
     *
     * @param componentId the ID of the component
     * @param componentClass the class of the Component
     */
    public static <T extends EntityComponent> void linkClass(final String componentId, Class<T> componentClass) {
        GAssert.that(!componentId.isEmpty(), "Invalid Component Id for class: " + componentClass.getCanonicalName());
        GAssert.notNull(classes, "classes == null");
        if(classes.containsKey(componentId)){
            System.out.println(
                    String.format("[WARNING]: Component : %s is already mapped with class %s therefore it cannot be mapped to %s",
                    componentId,
                    classes.get(componentId).getSimpleName(),
                    componentClass.getSimpleName()
            ));
            return;
        }
        classes.put(componentId, componentClass);
    }

    /**
     * Denormalises an entity component by using reflection.
     * Returns a component instance from a normalised component.
     *
     * @param data normalised dta
     * @return a component instance or null if none could be created
     */
    public static EntityComponent getComponent(NormalisedEntityComponent data) {
        if (GAssert.notNull(data, "data == null")) {
            String compId = data.get("component_id");
            try {
                Class<?> componentClass;
                if (classes.containsKey(compId)) {
                    componentClass = classes.get(compId);
                } else {
                    // TODO Remove that LuaGameComponent thing, this is game specific
                    componentClass = data.containsKey(LuaGameComponent.INTERNAL_KEY) ? LuaGameComponent.class : GameComponent.class;
                    // componentClass = GameComponent.class; --> Maybe MetadataComponent (?)
                }
                Constructor<?> constructor;
                constructor = componentClass.getConstructor(NormalisedEntityComponent.class);
                return (EntityComponent) constructor.newInstance(data);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                GoatEngine.logger.error(e.getMessage());
                GoatEngine.logger.error(e);
            } catch (Exception e){
                GoatEngine.logger.error("There was an error loading component.");
                GoatEngine.logger.error(e.getMessage());
                GoatEngine.logger.error(e.getStackTrace());
            }
        }
        return null;
    }
}
