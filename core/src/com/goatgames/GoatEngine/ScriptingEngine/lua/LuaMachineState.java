package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.fsm.MachineState;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import org.luaj.vm2.LuaTable;
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
        this.state.get("onEnter").call(CoerceJavaToLua.coerce(oldStateName),
                CoerceJavaToLua.coerce(oldState));
    }

    @Override
    public void onExit(String newStateName, MachineState newState) {
        this.state.get("onExit").call(CoerceJavaToLua.coerce(newStateName),
                CoerceJavaToLua.coerce(newState));
    }

    @Override
    public void onCollisionEvent(CollisionEvent event) {
        this.state.get("onCollisionEvent").call(CoerceJavaToLua.coerce(event));
    }

    @Override
    public void onGameEvent(GameEvent event) {
        this.state.get("onGameEvent").call(CoerceJavaToLua.coerce(event));
    }

    @Override
    public void onInputEvent(InputEvent event) {
        this.state.get("onInputEvent").call(CoerceJavaToLua.coerce(event));
    }

    @Override
    public void update(float dt) {
        this.state.get("update").call(CoerceJavaToLua.coerce(dt));
    }
}
