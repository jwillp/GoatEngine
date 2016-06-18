package com.goatgames.goatengine.input;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * Makes an entity touchable. The input system will fire event
 * when the entity is touched
 */
public class TouchableComponent extends EntityComponent {

    public final static String ID = "TOUCHABLE_COMPONENT";
    private boolean touched = false;

    public TouchableComponent(NormalisedEntityComponent data) {
        super(data);
    }

    @Override
    public EntityComponent clone() {
        return new TouchableComponent(normalise());
    }

    @Override
    public String getId() {
        return ID;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}
