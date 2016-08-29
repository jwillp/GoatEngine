package com.goatgames.goatengine.ecs.common;

import com.goatgames.gdk.VariantMap;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * Represents custom information about an entity.
 * This is mostly used for script inter communication, where some data needs to be easily
 * passed temporarily.
 */
public class VariantComponent extends EntityComponent {

    public final static String ID = "VARIANT_COMPONENT";


    // Member variables

    /**
     * Represents the data
     */
    private VariantMap data = new VariantMap();


    public VariantComponent(NormalisedEntityComponent data){
        super(data);
    }

    public VariantComponent() {
        super(true);
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.putAll(this.data.getValues());
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        this.data = new VariantMap();
        this.data.setValues(data);
    }


    public void set(String name, int value){
        data.set(name, value);
    }

    public void set(String name, float value){
        data.set(name, value);
    }

    public void set(String name, boolean value){
        data.set(name, value);
    }

    public void set(String name, String value){
        data.set(name, value);
    }

    public void set(String name, VariantMap.Variant value){
        data.set(name, value);
    }

    @Override
    public String getId() {
        return ID;
    }

    /**
     * Returns the variant map of the component
     *
     * @return VariantMap
     */
    public VariantMap getData(){
        return data;
    }

    public VariantMap.Variant get(String name) {
        return data.get(name);
    }

    public boolean hasValue(String name) {
        return data.hasValue(name);
    }
}

