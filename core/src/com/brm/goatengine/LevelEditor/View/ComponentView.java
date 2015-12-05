package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vie wof an Entity Component
 */
public abstract class ComponentView extends Table {


    EntityComponent component;

    protected CheckBox checkBoxEnable;
    TextButton btnRemove;
    Table contentTable;
    TextButton btnApply;

    protected HashMap<String, CheckBox> booleanFields = new  HashMap<String,CheckBox>();
    protected  HashMap<String, TextField> stringFields = new  HashMap<String, TextField>();

    public ComponentView(EntityComponent component, Skin skin) {
        super(skin);
        setDebug(false);
        this.component = component;
        initRootLayout();
        initContentTable();
        initContent();
        initApplyButton();
    }


    protected abstract void initContent();
    protected abstract void onApply();

    private void initRootLayout(){
        Skin skin =  getSkin();


        top().center();
        defaults().top().left().expandX();


        // Header [enable] Component name [remove]
        checkBoxEnable = new CheckBox(component.getId(), skin);
        checkBoxEnable.setChecked(component.isEnabled());
        add(checkBoxEnable);

        btnRemove = new TextButton("-", skin);
        add(btnRemove).top().right();

        row();

    }
    private void initContentTable(){
        contentTable = new Table(getSkin());
        contentTable.left();


        //readComponent();

        // Content
        /*contentTable.add("TEST LABEL");
        contentTable.row();

        contentTable.add("TEST LABEL");
        contentTable.row();

        contentTable.add("TEST LABEL");*/

        add(contentTable).colspan(2).row();
    }
    private void initApplyButton(){
        btnApply = new TextButton("Apply", this.getSkin());
        add(btnApply).colspan(2).fill().expandX().pad(20);
        btnApply.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ComponentView.this.onApply();
            }
        });
    }





    /**
     * Adds a boolean field
     * @param fieldName
     * @param value
     */
    protected void addBooleanField(String fieldName, boolean value){
        contentTable.add(fieldName).left();
        CheckBox box = new CheckBox("",getSkin());
        box.setChecked(value);
        booleanFields.put(fieldName, box);
        contentTable.add(box).left().expandX();
        contentTable.row();
        box.setUserObject(fieldName);
    }

    /**
     * Adds a string field
     * @param fieldName
     * @param value
     */
    protected void addStringField(String fieldName, String value){
        TextField txtField = new TextField(value, getSkin());
        txtField.setScale(0.5f);
        stringFields.put(fieldName, txtField);
        addRow(fieldName, txtField);
    }

    /**
     * Combobox
     * @param strings
     */
    protected void addStringList(String fieldName, Array<String> strings){
        SelectBox comboBox = new SelectBox(getSkin());
        comboBox.setItems(strings);
        addRow(fieldName,comboBox);
    }


    protected void addStringReadOnly(String fieldName, String value){
        contentTable.add(fieldName).left().padRight(4);
        Label txtField = new Label(value, getSkin());
        txtField.setScale(0.5f);
        contentTable.add(txtField).left().expandX();
        contentTable.row().padBottom(4).padTop(4);
    }

    protected void addEmptyRow(){
        addStringReadOnly("","");
    }

    private void addRow(String fieldName, Actor actor){
        contentTable.add(fieldName).left().padRight(4);
        contentTable.add(actor).left().expandX();
        contentTable.row().padBottom(4).padTop(4);
    }
}
