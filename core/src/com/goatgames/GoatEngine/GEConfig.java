package com.goatgames.goatengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Calendar;
import java.util.Date;

/**
 * Class loading the Global engine settings
 */
public class GEConfig {

    // Constants
    private static final String CONFIG_FILE_PATH = "priv_data/ge.conf";
    public static final String BUILD_VERSION = "16.02.12";                   // The current build version of the engine
    public static final Date LAUNCH_DATE = Calendar.getInstance().getTime(); // The date at which the engine was launched

    // The read Data from config files
    private static LuaTable data;


    /**
     * Loads the settings from the private data file
     */
    public static void load(){
        try{
            LuaScript defaultConfig = new LuaScript(CONFIG_FILE_PATH);
            if(!defaultConfig.load()){
                // TODO throw exception
            }
            data = new LuaTable();
            defaultConfig.executeFunction("geconf", data);

            // Game Specific override (if any)
            String configFile = getString("config_file");
            if(Gdx.files.internal(configFile).exists()){
                LuaScript gsScript = new LuaScript(configFile); // directly under root
                gsScript.load();
                gsScript.executeFunction("geconf", data);
            }
            // Post Data (sanitize data)
            defaultConfig.executeFunction("postconf", data);
        }catch (LuaScript.LuaScriptException ex){
            throw new InvalidConfigFileException(ex.getMessage());
        }
    }

    /**
     * returns the int value of a parameter
     * @param parameter
     * @return
     */
    public static int getInt(String parameter){
        return getParam(parameter).toint();
    }

    /**
     * Returns the string value of a parameter
     * @param parameter
     * @return
     */
    public static String getString(String parameter){
        return getParam(parameter).toString();
    }

    /**
     * Returns the float value of a parameter
     * @param parameter
     * @return
     */
    public static float getFloat(String parameter){
        return getParam(parameter).tofloat();
    }

    /**
     * Returns the boolean value of a parameter
     * @param parameter
     * @return
     */
    public static boolean getBoolean(String parameter){
        return getParam(parameter).toboolean();
    }

    /**
     * Returns an array of string for a given parameter
     * @param parameter desired parameter
     * @return
     */
    public static Array<String> getArray(String parameter){
        LuaTable table = getParam(parameter).checktable();
        Array<String> array = new Array<>();
        if(table.isnil()){
            return array; // empty array
        }

        LuaValue[] keys = table.keys();
        for(int i=0; i<table.keyCount(); i++){
            array.add(table.get(keys[i]).toString());
        }
        return array;
    }

    /**
     * Find in a table the desired parameter allowing such notation:
     * table.parameterTable.parameter
     * @param param the parameter to find (in point notation)
     * @return
     */
    private static LuaValue getParam(String param){
        String[] parts = param.split("\\.");
        LuaValue returnValue = data.get(parts[0]);
        // test if settings contain key
        for(int i = 1; i<parts.length ; i++){
            returnValue = returnValue.get(parts[i]);
        }
        return returnValue;
    }


    public static class InvalidConfigFileException extends RuntimeException{
        public InvalidConfigFileException(String msg){
            super(msg);
        }
    }

}
