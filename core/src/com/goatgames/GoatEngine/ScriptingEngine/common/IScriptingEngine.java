package com.goatgames.goatengine.scriptingengine.common;

import com.goatgames.goatengine.ecs.core.EntitySystem;

/**
 * This interface should provide all necessary methods to incorporate scripting in the engine.
 * (this excludes native scripting, as this is a slightly separate concept).
 * This applies to dynamic scripting languages such as lua, javascript, python, ruby, scala etc ..
 */
public interface IScriptingEngine {

    /**
     * Returns the Scripting System to use, for entity scripts processing
     * @return EntitySystem for Scripting capabilities
     */
    EntitySystem getScriptingSystem();
}
