package com.goatgames.goatengine;

import com.goatgames.gdk.logger.ILogger;
import com.goatgames.goatengine.ecs.io.IniPrefabLoader;
import com.goatgames.goatengine.ecs.io.PrefabFactory;
import com.goatgames.goatengine.files.GdxFileManager;
import com.goatgames.gdk.io.IFileManager;
import com.goatgames.goatengine.input.InputManager;
import com.goatgames.goatengine.logger.GameLogger;
import com.goatgames.goatengine.screenmanager.IGameScreenLoader;
import com.goatgames.goatengine.scriptingengine.common.IScriptingEngine;

/**
 * Default implementation details for the Goat Engine.
 */
public class DefaultGEImplSpecs implements GEImplSpecs {

    private final PrefabFactory prefabFactory;
    private final IScriptingEngine scriptingEngine;
    private final InputManager inputManager;
    private final IFileManager fileManager;
    private ILogger logger;

    public DefaultGEImplSpecs(){
        fileManager = new GdxFileManager();
        logger = new GameLogger(fileManager);
        prefabFactory = new PrefabFactory(new IniPrefabLoader());
        inputManager = new InputManager();
        scriptingEngine = null;
    }

    @Override
    public ILogger getLogger() {
        return this.logger;
    }

    @Override
    public IFileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public InputManager getInputManager() {
        return this.inputManager;
    }

    @Override
    public IScriptingEngine getScriptingEngine() {
        return this.scriptingEngine;
    }

    @Override
    public PrefabFactory getPrefabFactory() {
        return this.prefabFactory;
    }

    @Override
    public IGameScreenLoader getGameScreenLoader() {
        return null;
    }
}
