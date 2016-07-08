package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Component should be responsible for holding serialisation data of script.
 */
public class LuaEntityScriptComponent extends EntityComponent {

    public static final String ID = "LUA_ENTITY_SCRIPT_COMPONENT";
    private List<String> scripts;          // List of active scripts
    private List<String> scriptsToRemove;  // List of scripts scheduled to be removed

    public LuaEntityScriptComponent(boolean b) {
        super(b);
        scripts = new ArrayList<>();
        scriptsToRemove = new ArrayList<>();
    }

    public LuaEntityScriptComponent(NormalisedEntityComponent data) {
        super(data);
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
    public List<String> getScripts() {
        return scripts;
    }

    /**
     * Returns the scripts scheduled for removal
     * @return arrayList of scripts scheduled for removal
     */
    public List<String> getScriptsToRemove(){
        GAssert.notNull(this.scriptsToRemove, "scriptsToRemove == null");
        return this.scriptsToRemove;
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        // Convert array to csv (script.lua;script.lua;script.lua)
        String csv = this.scripts.toString().replace(", ", ";").replace("[", "").replace("]", "");
        data.put("scripts", csv);
        return data;
    }

    public void denormalise(NormalisedEntityComponent data) {
        if(data.get("scripts").equals("")){
            scripts = new ArrayList<>();
        }else{
            // Scripts are split by ";" (script.lua;script.lua;script.lua)
            scripts = new ArrayList<>(Arrays.asList(data.get("scripts").split(";")));
        }
        scriptsToRemove = new ArrayList<>();
    }

    @Override
    public String getId() {
        return ID;
    }
}
