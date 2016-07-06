package com.goatgames.goatengine.ecs.core;

/**
 * A Dynamic component as opposed to a static one corresponds to a component where each field is
 * dynamically added to the component at runtime instead of compile time. This are useful
 * for defining new components using scripts or configuration files like prefabs.
 */
public abstract class DynamicComponent extends EntityComponent{

    public static final String ID = "DYNAMIC_COMPONENT";

    public DynamicComponent(NormalisedEntityComponent data) {
        super(data);
    }

    public DynamicComponent(boolean enabled){
        super(enabled);
    }

    @Override
    public String getId() {
        return ID;
    }
}
