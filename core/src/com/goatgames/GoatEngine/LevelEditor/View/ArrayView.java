package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.goatgames.goatengine.utils.Logger;

import java.util.ArrayList;

/**
 * A View used to display arrays contained in Components
 */
public class ArrayView extends Table {

    private final String name;
    private ArrayList<String> elements;

    private TextButton btnAddElement;

    private Label labelSize;



    /**
     * Ctor
     * @param skin
     * @param name the name of the array
     * @param elements elements to display as an array list of strings
     */
    public ArrayView(Skin skin, String name, ArrayList<String> elements){
        super(skin);
        this.name = name;
        this.elements = elements;
        initRootLayout();

    }


    private void initRootLayout(){
        Skin skin =  getSkin();
        btnAddElement = new TextButton("+", skin);
        labelSize = new Label("0", skin);

        add(name).colspan(2);
        add(btnAddElement);
        row();

        add("Size");
        add(labelSize);
        add("");
        row();

        add("Size");
        add("Size");
        add("Size");
        row();
    }

    public void update(){

    }


    public void onElementUpdated(int index, String newValue){
        Logger.debug("ARRAY ELEMENT UPDATED");
    }

    public void onElementDeleted(int index){
        Logger.debug("ARRAY ELEMENT DELETED");
    }

    public void onElementAdded(String value){
        elements.add(value);
    }


}
