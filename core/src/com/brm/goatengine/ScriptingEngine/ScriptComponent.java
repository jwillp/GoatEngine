package com.brm.GoatEngine.ScriptingEngine;

import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Enables entities to have Custom Behaviour using Scripts
 */
public class ScriptComponent extends EntityComponent {

    public static final String ID = "SCRIPT_COMPONENT";
    private ArrayList<String> scripts = new ArrayList<String>();

    public ScriptComponent(){
        super(true);
    }

    public ScriptComponent(boolean b) {
        super(b);
    }

    public ScriptComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        // Convert array to csv
        String csv = this.scripts.toString().replace(", ", ";").replace("\\[|\\]", "");
        map.put("scripts", this.scripts.toString());
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    protected void makeFromMap(Map<String, String> map) {
        // Convert
        scripts = new ArrayList<String>(Arrays.asList(map.get("scripts").split(";*")));
    }


    /**
     * Adds a script to the component
     * @param scriptFilePath
     */
    public void addScript(String scriptFilePath){
        if(this.scripts == null){
            this.scripts = new ArrayList<String>();
        }
        this.scripts.add(scriptFilePath);
    }


    /**
     * Removes a script from the component
     * @param scriptFilePath
     */
    public void removeScript(String scriptFilePath){
        this.scripts.remove(scriptFilePath);
    }

    /**
     * Returns all the scripts
     * @return the scripts
     */
    public ArrayList<String> getScripts() {
        return scripts;
    }


    @Override
    public String getId() {
        return ID;
    }
}
