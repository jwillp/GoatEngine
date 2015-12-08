package com.brm.GoatEngine.ECS.core;


import com.brm.GoatEngine.Utils.Logger;
import com.brm.GoatEngine.Utils.PODType;

public abstract class EntityComponent {


    private static final String ID = "ENTITY_COMPONENT";
    private boolean enabled = true; //By default a component is enabled


    public class EntityComponentPOD extends PODType {
        @SerializeName("enabled")
        public boolean enabled;
    }


    public EntityComponent(){
        this(true);
    }

    public EntityComponent(boolean enabled){
        this.setEnabled(enabled);
    }


    /**
     * Ctor taking a Pod Representation of the current component
     * @param pod
     */
    public EntityComponent(EntityComponentPOD pod){
        makeFromPOD(pod);
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
    public final EntityComponentPOD toPODType(){
        EntityComponentPOD pod = (EntityComponentPOD) makePOD();
        try{
            pod.enabled = enabled;
        }catch (NullPointerException e){
            Logger.warn(this.getId() + " could not be converted to POD type");
        }
        return pod;
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     * @return
     */
    protected abstract EntityComponentPOD makePOD();

    /**
     * Builds the current object from a pod representation
     * @param pod the pod representation to use
     */
    protected abstract void makeFromPOD(EntityComponentPOD pod);



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
