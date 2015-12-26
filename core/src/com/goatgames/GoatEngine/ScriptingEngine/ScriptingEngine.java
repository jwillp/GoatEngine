package com.goatgames.goatengine.scriptingengine;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.utils.Logger;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Script engine used to communicate between the GameEngine and scripts
 */
public class ScriptingEngine{

    private GroovyScriptEngine engine;
    private Binding globalScope;


    //A Hash containing a the source for every script run in by the engine
    //Where the key is the path of the script
    private HashMap<String, EntityScriptInfo> entityScripts = new HashMap<String, EntityScriptInfo>();

    // A list of scripts that ran with errors
    private ArrayList<String> errorScripts = new ArrayList<String>();


    /**
     * Default ctor
     */
    public ScriptingEngine(){}


    /**
     * Initialises the Script Engine
     * By creating the environment (Context)
     * And exposing the basic Game Engine API
     */
    public void init(){

        // Init the script engine interpreter
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(EntityScript.class.getCanonicalName());

        try {
            engine = new GroovyScriptEngine(GEConfig.ScriptingEngine.SCRIPTS_DIR, this.getClass().getClassLoader());
            engine.setConfig(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        globalScope = new Binding();


        //EXPOSE GOAT ENGINE API
        //Pass the engine to the current globalScope that way we have access to the whole engine (and game)
        addObject("GoatEngine", new GoatEngine()); //Don't mind the new we'll only access static methods

        //Put some helpers Globals
        addObject("Console", GoatEngine.console); //Console for console.log, instead of doing GoatEngine.getConsole()
        addObject("EventManager", GoatEngine.eventManager); // To access event manager
    }



    /**
     * Adds an object to the Script Engine's global context
     * Useful for game specific Script API
     * @param objectName
     * @param object
     * @param <T>
     */
    public <T> void addObject(String objectName, T object){
        this.globalScope.setVariable(objectName, object);
    }


    /**
     * Runs a script according to its name
     * @param scriptName
     * @return
     */
    public Object runScript(String scriptName){
        Object result = null;
        try {
            result = this.engine.run(scriptName, globalScope);
        } catch (ResourceException e) {
            ScriptNotFoundException scriptNotFoundException = new ScriptNotFoundException(scriptName);
            e.printStackTrace();
            Logger.fatal(scriptNotFoundException.getMessage());
            Logger.fatal(e.getStackTrace());
            throw scriptNotFoundException;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Runs a script using it's source code as a string
     * @param source
     * @return
     */
    public Object runScriptSource(String source) {
        try {
            return this.engine.run(source, this.globalScope);
        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Runs an Entity Script
     * @param scriptName
     * @return
     */
    public EntityScript runEntityScript(String scriptName, Entity entityObject){

        //If the script is not in memory, we'll "register" it. (List as a potential script)
        if(!isRegistered(scriptName)){
            registerScript(scriptName);
        }

        //Does the current entity has an instance of that script?
        if(this.entityScripts.get(scriptName).getInstance(entityObject.getID()) == null){
            Binding binding = this.copyBinding(globalScope);
            binding.setVariable("myEntityId", entityObject.getID());
            this.entityScripts.get(scriptName).addInstance(entityObject.getID(), groovyRunEntityScript(scriptName,binding));

        }

        //Does it need to be refreshed?
        if(GEConfig.ScriptingEngine.AUTO_RELOAD){
            if(this.isSourceNewer(scriptName)){
                this.entityScripts.get(scriptName).setLastModified(this.getLastModified(scriptName));
                this.refreshEntityScript(scriptName);
            }
        }


        return this.entityScripts.get(scriptName).getInstance(entityObject.getID());
    }


    /**
     * Registers the script with the engine. (Make it available to the engine)
     * @param scriptName
     */
    private void registerScript(String scriptName){
        this.entityScripts.put(scriptName, new EntityScriptInfo(this.getLastModified(scriptName)));
    }

    /**
     * Indicates if a script was registered with the engine
     * @param scriptName
     * @return true if registered
     */
    private boolean isRegistered(String scriptName){
        return this.entityScripts.containsKey(scriptName);
    }

    /**
     * Runs a script in the Engine
     * @param scriptName
     * @param binding
     * @return
     */
    private EntityScript groovyRunEntityScript(String scriptName, Binding binding){
        try {
            return (EntityScript)engine.run(scriptName, binding);
        } catch (ResourceException e) {
            logError(scriptName, e.getMessage());
        } catch (ScriptException e) {
            logError(scriptName, e.getMessage());
        } catch (MultipleCompilationErrorsException e){
            logError(scriptName, e.getMessage());
        }
        return null;
    }


    /**
     * Reloads an entity script for all concerned entities
     */
    public void refreshEntityScript(String scriptName) {
        EntityScriptInfo info = this.entityScripts.get(scriptName);
        for(Map.Entry<String, EntityScript> entry: info.getInstances().entrySet()){
            Binding binding = this.copyBinding(globalScope);
            binding.setVariable("entity", entry.getKey());
            entry.setValue(groovyRunEntityScript(scriptName, binding));
        }
        errorScripts.remove(scriptName);
    }








    /**
     * Returns whether a Script file was changed on disk but not in memory
     * @param scriptName the name of the script to test
     * @return
     */
    private boolean isSourceNewer(String scriptName){
        long fileTime = getLastModified(scriptName);
        long memoryTime = this.entityScripts.get(scriptName).getLastModified();
        return fileTime != memoryTime;
    }


    /**
     * Returns when a script file was last modified
     * on disk (not in memory)
     * @return
     * @param scriptName
     */
    private long getLastModified(String scriptName){
        return Gdx.files.internal(scriptName).lastModified();

    }


    /**
     * Returns a copy of the binding
     * @param binding
     * @return
     */
    private Binding copyBinding(Binding binding){
        return new Binding(binding.getVariables());
    }



    public void dispose() { }


    /**
     * Logs Scripts related errors
     * @param scriptName
     * @param message
     */
    public void logError(String scriptName, String message){
        if(!errorScripts.contains(scriptName)){
            Logger.error(message);
            errorScripts.add(scriptName);
        }
    }



    // EXCEPTIONS //

    /**
     * Exception for Script not found
     */
    public static class ScriptNotFoundException extends RuntimeException{
        public ScriptNotFoundException(String scriptName){
            super("The Script: " + scriptName + " was not found");
        }
    }

}
