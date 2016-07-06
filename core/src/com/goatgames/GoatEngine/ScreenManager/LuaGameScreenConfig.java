package com.goatgames.goatengine.screenmanager;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.config.GEConfig;
import com.goatgames.goatengine.config.LuaEngineConfig;
import com.goatgames.goatengine.files.IFileHandle;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import org.luaj.vm2.LuaTable;

/**
 * Lua based game screen configuration
 */
public class LuaGameScreenConfig extends LuaEngineConfig implements IGameScreenConfig {

    private final static String DEFAULT_SCREEN = GEConfig.PRIV_DATA_DIRECTORY + "default_screen.ges";

    public LuaGameScreenConfig(){}

    @Override
    public void load(IFileHandle handle){
        try{
            data = new LuaTable();

            if(!GAssert.that(handle.exists(),
                    String.format("Could not load game screen config: %s does not exist.", handle.getPath()))) return;

            LuaScript defaultConfig = new LuaScript(DEFAULT_SCREEN);
            defaultConfig.load();
            defaultConfig.executeFunction("conf", data);


            LuaScript configScript = new LuaScript(handle.getPath());
            configScript.load();
            configScript.executeFunction("conf", data);

            // Post Data (sanitize data)
            defaultConfig.executeFunction("postconf", data, GoatEngine.config.getData());
        }catch (LuaScript.LuaScriptException ex){
            throw new LuaEngineConfig.InvalidConfigFileException(ex.getMessage());
        }

    }
}
