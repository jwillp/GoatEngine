package com.goatgames.goatengine.eventmanager;

import org.luaj.vm2.LuaTable;

/**
 * Class used by scripts to send their own events in order to communicate.
 * Cross scripting communication
 */
public class GameEvent extends Event{

    public final String name;   // The name of the event
    public final LuaTable data; // The data of the event this should always be a table


    public GameEvent(String name, LuaTable data){
        this.name = name;
        this.data = data;
    }

    @Override
    public boolean isOfType(String typeName) {
        return name.equals(typeName);
    }

}
