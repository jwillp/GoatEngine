package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.GameComponent;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.utils.Logger;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Scripting API for Lua
 */
public class GoatEngineAPI extends TwoArgFunction {

    public GoatEngineAPI(){}

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("console", CoerceJavaToLua.coerce(GoatEngine.console));
        library.set("logger", CoerceJavaToLua.coerce(new Logger()));      // We only access static methods
        library.set("engine", CoerceJavaToLua.coerce(new GoatEngine()));  // We only access static methods


        library.set("eventManager", CoerceJavaToLua.coerce(GoatEngine.eventManager));


        //library.set("GameComponent", CoerceJavaToLua.coerce(new GameComponent(true)));
        library.set("GameComponent",CoerceJavaToLua.coerce(new GameComponentAPI()));
        library.set("GameEvent",CoerceJavaToLua.coerce(new GameEventAPI()));

        env.set("GE", library);
        env.get("package").get("loaded").set("GE", library);
        return library;
    }


    /**
     * Used so scripts can generate GameComponents
     */
    public class GameComponentAPI extends OneArgFunction{

        @Override
        public LuaValue call(LuaValue table) {
            return CoerceJavaToLua.coerce(new GameComponent(table.checktable()));
        }
    }

    /**
     * Used so scripts can generate GameEvents
     */
    public class GameEventAPI extends TwoArgFunction{

        @Override
        public LuaValue call(LuaValue modname, LuaValue table) {
            return CoerceJavaToLua.coerce(new GameEvent(modname.toString(), table.checktable()));
        }
    }














}
