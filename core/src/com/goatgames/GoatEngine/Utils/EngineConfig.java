package com.goatgames.goatengine.utils;

/**
 * Basic abstract class for configuration
 */
public abstract class EngineConfig {


    /**
     * Try to applied a property, if the property string is Empty
     * keep the default value of the parameter
     * @param defaultProperty the default value of Config Property
     * @param readProperty the read from file property
     */
    public static String applyProperty(String defaultProperty, final String readProperty){
        if(!readProperty.isEmpty()){
            return readProperty;
        }
        return defaultProperty;
    }
    /**
     * Try to applied a property, if the property string is Empty
     * keep the default value of the parameter
     * @param defaultProperty the default value of Config Property
     * @param readProperty the read from file property
     */
    public static boolean getBooleanProperty(boolean defaultProperty, final String readProperty){
        if(!readProperty.isEmpty()){
            return Boolean.parseBoolean(readProperty);
        }
        return defaultProperty;
    }

    /**
     * Try to applied a property, if the property string is Empty
     * keep the default value of the parameter
     * @param defaultProperty the default value of Config Property
     * @param readProperty the read from file property
     */
    public static float getFloatProperty(float defaultProperty, final String readProperty){
        if(!readProperty.isEmpty()){
            return Float.parseFloat(readProperty);
        }
        return defaultProperty;
    }






}
