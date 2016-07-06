package com.goatgames.goatengine.config;

/**
 * Engine configuration.
 */

import com.goatgames.goatengine.files.IFileHandle;

import java.util.List;

/**
 * Interface representing configuration files
 * for the engine.
 */
public interface IEngineConfig{
    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @return returns the value of parameterName as a int.
     * @throws UnknownConfigParameterException if parameter not found
     */
    int getInt(String parameterName);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @return returns the value of as a String.
     * @throws UnknownConfigParameterException if parameter not found
     */
    String getString(String parameterName);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @return returns the value of parameterName as a float.
     * @throws UnknownConfigParameterException if parameter not found
     */
    float getFloat(String parameterName);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @return returns the value of as a boolean.
     * @throws UnknownConfigParameterException if parameter not found
     */
    boolean getBoolean(String parameterName);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @return returns the value of as a List.
     * @throws UnknownConfigParameterException if parameter not found
     */
    List<String> getList(String parameterName);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @param value the value to set to the parameter identified by name
     * @return returns the value of parameterName as a int.
     * @throws UnknownConfigParameterException if parameter not found
     */
    void setInt(String parameterName, int value);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @param value the value to set to the parameter identified by name
     * @return returns the value of as a String.
     * @throws UnknownConfigParameterException if parameter not found
     */
    void setString(String parameterName, String value);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @param value the value to set to the parameter identified by name
     * @return returns the value of parameterName as a float.
     * @throws UnknownConfigParameterException if parameter not found
     */
    void setFloat(String parameterName, float value);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @param value the value to set to the parameter identified by name
     * @return returns the value of as a boolean.
     * @throws UnknownConfigParameterException if parameter not found
     */
    void setBoolean(String parameterName, boolean value);

    /**
     * Returns the value of a parameter in the config file
     * @param parameterName name of the parameter in the config file
     * @param value the value to set to the parameter identified by name
     * @return returns the value of as a List.
     * @throws UnknownConfigParameterException if parameter not found
     */
    void setList(String parameterName, List<String> value);

    /**
     * Loads the configuration file from a file handle
     */
    void load(IFileHandle fileHande);

    class UnknownConfigParameterException extends RuntimeException{
        public UnknownConfigParameterException(String parameter){
            super(String.format("Parameter %s not found", parameter));
        }
    }
}
