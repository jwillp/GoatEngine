package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * Used to tag entities with a certain persistent identifier.
 * For example when loading from a tiled map, the tile map loader will automatically give
 * the entity a label based on the id and the name of the object in the editor.
 * This is mainly used for debugging.
 * This is different from the Id of an entity, as the id is for internal use and can change from
 * one execution of the engine to another.
 * The label is for human use to distinguish entities.
 */
public class LabelComponent extends EntityComponent {

    public final static String ID = "LABEL_COMPONENT";

    public static final String UNLABELED = "Unlabeled";

    // Member variables
    /**
     * The label of the entity
     */
    private String label;

    public LabelComponent(NormalisedEntityComponent data){
        super(data);
    }

    public LabelComponent(String label) {
        super(true);
        this.label = label;
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();

        data.put("label", getLabel());

        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);

        setLabel(data.getOrDefault("label", UNLABELED));
    }

    @Override
    public String getId() {
        return ID;
    }

    /**
     * The label of the entity
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if(label == null) label = UNLABELED;
        this.label = label;
    }
}

