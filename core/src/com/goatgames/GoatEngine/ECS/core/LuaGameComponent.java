package com.goatgames.goatengine.ecs.core;

import com.goatgames.gdk.GAssert;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Mostly used by scripts to easily create new components
 */
public class LuaGameComponent extends DynamicComponent {

    public static final String INTERNAL_KEY = "component_id_internal";
    private static final String ID = "LUA_GAME_COMPONENT";
    public LuaTable data;

    public LuaGameComponent(NormalisedEntityComponent data) {
        super(data);
        // Implicitly the method denormalise will be called
        // from super constructor so denormalise should never
        // be null. In such case that would mean super is broken
        // or in the case of a sub class of LuaGameComponent overriding
        // method denormalise but not calling super.

        GAssert.notNull(this.data, "Game Component data was null in constructor, did you call denormalise?");
    }

    /**
     * Ctor taking Lua Table as argument for it internal fields
     *
     * @param data
     */
    public LuaGameComponent(LuaTable data) {
        // Enabled by default unless specified otherwise
        super(data.get("isEnabled") == LuaValue.NIL || data.get("isEnabled").toboolean());
        GAssert.notNull(data, "LuaGameComponent Data was null in constructor table constructor");
        this.data = data;
        // This private ID makes sure that it is a GAME_COMPONENT
        this.data.set(INTERNAL_KEY, ID);
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent normData = super.normalise();
        normData.putAll(luaTableToMap(data));
        return normData;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        this.data = mapToLuaTable(data);
    }

    @Override
    public String getId() {
        GAssert.notNull(data, "LuaGameComponent Data was null");
        GAssert.that(data.get("component_id") != LuaValue.NIL, "Game component has no ID: Unknown component");
        return data.get("component_id").toString();
    }

    public LuaTable getData() {
        return data;
    }

    /**
     * Converts a map to a lua table
     *
     * @return
     */
    protected LuaTable mapToLuaTable(Map<String, String> map) {
        LuaTable table = (data == null) ? new LuaTable() : data;
        for (String key : map.keySet()) {
            table.set(key, map.get(key)); // TODO try to detect type of value in map
        }
        return table;
    }

    /**
     * Converts a lua table to a map
     *
     * @param table
     * @return
     */
    protected Map<String, String> luaTableToMap(LuaTable table) {
        Map<String, String> map = new HashMap<String, String>();
        // Convert LuaTable to HashMap
        int keysCount = table.keys().length;
        LuaValue[] keys = table.keys();
        for (int i = 0; i < keysCount; i++) {
            String key = keys[i].toString();
            map.put(key, table.get(key).toString());
        }
        return map;
    }
}