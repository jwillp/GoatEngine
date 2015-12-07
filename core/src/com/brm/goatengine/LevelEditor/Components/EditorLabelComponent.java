package com.brm.GoatEngine.LevelEditor.Components;

import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.core.EntityComponent;

/**
 * A label to be used by the editor to
 * visually give name to entities.
 */
public class EditorLabelComponent extends EntityComponent{


    public static final String ID = "EDITOR_LABEL_COMPONENT";
    private String label;

    public EditorLabelComponent(String label) {
        this.label = label;
    }


    /**
     * Desiralizes a component
     *
     * @param componentData the data as an XML element
     */
    public void deserialize(XmlReader.Element componentData) {

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
}
