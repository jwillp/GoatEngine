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


    class EditorLabelComponentPOD extends EntityComponentPOD{
        public String label;
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected EntityComponentPOD makePOD() {
        EditorLabelComponentPOD pod = new EditorLabelComponentPOD();
        pod.label = this.label;
        return pod;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromPOD(EntityComponentPOD pod) {
        EditorLabelComponentPOD editPOD = (EditorLabelComponentPOD)pod;
        this.label = editPOD.label;
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
