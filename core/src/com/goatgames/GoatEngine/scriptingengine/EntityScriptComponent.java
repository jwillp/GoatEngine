package com.goatgames.goatengine.scriptingengine;

import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.utils.GAssert;

/**
 * Enables entities to have Custom Behaviour using Scripts
 */
public class EntityScriptComponent extends EntityComponent {

    public static final String ID = "ENTITY_SCRIPT_COMPONENT";

    private ObjectMap<String, IEntityScript> scripts;

    public EntityScriptComponent(boolean enabled) {
        super(enabled);
        scripts = new ObjectMap<>();
    }

    public EntityScriptComponent(NormalisedEntityComponent data) {
        super(data);
    }

    /**
     * Adds a script to the component
     * @param script
     */
    public void addScript(IEntityScript script){
        if(!GAssert.notNull(this.scripts, "scripts == null")){
            this.scripts = new ObjectMap<>();
        }
        if(GAssert.notNull(script, "script == null, it will not be added")){
            this.scripts.put(script.getName(), script);
        }
    }

    /**
     * Removes a script from the component. Checks by identity
     * @param script the script instance to remove
     */
    public void removeScript(IEntityScript script){
        if(GAssert.notNull(script, "script == null")){
            removeScriptByName(script.getName());
        }
    }

    /**
     * Removes a script by name
     * @param scriptName the name of the script to remove
     */
    public void removeScriptByName(String scriptName){
        if(GAssert.notNull(this.scripts, "scripts == null")){
            this.scripts.remove(scriptName);
            //script.onDetach();
        }
    }


    /**
     * Clears all script instances
     */
    public void clearScripts(){
        if(GAssert.notNull(this.scripts, "scripts == null")){
            /*for(IEntityScript script : this.scripts) {
                script.onDetach();
            }*/
            this.scripts.clear();
        }
    }

    /**
     * Returns all the scripts
     * @return the scripts
     */
    public ObjectMap<String,IEntityScript> getScripts() {
        return scripts;
    }

    @Override
    public NormalisedEntityComponent normalise() {
        return super.normalise();
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        scripts = new ObjectMap<>();
    }

    @Override
    public EntityComponent clone() {
        return new Factory().processMapData(this.getId(), this.normalise());
    }

    @Override
    public String getId() {
        return ID;
    }

    public boolean hasScriptWithName(String scriptName) {
        if(GAssert.notNull(this.scripts, "scripts == null")){
            return this.scripts.containsKey(scriptName);
        }else{
            return false;
        }
    }


    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, NormalisedEntityComponent data){
            GAssert.that(componentId.equals(EntityScriptComponent.ID),
                    "Component Factory Mismatch: EntityScriptComponent.ID != " + componentId);
            EntityScriptComponent component = new EntityScriptComponent(data);
            return component;
        }
    }
}
