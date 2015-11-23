package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.ScreenManager.GameScreenConfig;
import com.brm.GoatEngine.Utils.Logger;

import java.lang.reflect.Field;

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

    public GameScreenConfigView(Skin skin) {
        super("Game Screen Config", skin);
        root = new Table(skin);
        add(root);

        root.setDebug(true);
        setFillParent(true);

        root.defaults().padBottom(5);

        readScreen();

        btnSave = new TextButton("Save", skin);
        root.add(btnSave).colspan(2).fill().expandX();


    }

    public void readScreen(){
        config = GoatEngine.gameScreenManager.getCurrentScreen().getConfig();
        Field[] fields = config.getClass().getFields();
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            try {
                if(field.getType().equals(boolean.class)){
                        addBoolean(field.getName(), field.getBoolean(config));
                }else{
                    addString(field.getName(), field.get(config).toString());
                }
            } catch (IllegalAccessException e) {
                Logger.error(e.getMessage());
                Logger.logStackTrace(e);
            }
        }
    }





    public void addBoolean(String fieldName, boolean value){
        root.add(fieldName).left();
        CheckBox box = new CheckBox("",getSkin());
        box.setChecked(value);
        root.add(box).left();
        root.row();
        box.setUserObject(fieldName);
    }

    public void addString(String fieldName, String value){
        root.add(fieldName).left();
        root.add(new TextField(value, getSkin())).left();
        root.row();
    }


}
