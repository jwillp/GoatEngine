package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Component should be responsible for holding serialisation data of script.
 */
public class LuaEntityScriptComponent extends EntityComponent {

    public static final String ID = "LUA_ENTITY_SCRIPT_COMPONENT";
    private ArrayList<String> scripts;          // List of active scripts
    private ArrayList<String> scriptsToRemove;  // List of scripts scheduled to be removed

    public LuaEntityScriptComponent(boolean b) {
        super(b);
        scripts = new ArrayList<String>();
    }

    public LuaEntityScriptComponent(Map<String, String> map) {
        super(map);
    }




    /**
     * Adds a script to the component
     * @param scriptFilePath
     */
    public void addScript(String scriptFilePath){
        if(!GAssert.notNull(this.scripts, "scripts == null")){
            this.scripts = new ArrayList<>();
        }
        this.scripts.add(scriptFilePath);
    }

    /**
     * Adds multiple scripts to the component
     * @param scriptFiles
     */
    public void addScripts(ArrayList<String> scriptFiles){
        if(!GAssert.notNull(this.scripts, "scripts == null")){
            this.scripts = new ArrayList<>();
        }
        this.scripts.addAll(scriptFiles);
    }

    /**
     * Removes a script from the component
     * @param scriptFile
     */
    public void removeScript(String scriptFile){
        this.scheduleScriptForRemoval(scriptFile);
    }

    /**
     * Schedules a script for removal.
     * The system will later properly remove the script instance from the EntityScript Component
     * @param scriptFile
     */
    private void scheduleScriptForRemoval(String scriptFile){
        if(!GAssert.notNull(this.scriptsToRemove, "scriptsToRemove == null")){
            this.scriptsToRemove = new ArrayList<>();
        }
        this.scriptsToRemove.add(scriptFile);
    }


    /**
     * Returns all the active scripts
     * @return the scripts
     */
    public ArrayList<String> getScripts() {
        return scripts;
    }

    /**
     * Returns the scripts scheduled for removal
     * @return arrayList of scripts scheduled for removal
     */
    public ArrayList<String> getScriptsToRemove(){
        return this.scriptsToRemove;
    }


    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        // Convert array to csv (script.lua;script.lua;script.lua)
        String csv = this.scripts.toString().replace(", ", ";").replace("[", "").replace("]", "");
        map.put("scripts", csv);
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    protected void makeFromMap(Map<String, String> map) {
        // Convert
        if(map.get("scripts").equals("")){
            scripts = new ArrayList<String>();
        }else{
            // Scripts are split by ";" (script.lua;script.lua;script.lua)
            scripts = new ArrayList<String>(Arrays.asList(map.get("scripts").split(";")));
        }
    }

    /**
     * Used to clone a component
     *
     * @return
     */
    @Override
    public EntityComponent clone() {
        return new Factory().processMapData(this.getId(), this.makeMap());
    }


    @Override
    public String getId() {
        return ID;
    }

    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(componentId.equals(LuaEntityScriptComponent.ID),
                    "Component Factory Mismatch: ScriptComponent.ID != " + componentId);
            LuaEntityScriptComponent component = new LuaEntityScriptComponent(map);
            return component;
        }
    }
}
