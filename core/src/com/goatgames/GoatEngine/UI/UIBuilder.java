package com.goatgames.goatengine.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ui.factories.*;
import com.goatgames.goatengine.ui.macros.Macro;
import com.goatgames.goatengine.utils.GAssert;


/**
 * Factory used to create Widgets. If you want to manually construct custom widgets
 * subclass from this
 */
class UIBuilder{

    /**
     * List of factories to use to create the different widget types
     */
    private final ObjectMap<UIWidgetType, UIWidgetFactory> factories;

    private final ObjectMap<String, Macro> macros;

    public UIBuilder(){
        factories = new ObjectMap<>();
        macros = new ObjectMap<>();

        // Widget Factories
        addFactory(UIWidgetType.BUTTON, new UIButtonFactory());
        addFactory(UIWidgetType.TEXT_BUTTON, new UITextButtonFactory());
        addFactory(UIWidgetType.LABEL, new UILabelFactory());
        addFactory(UIWidgetType.TABLE, new UITableFactory());
        addFactory(UIWidgetType.CUSTOM_WIDGET, new UICustomWidgetFactory());

        // Marcos
        addMacro(new Macro.Multiply());
        addMacro(new Macro.Divide());
        addMacro(new Macro.Add());
        addMacro(new Macro.Minus());
        addMacro(new Macro.SetValue());
        addMacro(new Macro.Increment());
        addMacro(new Macro.IsNull());
        addMacro(new Macro.Not());
        addMacro(new Macro.Or());
        addMacro(new Macro.And());
    }

    /**
     * Builds a Complete UI hierarchy and returns a UI Manager
     */
    public UIManager build(String uiTemplate){
        UIManager uiManager = new UIManager();

        // 1. Get uiTemplate as a Resource
        String templateContent = GoatEngine.resourceManager.getText(uiTemplate);
        // 2. Read the file as XML
        this.parseTemplateFile(uiTemplate, templateContent, uiManager);


        // 3. Create definition from XML nodes
        // For each node create a widget and add child as necessary

        return uiManager;
    }


    private void parseTemplateFile(String templateName, String templateContent, UIManager ui){
        XmlReader xml = new XmlReader();
        XmlReader.Element templateElement = xml.parse(templateContent);
        GAssert.that(templateElement.getName().equals("template"),
                String.format("Invalid UI Template: No top level tag <template> found in %s", templateName));

        // Context (optional)
        ui.getContext();
        XmlReader.Element ctxElement = templateElement.getChildByName("context");
        if(ctxElement != null){
            int childCount = ctxElement.getChildCount();
            for(int i=0; i<childCount; i++){
                XmlReader.Element varElement = ctxElement.getChild(i);
                if(GAssert.that(varElement.getName().equals("var"),
                        String.format("Invalid UI Template: <context> must only have <var> children. tag %s will be ignored",
                                varElement.getName()))) {
                    String varName = varElement.get("name");
                    String varValue = varElement.getText();
                    ui.getContext().addVariable(varName,
                                                new UIVariable(varValue, UIVariable.VariableType.DYNAMIC));
                }
            }
        }
        // Root (required)
        XmlReader.Element rootElement = templateElement.getChildByName("root");
        if(GAssert.notNull(rootElement, "Invalid UI Template: No <root> tag provided, the template won't be built"));
        UIWidgetDefinition rootDef = parseElement(rootElement);

        executeMacros(rootDef, ui);

    }

    private UIWidgetDefinition parseElement(XmlReader.Element element){

        // Create Widget Definition from element name
        String elementName = element.getName();
        UIWidgetDefinition def = new UIWidgetDefinition(elementName);
        ObjectMap<String, String> attributes = element.getAttributes();
        if(attributes != null) {
            for (String propertyName : attributes.keys()) {
                def.setProperty(propertyName, attributes.get(propertyName));
            }
        }

        int childCount = element.getChildCount();
        for(int i = 0; i < childCount; i++){
            def.addChild(parseElement(element.getChild(i)));
        }
        return def;
    }

    /**
     * Adds a macro to the builder
     * @param macro a macro to add
     */
    private void addMacro(Macro macro){
        this.macros.put(macro.getToken(), macro);
    }

    /**
     * Gets a macro by its token name
     * @param macro token name of the macro (without the call token)
     * @return the macro or null if not found
     */
    private Macro getMacro(String macro){
        return this.macros.get(macro);
    }


    /**
     * Executes Macros and variable replacement on the definitions
     */
    private void executeMacros(UIWidgetDefinition definition, UIManager ui){
        for(String property: definition.getProperties()){
            String value = definition.get(property);
            // Variable Replacement
            value = replaceVariable(value, ui.context).getString();
            definition.setProperty(property,value);
        }
        Array<UIWidgetDefinition> children = definition.getChildren();
        for(UIWidgetDefinition child : children){
            executeMacros(child, ui);
        }
    }

    /**
     * If value is in the form of a template variable ($variable), finds its value using context
     * and return it as a UIVariable. If it is not in the form of a variable simple returns a
     * UIVariable representing the value (with deduced type)
     * @param value
     * @return
     */
    private UIVariable replaceVariable(String value, UIContext context){
        UIVariable var;
        if(value.startsWith("$")){
            String vName = value.substring(1); // Remove the first character ($)
            var = context.getVariable(vName);
        }else{
            var = new UIVariable(value);
        }
        var.setVarType(var.findType());
        return var;
    }

    private Widget createWidget(UIWidgetDefinition def){
        UIWidgetFactory factory = factories.get(def.getType());
        //if (GAssert.notNull(factory, "factory == null"))
        return factory.createWidget(def);
    }

    public void addFactory(UIWidgetType widgetType, UIWidgetFactory factory){
        factories.put(widgetType, factory);
    }
}
