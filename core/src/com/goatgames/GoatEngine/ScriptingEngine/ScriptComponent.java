package com.goatgames.goatengine.scriptingengine;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Enables entities to have Custom Behaviour using Scripts
 */
public class ScriptComponent extends EntityComponent {

    public static final String ID = "SCRIPT_COMPONENT";
    private ArrayList<String> scripts;

    // TODO maybe keep script instances, because values can differ for the same script in two different entities

    public ScriptComponent(boolean b) {
        super(b);
        scripts = new ArrayList<String>();
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


    /**
     * Adds a script to the component
     * @param scriptFilePath
     */
    public void addScript(String scriptFilePath){
        if(this.scripts == null){
            this.scripts = new ArrayList<>();
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

    public void addScripts(ArrayList<String> strings){
        if(this.scripts == null){
            this.scripts = new ArrayList<>();
        }
        this.scripts.addAll(strings);
    }

    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(componentId.equals(ScriptComponent.ID),
                    "Component Factory Mismatch: ScriptComponent.ID != " + componentId);
            ScriptComponent component = new ScriptComponent(map);
            return component;
        }
    }
}
