package com.goatgames.goatengine.config.engine;

/**
 * Scripting Engine configuration
 */
public class ScriptingConfig {
    /**
     * Flag used to automatically reload scripts when their source code changes
     */
    public boolean autoReload = false;

    /**
     * The directory containing the scripts.
     * The script engine might use this value in order to resolve
     * script file paths. If the script is not found in this directory
     * the engine might fall back to a strategy where it considers the paths as a relative path
     * from the working directory
     */
    public String directory = "scripts/";
}
