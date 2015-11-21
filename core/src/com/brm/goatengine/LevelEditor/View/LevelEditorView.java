package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.LevelEditor.Events.ExitEditorEvent;
import com.brm.GoatEngine.LevelEditor.Events.ToggleConsoleEvent;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
import com.brm.GoatEngine.UI.UIEngine;

/**
 * GUI of the Level Editor
 */
public class LevelEditorView extends UIEngine {


    private final LevelEditor editor;

    // Toolbar
    Table toolbar;
    private TextButton btnQuit;
    private TextButton btnPlayPause;
    private TextButton btnConsole;
    private TextButton btnStats;
    private TextButton btnSaveChanges;
    private TextButton btnReloadScreen;
    private TextButton btnScreenSettings;
    private TextButton btnCreateEntity;


    public LevelEditorView(LevelEditor editor){
        super();
        this.editor = editor;


        initRootLayout();
        initToolbar();
    }


    public void initRootLayout(){
        Skin skin = new Skin(Gdx.files.internal("data/skins/default_skin/uiskin.json"));


        // Create Base Layout Border layout: NSEW
        toolbar = new Table();
        toolbar.setDebug(rootTable.getDebug());

        rootTable.setSkin(skin);
        rootTable.defaults().fill();
        rootTable.add(toolbar).colspan(3);
        rootTable.row();
        rootTable.add("west");
        rootTable.add("center").expand();
        rootTable.add("east");
        rootTable.row();
        rootTable.add("south").colspan(3);
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


        // Add Listener to buttons

        connectEditor(btnCreateEntity);
        connectEditor(btnConsole);
        connectEditor(btnStats);
        connectEditor(btnSaveChanges);
        connectEditor(btnPlayPause);
        connectEditor(btnReloadScreen);
        connectEditor(btnScreenSettings);
        connectEditor(btnQuit);
    }


    /**
     * Connects an actor with the editor
     * @param actor
     */
    private void connectEditor(Actor actor){
        actor.addListener(editor);
    }


    public void render(float delta){
        super.render(delta);
        // Update button
        btnPlayPause.setText(GoatEngine.gameScreenManager.isRunning() ? "Pause" : "Play");
    }


    public TextButton getBtnQuit() {
        return btnQuit;
    }

    public TextButton getBtnPlayPause() {
        return btnPlayPause;
    }

    public TextButton getBtnConsole() {
        return btnConsole;
    }

    public TextButton getBtnStats() {
        return btnStats;
    }

    public TextButton getBtnSaveChanges() {
        return btnSaveChanges;
    }

    public TextButton getBtnReloadScreen() {
        return btnReloadScreen;
    }

    public TextButton getBtnScreenSettings() {
        return btnScreenSettings;
    }

    public TextButton getBtnCreateEntity() {
        return btnCreateEntity;
    }
}
