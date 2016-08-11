package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.goatgames.goatengine.scriptingengine.common.IEntityScript;

/**
 * Intrerface used to load native scripts by name
 */
public interface IScriptLoader {

    /**
     * Loads an IEntityScript by its name and returns an instance of the script
     * @param scriptName name of the script
     * @return isntance of the script
     */
    IEntityScript load(String scriptName);
}
