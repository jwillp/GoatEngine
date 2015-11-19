package com.brm.GoatEngine.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Responsible for handling UI
 */
public class UIEngine {

    private Stage stage;
    private Table table;


    public UIEngine(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        GoatEngine.console.resetInputProcessing();

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // TODO move value in GEConfig
        table.setDebug(true); // This is optional, but enables debug lines for tables.

        // Add widgets to the table here.
    }

    public UIEngine addWidget(Actor w){
        table.add(w);
        return this;
    }

    public void render(float delta){
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        stage.getViewport().setWorldSize(width, height);
        stage.getViewport().update(width, height, true);

        stage.act(delta);
        stage.draw();
    }


}
