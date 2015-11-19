package com.brm.GoatEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.brm.GoatEngine.UI.UIEngine;

/**
 * Level Editor
 */
public class LevelEditor extends UIEngine{

    // Copy of the ECS


    // Widgets
    TextButton btnQuit;
    TextButton btnSave;
    TextButton btnDiscardChanges;


    public LevelEditor(){
        Skin skin = new Skin(Gdx.files.internal("data/skins/default_skin/uiskin.json"));

        btnQuit = new TextButton("Quit Editor", skin);
        btnSave = new TextButton("Save", skin);
        btnDiscardChanges = new TextButton("Discard Changes", skin);



        table.add(btnQuit).padLeft(10);
        table.add(btnSave).padLeft(10);
        table.add(btnDiscardChanges).padLeft(10);
    }



    public void render(float delta){
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        /*btnQuit.setX(0 + width * 0.005f);
        btnQuit.setY(height - height * 0.05f);*/



        super.render(delta);

    }

}
