package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.brm.GoatEngine.ECS.core.EntityComponent;

/**
 * Vie wof an Entity Component
 */
public class ComponentView extends Window{

    Table root;
    TextButton btnRemove;
    Table contentTable;

    EntityComponent component;

    public ComponentView(String v, Skin skin) {
        super("Dynamic Camera" + v, skin);
        initRootLayout();
        initContent();
        setMovable(false);
    }

    void initRootLayout(){
        Skin skin =  getSkin();

        top().left();
        root = new Table(this.getSkin());
        root.setDebug(this.getDebug());
        add(root).fill().expandX().padTop(5);

        root.top().center();
        root.defaults().top().left().expandX();


        // Header
        btnRemove = new TextButton(" - ", skin);
        root.add(btnRemove).top().right();
        row();

    }


    private void initContent(){
        contentTable = new Table(getSkin());
        // Content
        contentTable.add("TEST LABEL").row();
        contentTable.add("TEST LABEL").row();
        contentTable.add("TEST LABEL").row();
        contentTable.add("TEST LABEL");


        add(contentTable);
    }



}
