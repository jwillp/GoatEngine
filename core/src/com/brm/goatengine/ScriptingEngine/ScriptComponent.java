package com.brm.GoatEngine.ScriptingEngine;

import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.ArrayList;

/**
 * Enables entities to have Custom Behaviour using Scripts
 */
public class ScriptComponent extends EntityComponent {

    public static final String ID = "SCRIPT_COMPONENT";
    private ArrayList<String> scripts;

    class ScriptComponentPOD extends EntityComponentPOD{
        public ArrayList<String> scripts;

    }




    public ScriptComponent(){
        scripts = new ArrayList<String>();
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected EntityComponentPOD makePOD() {
        ScriptComponentPOD pod = new ScriptComponentPOD();
        pod.scripts = this.scripts;
        return pod;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromPOD(EntityComponentPOD pod) {
        ScriptComponentPOD scriptPOD = (ScriptComponentPOD) pod;
        scripts = scriptPOD.scripts;
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
