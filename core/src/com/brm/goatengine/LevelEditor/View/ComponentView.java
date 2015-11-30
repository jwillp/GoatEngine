package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Utils.Logger;

import java.lang.reflect.Field;

/**
 * Vie wof an Entity Component
 */
public class ComponentView extends Table{

    CheckBox checkBoxEnable;
    ImageButton btnRemove;
    Table contentTable;

    EntityComponent component;

    public ComponentView(EntityComponent component, Skin skin) {
        super(skin);
        this.component = component;
        initRootLayout();
        initContent();
        setDebug(true);

    }

    void initRootLayout(){
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


    private void initContent(){
        contentTable = new Table(getSkin());
        contentTable.left();


        readComponent();

        // Content
        /*contentTable.add("TEST LABEL");
        contentTable.row();

        contentTable.add("TEST LABEL");
        contentTable.row();

        contentTable.add("TEST LABEL");*/

        add(contentTable).colspan(2);
    }





    public void readComponent(){
        Field[] fields = component.getClass().getDeclaredFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            field.setAccessible(true);
            // Special cases
            if(field.getName().equals("ID")){
                continue;
            }


            try {
                if(field.getType().equals(boolean.class)){
                    addBoolean(field.getName(), field.getBoolean(component));
                }else{
                    addString(field.getName(), field.get(component).toString());
                }
            } catch (IllegalAccessException e) {
                Logger.error(e.getMessage());
                Logger.logStackTrace(e);
            }
        }
    }


    /**
     * Adds a boolean field
     * @param fieldName
     * @param value
     */
    private void addBoolean(String fieldName, boolean value){
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
    private void addString(String fieldName, String value){
        contentTable.add(fieldName).left();
        contentTable.add(new TextField(value, getSkin())).left().expandX();
        contentTable.row();
    }





}
