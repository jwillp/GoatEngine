package com.goatgames.goatengine.screenmanager;

import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import com.goatgames.goatengine.utils.EngineConfig;
import org.luaj.vm2.LuaTable;

import java.io.FileNotFoundException;


/**
 * Game screen config
 */
public class GameScreenConfig extends EngineConfig {

    private final static String DEFAULT_SCREEN = GEConfig.PRIV_DATA_DIRECTORY + "default_screen.ges";
    private final String gameScreenFile;

    public GameScreenConfig(String gameScreenFile){
        this.gameScreenFile = gameScreenFile;
    }


    public void load() throws FileNotFoundException {
        try{
            data = new LuaTable();

            LuaScript defaultConfig = new LuaScript(DEFAULT_SCREEN);
            if(!defaultConfig.load()){
                throw new FileNotFoundException(DEFAULT_SCREEN);
            }
            defaultConfig.executeFunction("conf", data);


            LuaScript configScript = new LuaScript(gameScreenFile);
            if(!configScript.load()){
                throw new FileNotFoundException(gameScreenFile);
            }
            configScript.executeFunction("conf", data);


            // Post Data (sanitize data)
            defaultConfig.executeFunction("postconf", data, GoatEngine.config.getData());
        }catch (LuaScript.LuaScriptException ex){
            throw new InvalidConfigFileException(ex.getMessage());
        }

    }
}
