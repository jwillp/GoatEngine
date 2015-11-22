package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.ECS.core.EntityComponent;

/**
 * Vie wof an Entity Component
 */
public class ComponentView extends Table{

    CheckBox checkBoxEnable;
    TextButton btnRemove;
    Table contentTable;

    EntityComponent component;

    public ComponentView(String v, Skin skin) {
        super(skin);
        initRootLayout();
        initContent();
        setDebug(true);

    }

    void initRootLayout(){
        Skin skin =  getSkin();


        top().center();
        defaults().top().left().expandX();


        // Header [enable] Component name [remove]
        checkBoxEnable = new CheckBox("Dynamic Camera", skin);
        checkBoxEnable.setChecked(false);
        add(checkBoxEnable);


        btnRemove = new TextButton(" - ", skin);
        add(btnRemove).top().right();

        row();

    }


    private void initContent(){
        contentTable = new Table(getSkin());
        contentTable.left();
        // Content
        contentTable.add("TEST LABEL");
        contentTable.row();

        contentTable.add("TEST LABEL");
        contentTable.row();

        contentTable.add("TEST LABEL");

        add(contentTable).colspan(2);
    }



}
