package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Utils.Logger;

import java.lang.reflect.Field;

/**
 * Vie wof an Entity Component
 */
public abstract class ComponentView extends Table{


    EntityComponent component;

    protected CheckBox checkBoxEnable;
    ImageButton btnRemove;
    Table contentTable;
    TextButton btnApply;


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

    private void initRootLayout(){
        Skin skin =  getSkin();


        top().center();
        defaults().top().left().expandX();


        // Header [enable] Component name [remove]
        checkBoxEnable = new CheckBox(component.getId(), skin);
        checkBoxEnable.setChecked(component.isEnabled());
        add(checkBoxEnable);

        btnRemove = new ImageButton(skin, "minus");
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
    }





    /**
     * Adds a boolean field
     * @param fieldName
     * @param value
     */
    protected void addBoolean(String fieldName, boolean value){
        contentTable.add(fieldName).left();
        CheckBox box = new CheckBox("",getSkin());
        box.setChecked(value);
        contentTable.add(box).left().expandX();
        contentTable.row();
        box.setUserObject(fieldName);
    }

    /**
     * Adds a string field
     * @param fieldName
     * @param value
     */
    protected void addString(String fieldName, String value){
        contentTable.add(fieldName).left().padRight(4);
        TextField txtField = new TextField(value, getSkin());
        txtField.setScale(0.5f);
        contentTable.add(txtField).left().expandX();
        contentTable.row().padBottom(4).padTop(4);
    }





}
