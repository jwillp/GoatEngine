package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.fsm.FiniteStateMachine;
import com.goatgames.goatengine.fsm.MachineState;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.utils.Logger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used for creating Machine states from lua script
 */
public class LuaMachineState extends MachineState {

    private final LuaTable stateTable;

    public LuaMachineState(LuaTable stateTable) {
        this.stateTable = stateTable;
    }

    @Override
    public void onEnter(FiniteStateMachine stateMachine, String oldStateName, MachineState oldState) {
        String functionName = "onEnter";
        if(this.stateTable.get(functionName).isfunction())
            executeFunction(functionName, stateMachine,oldStateName,oldState);
    }

    @Override
    public void onExit(FiniteStateMachine stateMachine, String newStateName, MachineState newState) {
        String functionName = "onExit";
        if(this.stateTable.get(functionName).isfunction())
            executeFunction(functionName, stateMachine,newStateName,newState);
    }

    @Override
    public void onCollisionEvent(FiniteStateMachine finiteStateMachine, CollisionEvent event) {
        String functionName = "onCollisionEvent";
        if(this.stateTable.get(functionName).isfunction())
            executeFunction(functionName, finiteStateMachine, event);
    }

    @Override
    public void onGameEvent(FiniteStateMachine finiteStateMachine, GameEvent event) {
        String functionName = "onGameEvent";
        if(this.stateTable.get(functionName).isfunction())
            executeFunction(functionName, finiteStateMachine, event);
    }

    @Override
    public void onInputEvent(FiniteStateMachine finiteStateMachine, InputEvent event) {
        String functionName = "onInputEvent";
        if(this.stateTable.get(functionName).isfunction())
            executeFunction(functionName, finiteStateMachine, event);
    }

    @Override
    public void update(FiniteStateMachine stateMachine, float dt) {
        String functionName = "update";
        if(this.stateTable.get(functionName).isfunction())
            executeFunction(functionName, stateMachine, dt);
    }

    /**
     * Call a function in the Lua script with the parameters
     * @param functionName the name of the function to call
     */
    public void executeFunction(String functionName, Object ... objects){
        executeFunctionWithParamAsArray(functionName, objects);
    }

    /**
     * Call a function in the Lua script with the given parameters passed
     * @param functionName the name of the function to call
     * @param objects the objects to pass a parameters
     */
    public void executeFunctionWithParamAsArray(String functionName, Object[] objects){

        LuaValue function = stateTable.get(functionName); // Get the function

        // Must call with self as first parameter (lua class)
        boolean mustAddSelf = stateTable.getmetatable() != null;
        if(mustAddSelf) {
            ArrayList<Object> temp = new ArrayList<>(Arrays.asList(objects));
            temp.add(0, stateTable);
            objects = temp.toArray();
        }
        LuaValue[] parameters = (objects == null) ? new LuaValue[0] : new LuaValue[objects.length];

        for(int i = 0; i<parameters.length; i++){
            // Convert Java Object To LuaValue
            Object obj = (objects != null) ? objects[i] : null;
            parameters[i] = CoerceJavaToLua.coerce(obj);
        }
        function.invoke(parameters);
    }
}
