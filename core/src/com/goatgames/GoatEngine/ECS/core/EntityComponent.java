package com.goatgames.goatengine.ecs.core;


import com.goatgames.goatengine.scriptingengine.nativescripts.NativeScriptComponent;
import com.goatgames.goatengine.utils.GAssert;

import java.util.Map;

public abstract class EntityComponent {


    private boolean enabled = true; //By default a component is enabled


    public EntityComponent(boolean enabled){
        this.setEnabled(enabled);
    }


    /**
     * Ctor taking a map Representation of the current component
     * @param map
     */
    public EntityComponent(Map<String, String> map){
        makeFromMap(map);
    }


    /**
     * Called when the component is attached to an entity
     */
    public void onAttach(Entity entity){}

    /**
     * Called when the component is detached from an entity
     */
    public void onDetach(Entity entity){}

    /**
     * Converts the current component to a POD Representation
     * @return a POD representation of the current component
     */
    public final Map<String, String> toMap(){
        Map<String, String> map = makeMap();
        map.put("component_id", this.getId());
        map.put("enabled", String.valueOf(this.enabled));
        return map;
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     * @return the map built
     */
    protected abstract Map<String, String> makeMap();


    /**
     * Builds the current object from a map representation
     * @param map the map representation to use
     */
    protected abstract void makeFromMap(Map<String, String>  map);

    /**
     * Builds the current object from a map representation public method
     * @param map the map representation to use
     */
    public void fromMap(Map<String, String> map){
        makeFromMap(map);
    }


    /**
     * Used to clone a component
     * @return
     */
    public abstract EntityComponent clone();


    /**
     * Returns if the component is enabled
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns if the component is disabled
     * @return
     */
    public boolean isDisabled(){
        return !enabled;
    }


    public abstract String getId();

    public static class Factory implements EntityComponentFactory {
        /**
         * Takes a data map and tries to construct a component with it
         * if the data map was incompatible with the factory
         * return null
         *
         * @param componentId
         * @param map         a map representation of a component
         * @return A Constructed Component or null if it could not be constructed
         */
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map) {
            GAssert.that(componentId.equals(NativeScriptComponent.ID),
                    "Component Factory Mismatch: NativeScriptComponent.ID != " + componentId);
            NativeScriptComponent component = new NativeScriptComponent(map);
            return component;
        }
    }
}
