package com.goatgames.goatengine.config;

import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.files.IFileHandle;
import com.goatgames.goatengine.utils.NotImplementedException;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Engine configuration based on Lua tables
 */
public class LuaEngineConfig implements IEngineConfig {

    // The read Data from config files
    protected LuaTable data;

    @Override
    public int getInt(String parameter){
        return getParam(parameter).toint();
    }

    @Override
    public String getString(String parameter){
        return getParam(parameter).toString().replace("nil", "");
    }

    @Override
    public float getFloat(String parameter){
        return getParam(parameter).tofloat();
    }

    @Override
    public boolean getBoolean(String parameter){
        return getParam(parameter).toboolean();
    }

    @Override
    public List<String> getList(String parameter){
        LuaTable table = getParam(parameter).checktable();
        List<String> array = new ArrayList<>();
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
    private LuaValue getParam(String param){
        String[] parts = param.split("\\.");
        LuaValue returnValue = data.get(parts[0]);
        // test if settings contain key
        for(int i = 1; i<parts.length ; i++){
            try{
                returnValue = returnValue.get(parts[i]);
            }catch(LuaError er){
                throw new UnknownConfigParameterException(param);
            }
        }
        return returnValue;
    }

    @Override
    public void setInt(String parameter, int  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    @Override
    public void setString(String parameter, String  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    @Override
    public void setFloat(String parameter, float  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    @Override
    public void setBoolean(String parameter, boolean  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    @Override
    public void setList(String parameterName, List<String> value){
        throw new NotImplementedException();
    }

    private void setParam(String param, LuaValue value){
        String[] parts = param.split("\\.");
        LuaValue returnValue = data.get(parts[0]);
        // test if settings contain key
        for(int i = 1; i<parts.length ; i++){
            try{
                if(i == parts.length-1){ // last item
                    returnValue.set(parts[i], value);
                }else {
                    returnValue = returnValue.get(parts[i]);
                }
            }catch(LuaError er){
                throw new UnknownConfigParameterException(param);
            }
        }
    }


    @Override
    public void load(IFileHandle fileHande) {

    }

    public LuaTable getData() {
        return data;
    }

    public static class InvalidConfigFileException extends RuntimeException{
        public InvalidConfigFileException(String msg){
            super(msg);
        }
    }
}
