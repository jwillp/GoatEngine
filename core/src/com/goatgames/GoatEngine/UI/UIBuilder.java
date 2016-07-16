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
    private final ObjectMap<UIWidgetType, UIWidgetFactory> widgetFactories;

    public UIBuilder(){
        widgetFactories = new ObjectMap<>();

        // Widget Factories
        addFactory(UIWidgetType.BUTTON, new UIButtonFactory());
        addFactory(UIWidgetType.TEXT_BUTTON, new UITextButtonFactory());
        addFactory(UIWidgetType.LABEL, new UILabelFactory());
        addFactory(UIWidgetType.TABLE, new UITableFactory());
        addFactory(UIWidgetType.CUSTOM_WIDGET, new UICustomWidgetFactory());
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
                }
            }
        }
        // Root (required)
        XmlReader.Element rootElement = templateElement.getChildByName("root");
        if(GAssert.notNull(rootElement, "Invalid UI Template: No <root> tag provided, the template won't be built"));
        UIWidgetDefinition rootDef = parseElement(rootElement);
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

    private Widget createWidget(UIWidgetDefinition def){
        UIWidgetFactory factory = widgetFactories.get(def.getType());
        //if (GAssert.notNull(factory, "factory == null"))
        return factory.createWidget(def);
    }

    public void addFactory(UIWidgetType widgetType, UIWidgetFactory factory){
        widgetFactories.put(widgetType, factory);
    }
}
