package com.goatgames.goatengine.ecs.core;

/**
 * Entity Component class
 */
public abstract class EntityComponent {

    /**
     * By default a component is enabled
     */
    private boolean enabled = true;

    /**
     * Constructor accepting a booleato indicate if the component
     * is flagged as enabled ornot
     * @param enabled
     */
    public EntityComponent(boolean enabled){
        this.setEnabled(enabled);
    }

    /**
     * Constructor taking a normalised entity component of the current component
     * @param normalisedComponent the normalised component data
     */
    public EntityComponent(NormalisedEntityComponent normalisedComponent){
        denormalise(normalisedComponent);
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
     * Constructs a normalised entity component
     * Sub classes should override this method (and use super)
     * to specify additional data to be normalised
     * @return the normalised entity component
     */
    public NormalisedEntityComponent normalise(){
        NormalisedEntityComponent data = new NormalisedEntityComponent();
        data.put("component_id", this.getId());
        data.put("enabled", String.valueOf(this.enabled));
        return data;
    }

    /**
     * initialises the current component instance
     * from the data of a normalised component representation
     * @param data  normalised component
     */
    public void denormalise(NormalisedEntityComponent data){
        this.enabled = Boolean.parseBoolean(data.getOrDefault("enabled", "true"));
    }

    /**
     * Indicates whether or not the component is enabled
     * @return true if enabled, otherwise false
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the component as being enabled or not
     * @param enabled whether or not the component should be enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Indicates  whether the component is disabled or not
     * @return true if disabled, otherwise false
     */
    public boolean isDisabled(){
        return !enabled;
    }

    /**
     * Returns the ID of the current component
     * @return String representing the ID of the current component
     */
    public abstract String getId();
}
