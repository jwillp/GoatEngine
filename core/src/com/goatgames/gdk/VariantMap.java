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

    public Variant getOrDefault(String name, Variant defaultValue){
        return values.containsKey(name) ? new Variant(values.get(name)) : defaultValue;
    }

    /**
     * Sets the content of the map
     * @param values
     */
    public void setValues(Map<String, String> values) {
        this.values = values;
    }


    public void clear(){
        this.values.clear();
    }

    /**
     * Returns wether or not a value is associated with specified name
     * @param name name of the value
     * @return true if value exists, false otherwise
     */
    public boolean hasValue(String name){
        return values.containsKey(name);
    }

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
         * @param value
         */
        public Variant(String value) {
            this.value = value;
        }

        /**
         * Construct an integer based variant
         *
         * @param value
         */
        public Variant(int value) {
            this(String.valueOf(value));
        }

        /**
         * Consturct a boolean based variant
         *
         * @param value
         */
        public Variant(boolean value) {
            this(String.valueOf(value));
        }

        /**
         * Construct a float based variant
         *
         * @param value
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

        public int getInt() {
            return Integer.parseInt(value);
        }

        public float getFloat() {
            return Float.parseFloat(value);
        }

        public String getString() {
            return value;
        }

        /**
         * Indicates if an given string represents an integer
         *
         * @param str string to test
         * @return true if string represents integer, otherwise false
         */
        private static boolean isInteger(String str) {
            if (str == null) {
                return false;
            }
            int length = str.length();
            if (length == 0) {
                return false;
            }
            int i = 0;
            if (str.charAt(0) == '-') {
                if (length == 1) {
                    return false;
                }
                i = 1;
            }
            for (; i < length; i++) {
                char c = str.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            return true;
        }

    }
}
