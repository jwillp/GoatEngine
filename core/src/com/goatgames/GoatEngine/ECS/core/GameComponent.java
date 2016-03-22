package com.goatgames.goatengine.ecs.core;

import com.goatgames.goatengine.utils.GAssert;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* Mostly used by scripts to easily create new components
*/
public class GameComponent extends EntityComponent {

    private static final String ID = "GAME_COMPONENT";
    private static final String INTERNAL_KEY = "component_id_internal";

    public LuaTable data;

    public GameComponent(Map<String, String> map) {
        super(map);
        // Implicitly the method makeFromMap will be called
        // from super constructor so makeFromMap should never
        // be null. In such case that would mean super is broken
        // or in the case of a sub class of GameComponent overriding
        // method makeFromMap but not calling super.

        GAssert.notNull(data,"Game Component data was null in map constructor, did you call makeFromMap?");
    }

    /**
     * Ctor taking Lua Table as argument for it internal fields
     * @param data
     */
    public GameComponent(LuaTable data){
        // Enabled by default unless specified otherwise
        super(data.get("isEnabled") == LuaValue.NIL || data.get("isEnabled").toboolean());
        GAssert.notNull(data, "GameComponent Data was null in constructor table constructor");
        this.data = data;
        // This private ID makes sure that it is a GAME_COMPONENT
        this.data.set(INTERNAL_KEY, ID);
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
        GAssert.that(data.get("component_id") != LuaValue.NIL, "Game component has no ID: Unkown component");
        return data.get("component_id").toString();
    }

    public LuaTable getData(){
        return data;
    }

    /**
     * Converts a map to a lua table
     * @return
     */
    protected LuaTable mapToLuaTable(Map<String, String> map){
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
    protected Map<String, String> luaTableToMap(LuaTable table){
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



    // FACTORY //
    public static class Factory implements EntityComponentFactory{
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(map.containsKey(INTERNAL_KEY), String.format("Game Component %s has no ID_INTERNAL", componentId));
            String internalId = map.get(INTERNAL_KEY);
            GAssert.that(Objects.equals(internalId, GameComponent.ID),
                    String.format("Component Factory Mismatch: GameComponent internal id %s != %s", internalId, INTERNAL_KEY));
            return new GameComponent(map);
        }
    }



}
