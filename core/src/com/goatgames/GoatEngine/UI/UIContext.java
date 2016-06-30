package com.goatgames.goatengine.ui;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Contains accessible variables and macros of the template
 */
public class UIContext {

    private ObjectMap<String, UIVariable> variables;

    public UIContext() {
        variables = new ObjectMap<>();

        // Default variables
        addVariable("true", new UIVariable(true));
        addVariable("false", new UIVariable(false));

    }

    /**
     * Adds a variable to the context
     * @param varName name of the variable to add
     * @param value UIVariable instance
     */
    public void addVariable(String varName, UIVariable value){
        this.variables.put(varName,value);
    }

    /**
     * Returns a variable by its name
     * @param varName name of the variable
     * @return variable
     */
    public UIVariable getVariable(String varName){
        return this.variables.get(varName);
    }

    /**
     * Indicates whether or not a variable exists in the context
     * @param varName name of the variable
     * @return true if exits, otherwise false
     */
    public boolean hasVariable(String varName){
        return this.variables.containsKey(varName);
    }

    /**
     * Removes a variable from the context
     * @param varName name of the variable
     */
    public void removeVariable(String varName){
        this.variables.remove(varName);
    }
}
