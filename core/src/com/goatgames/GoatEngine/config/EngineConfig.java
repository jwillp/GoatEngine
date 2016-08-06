package com.goatgames.goatengine.config;

import com.goatgames.goatengine.files.IFileHandle;
import com.goatgames.goatengine.utils.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Engine config based on Map<String,String>
 */
public class EngineConfig implements IEngineConfig {

    Map<String, String> data;

    public EngineConfig(){
        data = new HashMap<>();
    }

    @Override
    public int getInt(String parameterName) {
        return Integer.parseInt(data.get(parameterName));
    }

    @Override
    public String getString(String parameterName) {
        return data.get(parameterName);
    }

    @Override
    public float getFloat(String parameterName) {
        return Float.parseFloat(data.get(parameterName));
    }

    @Override
    public boolean getBoolean(String parameterName) {
        return Boolean.parseBoolean(data.get(parameterName));
    }

    @Override
    public List<String> getList(String parameterName) {
        return null;
    }

    @Override
    public void setInt(String parameterName, int value) {
        data.put(parameterName, String.valueOf(value));
    }

    @Override
    public void setString(String parameterName, String value) {
        data.put(parameterName, value);
    }

    @Override
    public void setFloat(String parameterName, float value) {
        data.put(parameterName, String.valueOf(value));
    }

    @Override
    public void setBoolean(String parameterName, boolean value) {
        data.put(parameterName, String.valueOf(value));
    }

    @Override
    public void setList(String parameterName, List<String> value) {
        data.put(parameterName, String.valueOf(value));
    }

    @Override
    public void load(IFileHandle fileHande) {
        throw new NotImplementedException();
    }
}
