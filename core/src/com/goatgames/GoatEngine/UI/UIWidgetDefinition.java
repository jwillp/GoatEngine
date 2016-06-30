package com.goatgames.goatengine.ui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.utils.Logger;

/**
 * Represents the definition of a Widget UI Item,
 * that was read from template file.
 */
public class UIWidgetDefinition extends ObjectMap<String,String> {

    private Array<UIWidgetDefinition> children;

    /**
     * Ctor
     * @param widgetType type of the widget to define
     */
    public UIWidgetDefinition(String widgetType){
        super();
        children = new Array<>();

        setProperty("id", "uniqueId");    // Make sure there is an id property

        // Detect type from string
        String resolvedTypeName = this.resolveTypeName(widgetType);
        try{
            UIWidgetType tmp = UIWidgetType.valueOf(resolvedTypeName);
        } catch(IllegalArgumentException ex){
            // Could not resolve
            Logger.warn(String.format("Invalid UI Widget Type: %s unexpected behaviour might occur.", widgetType));
        }
        setProperty("type", resolvedTypeName);  // Specify the widget type
        setDefaults();
    }

    /**
     * Sets the defaults values for the common properties
     */
    protected void setDefaults(){
        // Default Widget Types
        final String defaultValue = "";

        setProperty("width", defaultValue);
        setProperty("height", defaultValue);
        setProperty("size", defaultValue);
        setProperty("size", defaultValue);
        setProperty("prefWidth", defaultValue);
        setProperty("minWidth", defaultValue);
        setProperty("maxWidth", defaultValue);

        setProperty("prefHeight", defaultValue);
        setProperty("minHeight", defaultValue);
        setProperty("maxHeight", defaultValue);


        setProperty("padLeft", defaultValue);
        setProperty("padRight", defaultValue);
        setProperty("padTop", defaultValue);
        setProperty("padBottom", defaultValue);
        setProperty("pad", defaultValue);

        setProperty("spaceLeft", defaultValue);
        setProperty("spaceRight", defaultValue);
        setProperty("spaceTop", defaultValue);
        setProperty("spaceBottom", defaultValue);
        setProperty("space", defaultValue);

        setProperty("align", defaultValue);

        setProperty("expend", defaultValue);
        setProperty("fill", defaultValue);

        setProperty("colspan", defaultValue);
        setProperty("uniform", defaultValue);
    }


    /**
     * Tries to resolve the type name read from file.
     * to a UIWidgetType Name as a String
     */
    protected String resolveTypeName(String rawType){
        rawType = rawType.toUpperCase();

        //TEXT_BUTTON
        if(rawType.equals("TEXTBUTTON")){
            return UIWidgetType.TEXT_BUTTON.toString();
        }

        // CUSTOM WIDGET
        if(rawType.equals("CUSTOMWIDGET")){
            return UIWidgetType.CUSTOM_WIDGET.toString();
        }

        return rawType;
    }

    /**
     * Sets a property to a certain value
     * @param propertyName the name of the property
     * @param value the value of the property
     */
    public void setProperty(String propertyName, String value){
        put(propertyName, value);
    }

    /**
     * @param propertyName the name of the property
     * @return returns the value of the property specified by name
     */
    public String getProperty(String propertyName){
        return get(propertyName);
    }

    /**
     * Returns the type of the widget
     * @return the type of the widget
     */
    public UIWidgetType getType(){
        return UIWidgetType.valueOf(getProperty("type"));
    }

    public void addChild(UIWidgetDefinition childDefinition) {
        this.children.add(childDefinition);
    }

    @Override
    public String toString() {
        return getProperty("type");
    }

    /**
     * Returns the properties of the definition
     * @return the property names of the definition
     */
    public Keys<String> getProperties(){
        return keys();
    }

    public Array<UIWidgetDefinition> getChildren() {
        return children;
    }
}
