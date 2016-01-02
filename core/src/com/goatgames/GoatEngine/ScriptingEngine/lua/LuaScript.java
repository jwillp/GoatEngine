package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.utils.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * A Lua Script
 */
public class LuaScript {

    private final String scriptFile;                         // Path to the script file
    LuaValue chunk;                                          // Read data from t lua script
    private Globals globals = JsePlatform.standardGlobals(); // The standard Lua Lib

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
        if(!Gdx.files.internal(scriptFile).exists()){
            Logger.error("Lua Script not found: " + scriptFile);
            return false;
        }

        try{
            chunk = globals.load(Gdx.files.internal(scriptFile).readString());
        } catch (LuaError e) {
            // If reading the file fails, then log the error to the console
            Logger.error("Lua Error in file" + scriptFile + ": " + e.getMessage());
            Logger.logStackTrace(e);
            return false;
        }

        // An important step. Calls to script method do not work if the chunk is not called here
        chunk.call();

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
        return executeFunction(functionName, new Object[0]);
    }

    /**
     * Call a function in the Lua script with the given parameters passed
     * @param functionName the name of the function to call
     * @param objects the objects to pass a parameters
     * @return true if the call was successful
     */
    public boolean executeFunction(String functionName, Object[] objects){

        LuaValue function = globals.get(functionName); // Get the function

        if(!function.isfunction()){
            Logger.error("Lua Error: function " + functionName + " does not exist.");
            return false;
        }

        LuaValue[] parameters = new LuaValue[objects.length];
        for(int i = 0; i<objects.length; i++){
            parameters[i] = CoerceJavaToLua.coerce(objects[i]); // Convert Java Object To LuaValue
        }

        try{
            function.invoke(parameters);
        } catch (LuaError e){
            // Call function with converted parameters
            Logger.error("Lua Error in " + this.scriptFile + ": " + e.getMessage());
            Logger.logStackTrace(e);
            return false;
        }
        return true;
    }


    /**
     * Exposes a java function to be called in a lua script
     * @param javaFunction the java function
     */
    public void exposeJavaFunction(TwoArgFunction javaFunction){
        globals.load(javaFunction);
    }



}
