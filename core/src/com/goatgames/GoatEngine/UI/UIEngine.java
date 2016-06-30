package com.goatgames.goatengine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.goatgames.goatengine.GoatEngine;

/**
 * Responsible for handling UI for a Game Screen
 */
public class UIEngine {

    protected Stage stage;
    protected Table rootTable;


    public UIEngine(){
        stage = new Stage(new ScreenViewport());
        GoatEngine.inputManager.addInputProcessor(stage);

        UIBuilder builder = new UIBuilder();
        UIManager manager = builder.build("H:/home/Dev/GoatEngine/local/ui.uiml.twig");
        rootTable = manager.getRoot();
        rootTable.setFillParent(true);


        // TODO move value in GEConfig
        rootTable.setDebug(true); // This is optional, but enables debug lines for tables.

        stage.addActor(rootTable);

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
