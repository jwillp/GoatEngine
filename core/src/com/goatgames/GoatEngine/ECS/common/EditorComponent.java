package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * Represents information about an entity that was entered in the editor
 * This is  can be used to create entities using custom rules according to
 * the info entered in the editor
 */
public class EditorComponent extends VariantComponent {

    public final static String ID = "EDITOR_COMPONENT";

    public EditorComponent(NormalisedEntityComponent data){
        super(data);
    }

    @Override
    public String getId() {
        return ID;
    }
}

