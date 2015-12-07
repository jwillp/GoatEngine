package com.brm.GoatEngine.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.brm.GoatEngine.GoatEngine;

/**
 * Responsible for handling UI
 */
public class UIEngine {

    protected Stage stage;
    protected Table rootTable;


    public UIEngine(){
        stage = new Stage();
        GoatEngine.inputManager.addInputProcessor(stage);

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // TODO move value in GEConfig
        rootTable.setDebug(true); // This is optional, but enables debug lines for tables.

        // Add widgets to the rootTable here.
    }


    public void render(float delta){
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        stage.getViewport().setWorldSize(width, height);
        stage.getViewport().update(width, height, true);

        stage.act(delta);
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        GoatEngine.inputManager.removeInputProcessor(stage);
    }

    public Table getRootTable() {
        return rootTable;
    }

    public Stage getStage() {
        return stage;
    }
}
