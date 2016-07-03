package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.Gdx;
import com.goatgames.gdk.GAssert;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * A Lua Script
 */
public class LuaScript{

    private final String scriptFile;                         // Path to the script file
    private LuaValue chunk;                                  // Read data from lua script
    private Globals globals = JsePlatform.standardGlobals(); // The standard Lua Lib

    private boolean hasError = true; // indicates if the script has encountered an error

    /**
     * Constructs and initializes a script file
     * @param scriptFile represents the path to the script file
     */
    public LuaScript(String scriptFile){
        this.scriptFile = scriptFile;
    }

    /**
     * Loads the script file in memory
     * @return true if the load operation was successful
     */
    public boolean load(){
        // Check if script exists
        if(!GAssert.that(Gdx.files.internal(scriptFile).exists(),
                String.format("Lua Script not found: \"%s\"", scriptFile))){
            hasError = true;
            return false;
        }

        try{
            chunk = globals.load(Gdx.files.internal(scriptFile).readString());
            chunk.call();
        } catch (LuaError e) {
            hasError = true;
            throw new LuaScriptException(e,this.scriptFile);
        }
        hasError = false;
        return true;
    }

    /**
     * Reloads a script
     * @return true if the reload operation was successful
     */
    public boolean reload(){
        return load();
    }


    /**
     * Call a function in the Lua script with the no parameters
     * @param functionName the name of the function to call
     * @return true if the call was successful
     */
    public boolean executeFunction(String functionName){
        return executeFunction(functionName, (Object)null);
    }

    /**
     * Call a function in the Lua script with the parameters
     * @param functionName the name of the function to call
     * @return true if the call was successful
     */
    public boolean executeFunction(String functionName, Object ... objects){
        return executeFunctionWithParamAsArray(functionName, objects);
    }


    /**
     * Call a function in the Lua script with the given parameters passed
     * @param functionName the name of the function to call
     * @param objects the objects to pass a parameters
     * @return true if the call was successful
     */
    public boolean executeFunctionWithParamAsArray(String functionName, Object[] objects){

        if(hasError) return false;

        LuaValue function = globals.get(functionName); // Get the function

        if(!function.isfunction()){
            //Logger.error("Lua Error: function " + functionName + " does not exist in " + scriptFile);
            hasError = true;
            throw new LuaScriptException("Lua Error: function " + functionName + " does not exist", scriptFile);
        }

        LuaValue[] parameters = (objects == null) ? new LuaValue[0] : new LuaValue[objects.length];

        for(int i = 0; i<parameters.length; i++){
            parameters[i] = CoerceJavaToLua.coerce(objects[i]); // Convert Java Object To LuaValue
        }

        try{
            function.invoke(parameters);
        } catch (LuaError e){
            hasError = true;
            throw new LuaScriptException(e,this.scriptFile);
        }
        return true;
    }


    public boolean functionExists(final String functionName){
       LuaValue function = globals.get(functionName); // Get the function
       return function.isfunction();
    }

    /**
     * Exposes a java function to be called in a lua script
     * @param javaFunction the java function
     */
    public void exposeJavaFunction(TwoArgFunction javaFunction){
        globals.load(javaFunction);
    }

    public void exposeJavaObject(Object o){
        globals.load(CoerceJavaToLua.coerce(o));
    }

    /**
     * Returns the time the script was last modified
     * @return when the script was last modified
     */
    public long getLastModified(){
        return Gdx.files.internal(scriptFile).lastModified();
    }

    public LuaValue getChunk() {
        return chunk;
    }

    public Globals getGlobals() {
        return globals;
    }

    /**
     * Returns the name of the file
     * @return the name of the file as a String
     */
    public String getName() {
        return scriptFile;
    }


    // Exceptions //

    public static class LuaScriptException extends RuntimeException{

        public LuaScriptException(LuaError e, String fileName){
            super("Lua Error: " + getReason(e.getMessage()) +
                    "\nin " + fileName + " at line: " + getLineNumber(e.getMessage())
            );
        }

        public LuaScriptException(String errorMsg, String fileName){
            super("Lua Error: " + errorMsg + "in script " + fileName);
        }

        public static String getReason(String msg) {
            msg = msg.substring(msg.lastIndexOf("\n"));
            return msg.replace(String.valueOf(getLineNumber(msg)), "").replace(":", "");
        }

        public static int getLineNumber(String msg){
            msg = msg.substring(msg.lastIndexOf("\n"));
            String intValue = msg.replaceAll("[^0-9]", "");
            return (!intValue.isEmpty()) ? Integer.parseInt(intValue) : -1;
        }

    }










}
