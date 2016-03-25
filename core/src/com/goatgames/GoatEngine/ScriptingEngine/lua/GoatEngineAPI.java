package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.AudioMixer;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.GameComponent;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.Logger;
import com.goatgames.goatengine.utils.Timer;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Scripting API for Lua
 */
public class GoatEngineAPI extends TwoArgFunction {

    public GoatEngineAPI(){}

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();

        library.set("logger", CoerceJavaToLua.coerce(new Logger()));      // We only access static methods
        library.set("engine", CoerceJavaToLua.coerce(new GoatEngine()));  // We only access static methods

        library.set("resourceManager", CoerceJavaToLua.coerce(GoatEngine.resourceManager));
        library.set("eventManager", CoerceJavaToLua.coerce(GoatEngine.eventManager));

        library.set("gamePadManager", CoerceJavaToLua.coerce(GoatEngine.inputManager.getGamePadManager()));

        library.set("GAssert", CoerceJavaToLua.coerce(new GAssert())); // We only access static methods


        //library.set("GameComponent", CoerceJavaToLua.coerce(new GameComponent(true)));
        library.set("GameComponent",CoerceJavaToLua.coerce(new GameComponentAPI()));
        library.set("GameEvent",CoerceJavaToLua.coerce(new GameEventAPI()));

        library.set("playMusic", new PlayMusicAPI());
        library.set("playAudio", new PlayAudioAPI());

        //env.set("input.Buttons", CoerceJavaToLua.coerce(new GamePadMap()));

        library.set("Timer", CoerceJavaToLua.coerce(new TimerAPI()));

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

    /**
     * Used to create timers
     */
    public class TimerAPI extends OneArgFunction{

        @Override
        public LuaValue call(LuaValue delay) {
            return CoerceJavaToLua.coerce(new Timer(delay.toint()));
        }
    }






    public class PlayMusicAPI extends TwoArgFunction{
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            return CoerceJavaToLua.coerce(AudioMixer.playMusic(arg1.toString(), arg2.toboolean()));
        }

    }



    public class PlayAudioAPI extends OneArgFunction{

        @Override
        public LuaValue call(LuaValue arg1) {
            return CoerceJavaToLua.coerce(AudioMixer.playSound(arg1.toString()));
        }

    }


}
