package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.scriptingengine.common.IScriptingEngine;

/**
 * Scripting Engine for Lua integration
 */
public class LuaScriptingEngine implements IScriptingEngine {

    @Override
    public EntitySystem getScriptingSystem() {
        return new LuaEntityScriptSystem();
    }
}
