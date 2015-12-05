package com.brm.GoatEngine.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.brm.GoatEngine.Files.FileSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class used for Game Specific Configs
 */
public class GameConfig extends EngineConfig{

    public static final String BASE = "CONFIG_BASE";


    /**
     * Data read from config file
     * HashMap<ParameterName, ValueAsString>
     */
    private class ConfigData extends HashMap<String, String>{}
    public class Parameter{
        private String value;

        public Parameter(final String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }


        public int toInt(){
            return Integer.parseInt(value);
        }

        public float toFloat(){
            return Float.parseFloat(value);
        }

        public boolean toBoolean(){
            return Boolean.parseBoolean(value);
        }


        public String[] toStringArray(final String delimiter){
            return value.split(delimiter);
        }

    }

    private final HashMap<String, ConfigData> overrides = new HashMap<String, ConfigData>();

    private String currentOverride;
    private String configName;
    private final String configPath;
    /**
     * Ctor
     * @param configPath path to config file
     */
    public GameConfig(String configPath){
        this.configPath = configPath;
    }


    public void load(){
        // Read the default base
        FileHandle baseHandle = Gdx.files.internal(configPath);
        configName = baseHandle.nameWithoutExtension();
        ConfigData baseData = readData(configPath);
        overrides.put(BASE, baseData);
        currentOverride = BASE;

        //Read all overrides
        String inheritencePrefix = configName + "_";

        Array<FileHandle> potentialOverrides = FileSystem.getFilesInDir(baseHandle.parent().toString());
        for(FileHandle overrideHandle: potentialOverrides){
            if(overrideHandle.name().contains(inheritencePrefix)){
                ConfigData data = readData(overrideHandle.path());
                // Get Override Name
                String overrideName  = overrideHandle.nameWithoutExtension().replace(inheritencePrefix, "");
                overrides.put(overrideName, data);
            }
        }
    }

    public void reload(){
        overrides.clear();
        load();
    }


    /**
     * Name of the config override
     * @param overrideName
     */
    public void setOverride(final String overrideName){
        if(!overrides.containsKey(overrideName))
            throw new UnknownGameConfigOverrideException(currentOverride, configName);
        this.currentOverride = overrideName;
    }

    /**
     * Returns the value of a parameter, if it was not foundin the current
     * overdrive tries to find in config base, if this fails as well
     * throw an exception
     * @param parameter the parameter from which we want to retrieve the value
     * @return Parameter Object
     */
    public Parameter getParameter(final String parameter){
        ConfigData currentConfig = overrides.get(currentOverride);
       if(currentConfig.containsKey(parameter)){
           return new Parameter(currentConfig.get(parameter));
       }else{
           return getBaseParameter(parameter);
       }
    }

    /**
     * Returns the parameter value in the config base
     * @param parameter
     * @return
     */
    private Parameter getBaseParameter(final String parameter){
        ConfigData base = overrides.get(BASE);
        if(base.containsKey(parameter)){
            return new Parameter(base.get(parameter));
        }
        throw new UndefinedGameConfigParametereException(parameter, configName);
    }



    /**
     * Reads config data from file
     * @param configFile
     * @return
     */
    private ConfigData readData(String configFile){
        FileInputStream inputStream = null;
        ConfigData data = new ConfigData();
        try {
            inputStream = new FileInputStream(configFile);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            // Read Data
            for(String parameter: prop.stringPropertyNames()){
                String value = prop.getProperty(parameter);
                data.put(parameter,value);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }



    /**
     * Exceptions thrown when an override does not exists for a base config file
     */
    public class UnknownGameConfigOverrideException extends RuntimeException{
        public UnknownGameConfigOverrideException(String overrideName, String configName){
            super("The config override '" + overrideName + "' was not found for base config '" + configName + "'.");
        }
    }

    /**
     * Exceptions thrown when a parameter does not exists in a base config file
     */
    public class UndefinedGameConfigParametereException extends RuntimeException{
        public UndefinedGameConfigParametereException(String parameter, String configName){
            super("The config parameter '" + parameter + "' was not found in config '" + configName + "'.");
        }
    }

}
