package com.goatgames.goatengine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.goatgames.goatengine.GoatEngine;
import com.kotcrab.vis.ui.VisUI;

/**
 * Responsible for handling UI for a Game Screen
 */
public class UIEngine {

    protected Stage stage;
    protected Table rootTable;


    public UIEngine(){
        stage = new Stage();
        GoatEngine.inputManager.addInputProcessor(stage);
        VisUI.load();
        rootTable = new Table(VisUI.getSkin());
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // TODO move value in GEConfig
        // This is optional, but enables debug lines for tables.
        rootTable.setDebug(GoatEngine.config.getBoolean("ui.debug"));
        rootTable.top();
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
