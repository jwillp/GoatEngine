package com.goatgames.goatengine.scriptingengine.common;

/**
 * Interface used to load native scripts by name
 */
public interface IScriptLoader {

    /**
     * Loads an IEntityScript by its name and returns an instance of the script
     * @param scriptName name of the script
     * @return instance of the script
     */
    IEntityScript load(String scriptName);
}
