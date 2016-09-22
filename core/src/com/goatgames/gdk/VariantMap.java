package com.goatgames.gdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Map used to hold different types of values.
 */
public class VariantMap {

    /**
     * Represents the data in the map
     */
    private Map<String, String> values = new HashMap<>();

    public void set(String name, int value){
        set(name, new Variant(value));
    }

    public void set(String name, float value){
        set(name, new Variant(value));
    }

    public void set(String name, boolean value){
        set(name, new Variant(value));
    }

    public void set(String name, String value){
        values.put(name, value);
    }

    public void set(String name, Variant value){
        set(name, value.getString());
    }

    /**
     * Returns a variant associated with a given name
     * @param name name of the variant to retrieve
     * @return the variant,
     */
    public Variant get(String name){
        final String value = values.get(name);
        return new Variant(value);
    }

    /**
     * Returns a certain value associated with name, if none for specified name returns the passed defaultValue
     *
     * @param name name of the value to retrieve
     * @param defaultValue default value to return in case the value does not exist for specified name
     * @return the variant value or default value.
     */
    public Variant getOrDefault(String name, Variant defaultValue){
        return values.containsKey(name) ? new Variant(values.get(name)) : defaultValue;
    }

    /**
     * Sets the content of the map
     * @param values the values to set
     */
    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    /**
     * Clears all the values from the map, leaving it empty
     */
    public void clear(){
        this.values.clear();
    }

    /**
     * Returns whether or not a value is associated with a specified name
     *
     * @param name name of the value
     * @return true if value exists, false otherwise
     */
    public boolean hasValue(String name){
        return values.containsKey(name);
    }

    /**
     * Returns all the values as map where the keys are the names of the values
     * and the values of the map the VariantValues as Strings
     *
     * @return map representing the values as strings
     */
    public Map<String, String> getValues() {
        return values;
    }

    /**
     * Represents template variable
     */
    public static class Variant {

        String value;

        /**
         * Constructs a string based variant
         *
         * @param value value as a string
         */
        public Variant(String value) {
            this.value = value;
        }

        /**
         * Construct an integer based variant
         *
         * @param value integer value
         */
        public Variant(int value) {
            this(String.valueOf(value));
        }

        /**
         * Construct a boolean based variant
         *
         * @param value boolean value
         */
        public Variant(boolean value) {
            this(String.valueOf(value));
        }

        /**
         * Construct a float based variant
         *
         * @param value float value
         */
        public Variant(float value) {
            this(String.valueOf(value));
        }

        /**
         * Returns the current value as a boolean.
         * Something different than "true", "false" or "0", "1" will return false.
         *
         * @return value as boolean
         */
        public boolean getBoolean() {
            return Boolean.parseBoolean(value);
        }

        /**
         * Returns the current value as an Int
         *
         * @return the value as an int
         */
        public int getInt() {
            return Integer.parseInt(value);
        }

        /**
         * Returns the current value as a float
         *
         * @return value as a float
         */
        public float getFloat() {
            return Float.parseFloat(value);
        }

        /**
         * Returns the current value as a String
         *
         * @return value as a string
         */
        public String getString() {
            return value;
        }
    }
}
