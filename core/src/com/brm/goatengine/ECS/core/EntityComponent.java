package com.brm.GoatEngine.ECS.core;


public abstract class EntityComponent {


    private static final String ID = "ENTITY_COMPONENT";
    private boolean enabled = true; //By default a component is enabled


    public EntityComponent(boolean enabled){
        this.setEnabled(enabled);
    }

    public EntityComponent(){}




    /**
     * Called when the component is attached to an entity
     */
    public void onAttach(Entity entity){}

    /**
     * Called when the component is detached from an entity
     */
    public void onDetach(Entity entity){}




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
}
