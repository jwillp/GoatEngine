package com.goatgames.goatengine.scriptingengine;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a collection of scripts names to be attached to the entity.
 * This component is used by the different Script Systems to create an instance of an IEntityScript
 * out of these "script names"
 */
public class ScriptComponent extends EntityComponent {

    public final static String ID = "SCRIPT_COMPONENT";


    // Member variables

    /**
     * Lists of scripts names attached to the entity
     */
    private List<String> scripts;

    /**
     * List of scripts scheduled to be removed
     */
    private List<String> scriptsToRemove;


    public ScriptComponent(NormalisedEntityComponent data){
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

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();

        String csv = this.scripts.toString().replace(", ", ";").replace("[", "").replace("]", "");
        data.put("scripts", csv);

        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        if(!GAssert.that(data.containsKey("scripts"), "ScriptComponent does not have scrips in its normalised form"))
            return;

        // Scripts are split by ";" (script;script;script)
        final String[] scriptNames = data.get("scripts").trim().split(";");
        this.scripts = new ArrayList<>(Arrays.asList(scriptNames));

        scriptsToRemove = new ArrayList<>();
    }

    // Accessors

    public List<String> getScripts(){ return this.scripts; }
    public void setScripts(List<String> scripts){this.scripts = scripts; }

    /**
     * Returns the scripts scheduled for removal
     * @return arrayList of scripts scheduled for removal
     */
    public List<String> getScriptsToRemove(){
        GAssert.notNull(this.scriptsToRemove, "scriptsToRemove == null");
        return this.scriptsToRemove;
    }

    @Override
    public String getId() {
        return ID;
    }
}
