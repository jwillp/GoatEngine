package com.goatgames.goatengine.input;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

import java.util.HashMap;
import java.util.Map;

/**
 * Makes an entity touchable. The input system will fire event
 * when the entity is touched
 */
public class TouchableComponent extends EntityComponent {

    public final static String ID = "TOUCHABLE_COMPONENT";
    private boolean touched = false;

    public TouchableComponent(EntityComponentMap componentData) {
        super(componentData);
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return the map built
     */
    @Override
    protected Map<String, String> makeMap() {
        return new HashMap<String, String>();
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {

    }

    @Override
    public String getId() {
        return ID;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }


    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(componentId.equals(TouchableComponent.ID),
                    "Component Factory Mismatch: TouchableComponent.ID != " + componentId);
            TouchableComponent component = new TouchableComponent(map);
            return component;
        }
    }


}
