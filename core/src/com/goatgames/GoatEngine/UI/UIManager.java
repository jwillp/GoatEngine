package com.goatgames.goatengine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Manages a hierarchy of Widgets
 */
class UIManager {

    /**
     * A Map of widget ids and Widget instances
     */
    ObjectMap<String, Widget> widgets;
    Table root; // root uiWidget
    UIContext context;


    public UIManager(){
        context = new UIContext();
        widgets = new ObjectMap<>();


        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        root = new Table(uiSkin);

        root.defaults().fill();
        root.add("north").colspan(3);
        root.row();
        root.add("west");
        root.add("center").expand();
        root.add("east");
        root.row();
        root.add("south").colspan(3);

    }

    /**
     * Returns a widget by its Id
     */
    public Widget getWidget(String widgetId){
        return widgets.get(widgetId);
    }

    /**
     * Adds a widget to the manager
     */
    public void addWidget(String widgetId, Widget widget){
        widgets.put(widgetId, widget);
    }

    /**
     * Clears the Manager from any UI
     */
    public void clear(){
        widgets.clear();
    }

    /**
     * Returns the root table
     * @return the root table
     */
    public Table getRoot() {
        return root;
    }

    public UIContext getContext() {
        return context;
    }
}




