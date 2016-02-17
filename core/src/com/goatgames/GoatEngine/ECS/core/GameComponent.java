package com.goatgames.goatengine.ecs.core;

import com.goatgames.goatengine.utils.GAssert;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

/**
* Mostly used by scripts to easily create new components
*/
public class GameComponent extends EntityComponent {

    public LuaTable data;

    public GameComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Ctor taking Lua Table as argument for it internal fields
     * @param data
     */
    public GameComponent(LuaTable data){
        // Enabled by default unless specified otherwise
        super(data.get("isEnabled") == LuaValue.NIL || data.get("isEnabled").toboolean());
        GAssert.notNull(data, "GameComponent Data was null in constructor");
        this.data = data;
        // This private ID makes sure that it is a GAME_COMPONENT
        this.data.set("component_id_internal", "GAME_COMPONENT");
    }

    @Override
    protected Map<String, String> makeMap() {
        return luaTableToMap(data);
    }

    @Override
    protected void makeFromMap(Map<String, String> map) {
        data = mapToLuaTable(map);
    }

    @Override
    public String getId() {
        GAssert.notNull(data, "GameComponent Data was null");
        return data.get("component_id").toString();
    }

    public LuaTable getData(){
        return data;
    }

    /**
     * Converts a map to a lua table
     * @return
     */
    private LuaTable mapToLuaTable(Map<String, String> map){
        LuaTable table = (data == null) ? new LuaTable() : data;
        for(String key : map.keySet()){
            table.set(key, map.get(key)); // TODO try to detect type of value in map
        }
        return table;
    }

    /**
     * Converts a lua table to a map
     * @param table
     * @return
     */
    private Map<String, String> luaTableToMap(LuaTable table){
        Map<String, String> map = new HashMap<String, String>();
        // Convert LuaTable to HashMap
        int keysCount = table.keys().length;
        LuaValue[] keys = table.keys();
        for(int i=0; i<keysCount; i++){
            String key = keys[i].toString();
            map.put(key, table.get(key).toString());
        }
        return map;
    }




}
