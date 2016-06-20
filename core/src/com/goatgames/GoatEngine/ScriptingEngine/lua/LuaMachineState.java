package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.fsm.MachineState;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Used for creating Machine states from lua script
 */
public class LuaMachineState extends MachineState{

    private final LuaTable state;

    public LuaMachineState(LuaTable state) {
        this.state = state;
    }

    @Override
    public void onEnter(String oldStateName, MachineState oldState) {
        LuaValue method = this.state.get("onEnter");
        if(method != LuaValue.NIL)
            method.call(CoerceJavaToLua.coerce(oldStateName),
                    CoerceJavaToLua.coerce(oldState));
    }

    @Override
    public void onExit(String newStateName, MachineState newState) {
        LuaValue method = this.state.get("onExit");
        if(method != LuaValue.NIL)
            method.call(CoerceJavaToLua.coerce(newStateName),
                    CoerceJavaToLua.coerce(newState));
    }

    @Override
    public void onCollisionEvent(CollisionEvent event) {
        LuaValue method = this.state.get("onCollisionEvent");
        if(method != LuaValue.NIL)
            method.call(CoerceJavaToLua.coerce(event));
    }

    @Override
    public void onGameEvent(GameEvent event) {
        LuaValue method = this.state.get("onGameEvent");
        if(method != LuaValue.NIL)
            method.call(CoerceJavaToLua.coerce(event));
    }

    @Override
    public void onInputEvent(InputEvent event) {
        LuaValue method = this.state.get("onInputEvent");
        if(method != LuaValue.NIL)
            method.call(CoerceJavaToLua.coerce(event));
    }

    @Override
    public void update(float dt) {
        LuaValue method = this.state.get("update");
        if(method != LuaValue.NIL)
            method.call(CoerceJavaToLua.coerce(dt));
    }
}
