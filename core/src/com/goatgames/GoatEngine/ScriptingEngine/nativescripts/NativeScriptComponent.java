package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.ecs.core.EntityComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * USed for Native (Java) Scripts
 */
public class NativeScriptComponent extends EntityComponent {

    public final static String ID = "NATIVE_SCRIPT_COMPONENT";


    private Array<NativeScript> scripts;

    public NativeScriptComponent() {
        this(true);
    }
    public NativeScriptComponent(boolean enabled) {
        super(enabled);
    }

    public NativeScriptComponent(Map<String, String> map) {
        super(map);
    }




    public void addScript(NativeScript script){
        scripts.add(script);
    }

    public void removeScript(NativeScript script){
        scripts.removeValue(script,false);
    }

    @Override
    protected Map<String, String> makeMap() {
        return new HashMap<String, String>();
    }

    @Override
    protected void makeFromMap(Map<String, String> map) {}

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

    public Array<NativeScript> getScripts() {
        return scripts;
    }
}
