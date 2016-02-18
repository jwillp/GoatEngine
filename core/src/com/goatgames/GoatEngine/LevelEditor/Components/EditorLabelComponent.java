package com.goatgames.goatengine.leveleditor.components;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

import java.util.HashMap;
import java.util.Map;

/**
 * A label to be used by the editor to
 * visually give name to entities.
 */
public class EditorLabelComponent extends EntityComponent{


    public static final String ID = "EDITOR_LABEL_COMPONENT";
    private String label;

    public EditorLabelComponent(String label){
        super(true);
        this.label = label;
    }

    public EditorLabelComponent(Map<String, String> map) {
        super(map);
    }


    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String>  map = new HashMap<String, String>();
        map.put("label", this.label);
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
        this.label = map.get("label");
    }




    @Override
    public String getId() {
        return ID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(componentId.equals(EditorLabelComponent.ID),
                    "Component Factory Mismatch: EditorLabelComponent.ID != " + componentId);
            EditorLabelComponent component = new EditorLabelComponent(map);
            return component;
        }
    }



}
