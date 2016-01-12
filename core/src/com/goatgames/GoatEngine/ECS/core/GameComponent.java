package com.goatgames.goatengine.ecs.core;

import java.util.HashMap;
import java.util.Map;

/**
* Mostly used by scripts to easily create new components
*/
public class GameComponent extends EntityComponent {

    HashMap<String, String> map;
    private String id;

    public GameComponent(Map<String, String> map) {
        super(map);
    }

    public GameComponent(boolean b) {
        super(b);
        map = new HashMap<>();
    }

    @Override
    protected Map<String, String> makeMap() {
        return map;
    }

    @Override
    protected void makeFromMap(Map<String, String> map) {
        this.map = (HashMap<String, String>) map;
    }

    @Override
    public String getId() {
        return map.get("ID");
    }

    /**
     * Sets the id of the component
     * @param id
     */
    public void setId(final String id) {
        this.map.put("ID", id);
    }


    // FIELD MANAGEMENT //

    /**
     * Adds a new field
     * @param fieldName name of the field
     * @param value the initial value to set
     */
    public void addField(String fieldName, String value){
        map.put(fieldName, value);
    }

    /**
     * Adds a new field
     * @param fieldName name of the field
     * @param value the initial value to set
     */
    public void addField(String fieldName, int value){
        addField(fieldName, String.valueOf(value));
    }

    /**
     * Adds a new field
     * @param fieldName name of the field
     * @param value the initial value to set
     */
    public void addField(String fieldName, float value){
        addField(fieldName, String.valueOf(value));
    }

    /**
     * Adds a new field
     * @param fieldName name of the field
     * @param value the initial value to set
     */
    public void addField(String fieldName, boolean value){
        addField(fieldName, String.valueOf(value));
    }

    /**
     * sets the value of a field
     * @param fieldName name of the field
     * @param value the new value to set
     */
    public void setField(String fieldName, String value){
        addField(fieldName, value);
    }

    /**
     * sets the value of a field
     * @param fieldName name of the field
     * @param value the new value to set
     */
    public void setField(String fieldName, int value){
        addField(fieldName, String.valueOf(value));
    }

    /**
     * sets the value of a field
     * @param fieldName name of the field
     * @param value the new value to set
     */
    public void setField(String fieldName, float value){
        addField(fieldName, String.valueOf(value));
    }

    /**
     * sets the value of a field
     * @param fieldName name of the field
     * @param value the new value to set
     */
    public void setField(String fieldName, boolean value){
        addField(fieldName, String.valueOf(value));
    }


    /**
     * Returns the String value of a field
     * @param fieldName name of the field
     * @return the field as a String
     */
    public String getString(String fieldName){
        return map.get(fieldName);
    }

    /**
     * Returns the int value of a field
     * @param fieldName name of the field
     * @return the field as an int
     */
    public int getInt(String fieldName){
        return Integer.parseInt(map.get(fieldName));
    }

    /**
     * Returns the float value of a field
     * @param fieldName name of the field
     * @return the field as a float
     */
    public float getFloat(String fieldName){
        return Float.parseFloat(map.get(fieldName));
    }

    /**
     * Returns the boolean value of a field
     * @param fieldName name of the field
     * @return the field as a boolean
     */
    public boolean getBoolean(String fieldName){
        return Boolean.parseBoolean(map.get(fieldName));
    }


}
