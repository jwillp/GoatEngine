package com.goatgames.goatengine.ui;

/**
 * Represents template variable
 */
public class UIVariable {

    public final static UIVariable NULL = new UIVariable(null, VariableType.STRING, true /* constant */);

    protected String value;
    protected VariableType varType;
    protected boolean _const; // is constant or not

    public UIVariable(String strValue){
        this(strValue, VariableType.STRING);
    }

    public UIVariable(int value){
        this(String.valueOf(value),VariableType.INT);
    }

    public UIVariable(boolean value) {
        this(String.valueOf(value), VariableType.BOOLEAN);
    }

    public UIVariable(boolean value, boolean constant) {
        this(String.valueOf(value), VariableType.BOOLEAN, constant);
    }

    public UIVariable(String valueAsStr, VariableType varType){
        this(valueAsStr, varType, false);
    }

    public UIVariable(String valueAsStr, VariableType varType, boolean constant){
        this.value = valueAsStr;
        this.varType = varType;
        this._const = constant;
    }


    public boolean getBoolean(){
        return Boolean.parseBoolean(value);
    }

    public int getInt(){
        return Integer.parseInt(value);
    }

    public String getString(){
        return value;
    }

    /**
     * If the type is dynamic, will dynamically try to find the type of the variable
     * @return the type of the variable
     */
    public VariableType findType(){

        // If type is already defined return the type
        if(this.varType != VariableType.DYNAMIC) return this.varType;

        // Boolean ? (easiest)
        String lowerCase = this.value.toLowerCase();
        if(lowerCase.equals("true") || lowerCase.equals("false")){
            return VariableType.BOOLEAN;
        }

        // integer ?
       if(isInteger(this.value)){
           return VariableType.INT;
       }

        // else String
        return VariableType.STRING;

    }
    public VariableType getVarType() {
        return varType;
    }

    public void setVarType(VariableType varType) {
        this.varType = varType;
    }

    public <T> T getValue() {
        /*T returnVal;
        switch (varType){
            case INT:
                returnVal = getInt();
                break;
            case BOOLEAN:
                returnVal = getBoolean();
                break;
            case STRING:
                break;
            case DYNAMIC:
                break;
        }*/
        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", varType, value);
    }

    public boolean isConst() {
        return _const;
    }

    /**
     * Represents the different possible types for a variable
     */
    public enum VariableType{
        INT,
        BOOLEAN,
        STRING,
        DYNAMIC,
    }

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
