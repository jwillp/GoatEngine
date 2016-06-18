package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * USed for Native (Java) Scripts
 */
public class NativeScriptComponent extends EntityComponent {

    public final static String ID = "NATIVE_SCRIPT_COMPONENT";

    private Array<NativeEntityScript> scripts;

    public NativeScriptComponent() {
        this(true);
    }
    public NativeScriptComponent(boolean enabled) {
        super(enabled);
    }

    public NativeScriptComponent(NormalisedEntityComponent data) {
        super(data);
    }

    public void addScript(NativeEntityScript script){
        scripts.add(script);
    }

    public void removeScript(NativeEntityScript script){
        scripts.removeValue(script,false);
    }

    /**
     * Used to clone a component
     *
     * @return
     */
    @Override
    public EntityComponent clone() {
        return null;
    }

    @Override
    public String getId() {
        return ID;
    }

    public Array<NativeEntityScript> getScripts() {
        return scripts;
    }
}
