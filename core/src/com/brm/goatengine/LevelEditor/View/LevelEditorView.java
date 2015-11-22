package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.GoatEngine;
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


    // Stats bar
    Table statsBar;
    private Label labelScreenName;
    private Label labelFPS;
    private Label labelEntityCount;


    // Inspector
    private EntityInspector inspector;


    public LevelEditorView(LevelEditor editor){
        super();
        this.editor = editor;

        this.rootTable.setDebug(false);
        initRootLayout();
        initToolbar();
        initInspector();
        initStatistics();



    }

    public void initRootLayout(){
        Skin skin = new Skin(Gdx.files.internal("data/skins/default_skin/uiskin.json"));


        // Create Base Layout Border layout: NSEW
        toolbar = new Table();
        toolbar.setDebug(getRootTable().getDebug());


        statsBar = new Table();
        statsBar.setDebug(getRootTable().getDebug());


        inspector = new EntityInspector(skin);
        inspector.setDebug(getRootTable().getDebug());

        rootTable.setSkin(skin);
        rootTable.defaults().fill();
        rootTable.add(toolbar).colspan(3);
        rootTable.row();
        rootTable.add("west");


        //rootTable.add("center").expand().center();
        rootTable.add("center").expand().top();

        rootTable.add(inspector).padRight(10).width(275);
        //this.stage.addActor(inspector);
        rootTable.row();
        rootTable.add(statsBar).colspan(3);


    }

    public void initToolbar(){
        Skin skin = getRootTable().getSkin();

        btnConsole = new TextButton("Console",skin);
        btnStats = new TextButton("Stats",skin);
        btnSaveChanges = new TextButton("Save Changes",skin);
        btnPlayPause = new TextButton("Play", skin);
        btnReloadScreen = new TextButton("Reload Screen",skin);
        btnScreenSettings = new TextButton("Screen Settings",skin);
        btnQuit = new TextButton("Quit Editor", skin);
        btnCreateEntity = new TextButton(" + ", skin);

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

    private void initInspector() {
        Skin skin = rootTable.getSkin();
        //inspector.setMovable(false);



    }

    private void initStatistics() {
        Skin skin = getRootTable().getSkin();
        labelScreenName = new Label(
                GoatEngine.gameScreenManager.getCurrentScreen().getName().replace(
                        GEConfig.ScreenManager.GAME_SCREEN_EXT,""), skin);
        labelFPS = new Label(Integer.toString(Gdx.graphics.getFramesPerSecond()), skin);
        labelEntityCount = new Label(Integer.toString(0), skin);

        statsBar.top().left().padLeft(30).padBottom(30);
        statsBar.defaults().top().left();
        statsBar.setSkin(skin);
        statsBar.add("Game screen: ");
        statsBar.add(labelScreenName);
        statsBar.row();

        statsBar.add("FPS: ");
        statsBar.add(labelFPS);
        statsBar.row();

        statsBar.add("Entity count: ");
        statsBar.add(labelEntityCount);

        // Put statistics on
        statsBar.setVisible(false);
        toggleStatistics();


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
        // Must be updated every frame because the value can change from external source
        btnPlayPause.setText(GoatEngine.gameScreenManager.isRunning() ? "Pause" : "Play");


        // Update stats
        int entityCount = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().getEntityCount();
        labelEntityCount.setText(Integer.toString(entityCount));
        labelFPS.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));

    }



    public void toggleStatistics(){
        statsBar.setVisible(!statsBar.isVisible());
        btnStats.setText(statsBar.isVisible() ? "Stat OFF" : "Stats ON");
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
