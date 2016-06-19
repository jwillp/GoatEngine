package com.goatgames.goatengine;

import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import com.goatgames.goatengine.utils.EngineConfig;
import org.luaj.vm2.LuaTable;

import java.util.Calendar;
import java.util.Date;

/**
 * Class loading the Global engine settings
 */
public class GEConfig extends EngineConfig{

    // Constants
    public static final String PRIV_DATA_DIRECTORY = "priv_data/";
    private static final String CONFIG_FILE_PATH = PRIV_DATA_DIRECTORY + "ge.conf";
    public static final String BUILD_VERSION = "16.02.12";                   // The current build version of the engine
    public static final Date LAUNCH_DATE = Calendar.getInstance().getTime(); // The date at which the engine was launched




    /**
     * Loads the settings from the private data file
     */
    public void load(){
        // Make sure priv_data exist
        if(!Gdx.files.internal(PRIV_DATA_DIRECTORY).exists()){
            throw new MissingConfigFileException(
                    String.format("The directory for private data \"%s\" is missing.", PRIV_DATA_DIRECTORY));
        }

        try{
            LuaScript defaultConfig = new LuaScript(CONFIG_FILE_PATH);
            if(!defaultConfig.load()){
                // Most likely the file was not found
                throw new MissingConfigFileException(CONFIG_FILE_PATH + " is missing from internal data." +
                        " contact Goat Game Support for assistance. ");
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
     * Thrown when the internal priv_data ge.conf is missing
     */
    private class MissingConfigFileException extends RuntimeException {
        public MissingConfigFileException(String msg){
            super(msg);
        }
    }
}
