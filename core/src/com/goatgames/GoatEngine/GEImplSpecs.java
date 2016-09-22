package com.goatgames.goatengine;

import com.goatgames.gdk.logger.ILogger;
import com.goatgames.goatengine.ecs.io.PrefabFactory;
import com.goatgames.gdk.io.IFileManager;
import com.goatgames.goatengine.input.InputManager;
import com.goatgames.goatengine.screenmanager.IGameScreenLoader;
import com.goatgames.goatengine.scriptingengine.common.IScriptingEngine;

/**
 * Implementation configuration of the GoatEngine
 */
public interface GEImplSpecs {

    /**
     * Returns the logger to use
     * @return logger
     */
    ILogger getLogger();

    /**
     * File Manager to use
     * @return file manager
     */
    IFileManager getFileManager();

    /**
     * Input Manager to use
     * @return inputManager
     */
    InputManager getInputManager();

    /**
     * Scripting engine to use
     * @return scripting engine
     */
    IScriptingEngine getScriptingEngine();

    /**
     * Returns the prefab factory to use
     * @return prefab factory
     */
    PrefabFactory getPrefabFactory();

    IGameScreenLoader getGameScreenLoader();
}
