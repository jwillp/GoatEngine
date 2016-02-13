package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.screenmanager.GameScreenConfig;
import com.goatgames.goatengine.utils.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A Window enabling the user to change the values
 * of a game screen
 */
public class GameScreenConfigView extends Window{

    // MODEL
    GameScreenConfig config;

    // VIEW
    private Table root;
    private TextButton btnSave;


    protected HashMap<String, CheckBox> booleanFields = new  HashMap<String,CheckBox>();
    protected HashMap<String, TextField> stringFields = new  HashMap<String, TextField>();



    public GameScreenConfigView(Skin skin) {
        super("Game Screen Config", skin);
        root = new Table(skin);
        add(root);

        /*root.setDebug(true);*/
        setFillParent(true);

        root.defaults().padBottom(5);

        readScreen();

        btnSave = new TextButton("Save", skin);
        root.add(btnSave).colspan(2).fill().expandX();



        this.setWidth(5);
        this.setHeight(5);



        /// Connect Button
        this.btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSave();
            }
        });

    }

    private void onSave() {
        //Strings
        for(String key: this.stringFields.keySet()){
            try {
                config.getClass().getField(key).setAccessible(true);
                String value = this.stringFields.get(key).getText();
                Field field = config.getClass().getField(key);
                if(field.getGenericType() == float.class)
                    field.set(config, Float.parseFloat(value));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Booleans
        for(String key: this.booleanFields.keySet()){
            try {
                config.getClass().getField(key).setAccessible(true);
                config.getClass().getField(key).set(config, this.booleanFields.get(key).isChecked());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        // Update Gravity
        /*float configX = GoatEngine.gameScreenManager.getCurrentScreen().getConfig().GRAVITY_X;
        float configY = GoatEngine.gameScreenManager.getCurrentScreen().getConfig().GRAVITY_Y;
        GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld().setGravity(
                new Vector2(configX, configY)
        );*/
        this.remove();
    }




    public void readScreen(){
        config = GoatEngine.gameScreenManager.getCurrentScreen().getConfig();
        Field[] fields = config.getClass().getFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            try {
                if(field.getType().equals(boolean.class)){
                    addBooleanField(field.getName(), field.getBoolean(config));
                }else{
                    addStringField(field.getName(), field.get(config).toString());
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
    protected CheckBox addBooleanField(String fieldName, boolean value){
        root.add(fieldName).left();
        CheckBox box = new CheckBox("",getSkin());
        box.setChecked(value);
        booleanFields.put(fieldName, box);
        root.add(box).left().expandX();
        root.row();
        box.setUserObject(fieldName);
        return box;
    }

    /**
     * Adds a string field
     * @param fieldName
     * @param value
     */
    protected TextField addStringField(String fieldName, String value){
        TextField txtField = new TextField(value, getSkin());
        txtField.setScale(0.5f);
        stringFields.put(fieldName, txtField);
        addRow(fieldName, txtField);
        return txtField;
    }

    private void addRow(String fieldName, Actor actor){
        root.add(fieldName).left().padRight(4);
        root.add(actor).left().expandX();
        root.row().padBottom(4).padTop(4);
    }

}
