package com.goatgames.goatengine.utils;

import com.badlogic.gdx.utils.Array;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;


import java.io.FileNotFoundException;

/**
 * Basic abstract class for configurations
 * they are based on Lua Tables therefore require
 * Lua scripting engine's features
 */
public abstract class EngineConfig {

    // The read Data from config files
    protected LuaTable data;


    public abstract void load() throws FileNotFoundException;

    /**
     * returns the int value of a parameter
     * @param parameter
     * @return
     */
    public int getInt(String parameter){
        return getParam(parameter).toint();
    }

    /**
     * Returns the string value of a parameter
     * @param parameter
     * @return
     */
    public String getString(String parameter){
        return getParam(parameter).toString();
    }

    /**
     * Returns the float value of a parameter
     * @param parameter
     * @return
     */
    public float getFloat(String parameter){
        return getParam(parameter).tofloat();
    }

    /**
     * Returns the boolean value of a parameter
     * @param parameter
     * @return
     */
    public boolean getBoolean(String parameter){
        return getParam(parameter).toboolean();
    }

    /**
     * Returns an array of string for a given parameter
     * @param parameter desired parameter
     * @return
     */
    public Array<String> getArray(String parameter){
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
    private LuaValue getParam(String param){
        String[] parts = param.split("\\.");
        LuaValue returnValue = data.get(parts[0]);
        // test if settings contain key
        for(int i = 1; i<parts.length ; i++){
            try{
                returnValue = returnValue.get(parts[i]);
            }catch(LuaError er){
                throw new UnkownConfigParameterException(param);
            }
        }
        return returnValue;
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
                throw new UnkownConfigParameterException(param);
            }
        }
    }
    /**
     * Sets a new value to a certain parameter
     * @param parameter the parameter to change
     * @param value the new value
     *
     */
    public void setInt(String parameter, int  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    /**
     * Sets a new value to a certain parameter
     * @param parameter the parameter to change
     * @param value the new value
     *
     */
    public void setString(String parameter, String  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    /**
     * Sets a new value to a certain parameter
     * @param parameter the parameter to change
     * @param value the new value
     *
     */
    public void setFloat(String parameter, float  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    /**
     * Sets a new value to a certain parameter
     * @param parameter the parameter to change
     * @param value the new value
     *
     */
    public void setBoolean(String parameter, boolean  value){
        setParam(parameter, LuaValue.valueOf(value));
    }

    /**
     * Sets a new value to a certain parameter
     * @param parameter the parameter to change
     * @param value the new value
     *
     */
    public void  setArray(String parameter, Array<String> value){
        //setParam(parameter, LuaValue.valueOf(value));
        // TODO Implement
        throw new NotImplementedException();
    }




















    public LuaTable getData() {
        return data;
    }


    public static class InvalidConfigFileException extends RuntimeException{
        public InvalidConfigFileException(String msg){
            super(msg);
        }
    }

    public static class UnkownConfigParameterException extends RuntimeException{
        public UnkownConfigParameterException(String parameter){
            super(String.format("Parameter %s not found", parameter));
        }
    }
}
