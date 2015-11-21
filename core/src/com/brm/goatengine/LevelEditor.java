package com.brm.GoatEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.brm.GoatEngine.UI.UIEngine;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Level Editor
 */
public class LevelEditor extends UIEngine{

    // Copy of the ECS



    // Toolbar
    Table toolbar;
    TextButton btnQuit;
    TextButton btnPlayPause;
    TextButton btnConsole;
    TextButton btnStats;
    TextButton btnSaveChanges;
    TextButton btnReloadScreen;
    TextButton btnScreenSettings;
    TextButton btnCreateEntity;


    public LevelEditor(){
        super();
        Logger.info("Level Editor initialisation ... ");
        Skin skin = new Skin(Gdx.files.internal("data/skins/default_skin/uiskin.json"));

        // Create Base Layout
        toolbar = new Table();
        toolbar.setDebug(true);


        rootTable.setSkin(skin);
        rootTable.defaults().fill();
        rootTable.add(toolbar).colspan(3);
        rootTable.row();
        rootTable.add("west");
        rootTable.add("center").expand();
        rootTable.add("east");
        rootTable.row();
        rootTable.add("south").colspan(3);





        // Toolbar
        initToolbar();


        Logger.info("Level Editor initialised");
    }


    public void initToolbar(){
        Skin skin = rootTable.getSkin();

        btnConsole = new TextButton("Console",skin);
        btnStats = new TextButton("Stats",skin);
        btnSaveChanges = new TextButton("Save Changes",skin);
        btnPlayPause = new TextButton("Play", skin);
        btnReloadScreen = new TextButton("Reload Screen",skin);
        btnScreenSettings = new TextButton("Screen Settings",skin);
        btnQuit = new TextButton("Quit Editor", skin);
        btnCreateEntity = new TextButton("+", skin);

        toolbar.defaults().fill().expandX();

        toolbar.left();
        toolbar.padTop(20);
        toolbar.padBottom(20);

        // The tool bar is made out of three tables
        // | []  | [] | [] |

        Table tableLeft = new Table(skin);
        Table tableCenter  = new Table(skin);
        Table tableRight  = new Table(skin);

        // Left
        toolbar.add(tableLeft);
        tableLeft.left();
        tableLeft.add(btnConsole);
        tableLeft.add(btnStats).padLeft(20);
        tableLeft.add(btnSaveChanges).padLeft(20);

        tableLeft.padLeft(20);

        // Center
        toolbar.add(tableCenter);
        tableCenter.center();
        tableCenter.add(btnCreateEntity);
        tableCenter.add(btnPlayPause).padLeft(20);
        tableCenter.add(btnReloadScreen).padLeft(20);

        //Right
        toolbar.add(tableRight);
        tableRight.right();
        tableRight.add(btnScreenSettings).padLeft(20);
        tableRight.add(btnQuit).padLeft(20);

        tableRight.padRight(20);


     }





    public void render(float delta){
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        /*btnQuit.setX(0 + width * 0.005f);
        btnQuit.setY(height - height * 0.05f);*/



        super.render(delta);

    }

}
