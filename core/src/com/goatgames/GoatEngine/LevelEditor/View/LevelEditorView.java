package com.goatgames.goatengine.leveleditor.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.graphicsrendering.CameraComponent;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.goatgames.goatengine.ui.UIEngine;
import com.kotcrab.vis.ui.VisUI;

/**
 * GUI of the Level Editor
 */
public class LevelEditorView extends UIEngine {


    private final LevelEditor editor;

    private GameScreenConfigView configView;
    private ShapeRenderer shapeRenderer;

    private final int gridSize = 15;

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
    private TextButton btnEntityFromPrefab;

    private TextButton btnUndo;
    private TextButton btnRedo;


    // Stats bar
    Table statsBar;
    private Label labelScreenName;
    private Label labelFPS;
    private Label labelEntityCount;
    private Label labelNativeHeap;
    private Label labelJavaHeap;
    private Label labelCameraPosition;


    // Zoom Controls
    Table zoomCtrlTable;
    private TextButton btnZoomIn;
    private TextButton btnZoomOut;


    // Inspector
    private EntityInspector inspector;


    // Center
    private Table center;

    public LevelEditorView(LevelEditor editor){
        super();
        VisUI.load();
        this.editor = editor;
        shapeRenderer = new ShapeRenderer();
        this.rootTable.setDebug(false);
        initRootLayout();
        initToolbar();
        initInspector();
        initStatistics();
        initZoomControls();
    }

    public void initRootLayout(){
        //Skin skin = new Skin(Gdx.files.internal("data/skins/default_skin/uiskin.json"));
        Skin skin = VisUI.getSkin();
        configView = new GameScreenConfigView(skin);

        // Create Base Layout Border layout: NSEW
        toolbar = new Table();
        toolbar.setDebug(getRootTable().getDebug());


        statsBar = new Table();
        statsBar.setDebug(getRootTable().getDebug());


        inspector = new EntityInspector(skin);
        inspector.setDebug(getRootTable().getDebug());


        zoomCtrlTable = new Table(skin);
        zoomCtrlTable.setDebug(getRootTable().getDebug());

        center = new Table(skin);
        center.setDebug(getRootTable().getDebug());

        rootTable.setSkin(skin);
        rootTable.defaults().fill();
        rootTable.add(toolbar).colspan(3);
        rootTable.row();
        rootTable.add(zoomCtrlTable);

        //rootTable.add("center").expand().center();
        rootTable.add(center).expand().top();

        rootTable.add(inspector).padRight(10).width(300);
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
        btnCreateEntity = new TextButton("+", skin);
        btnEntityFromPrefab = new TextButton("Prefab", skin);

        btnUndo = new TextButton("Undo", skin);
        btnRedo = new TextButton("Redo", skin);


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
        tableLeft.defaults().padLeft(20);
        tableLeft.left();
        tableLeft.add(btnConsole);
        tableLeft.add(btnStats);
        tableLeft.add(btnSaveChanges);
        tableLeft.add(btnUndo);
        tableLeft.add(btnRedo);

        tableLeft.padLeft(20);

        // Center
        toolbar.add(tableCenter);
        tableCenter.center();
        tableCenter.add(btnCreateEntity);
        tableCenter.add(btnEntityFromPrefab).padLeft(20);
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
        connectEditor(btnEntityFromPrefab);
        connectEditor(btnConsole);
        connectEditor(btnStats);
        connectEditor(btnSaveChanges);
        connectEditor(btnPlayPause);
        connectEditor(btnReloadScreen);
        connectEditor(btnScreenSettings);
        connectEditor(btnQuit);
        connectEditor(btnUndo);
        connectEditor(btnRedo);
    }

    private void initZoomControls() {
        Skin skin = rootTable.getSkin();
        btnZoomIn = new TextButton("Zoom + ", skin);
        btnZoomOut = new TextButton("Zoom - ", skin);

        zoomCtrlTable.add(btnZoomIn).row();
        zoomCtrlTable.add(btnZoomOut).padTop(30);

        zoomCtrlTable.padLeft(30);

        //Connect buttons
        connectEditor(btnZoomIn);
        connectEditor(btnZoomOut);
    }

    private void initInspector() {
        inspector.setMovable(false);
    }

    private void initStatistics() {
        Skin skin = getRootTable().getSkin();
        labelScreenName = new Label(
                GoatEngine.gameScreenManager.getCurrentScreen().getName().replace(
                        GEConfig.ScreenManager.GAME_SCREEN_EXT,""), skin);
        labelFPS = new Label("", skin);
        labelEntityCount = new Label("", skin);

        labelNativeHeap = new Label("", skin);
        labelJavaHeap = new Label("", skin);
        labelCameraPosition = new Label("", skin);


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
        statsBar.row();

        statsBar.add("Java Heap: ");
        statsBar.add(labelJavaHeap);
        statsBar.row();

        statsBar.add("Native heap: ");
        statsBar.add(labelNativeHeap);
        statsBar.row();

        statsBar.add("Camera Position: ");
        statsBar.add(labelCameraPosition);
        statsBar.row();



        // Put statistics on
        statsBar.setVisible(false);
        toggleStatistics();

        // Do not display inspector right off
        this.inspector.clear();
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
        renderStats();

        // Grid
        drawGrid();

        // Selection rendering
        renderSelection();
    }


    // Optimize stats
    private static float byteToMega = 1.0f/1024/1024;
    private static int lastNativeHeap = (int) (Gdx.app.getNativeHeap() * byteToMega);
    private static int lastJavaHeap = (int) (Gdx.app.getNativeHeap() * byteToMega);

    private static Vector3 lastCamPos;

    private static int lastFps;

    private static int lastEntityCount = 0;

    public void renderStats(){

        if(!statsBar.isVisible()) return;

        // Update stats


        int entityCount = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().getEntityCount();
        if(lastEntityCount != entityCount){
            labelEntityCount.setText(Integer.toString(entityCount));
            lastEntityCount = entityCount;
        }
        int currentFps = Gdx.graphics.getFramesPerSecond();
        if(lastFps != currentFps){
            labelFPS.setText(Integer.toString(currentFps));
        }


        // Heaps
        int currentNativeHeap = (int) (Gdx.app.getNativeHeap()*byteToMega);
        if(lastNativeHeap != currentNativeHeap) {
            labelNativeHeap.setText(Integer.toString(currentNativeHeap) + "MB");
            lastNativeHeap = currentNativeHeap;
        }

        int currentJavaHeap = (int) (Gdx.app.getNativeHeap()*byteToMega);
        if(lastJavaHeap != currentJavaHeap) {
            labelJavaHeap.setText(Integer.toString(currentJavaHeap) + "MB");
            lastJavaHeap = currentJavaHeap;
        }


        // Camera position
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        if(!cam.getCamera().position.equals(lastCamPos)){
            labelCameraPosition.setText(cam.getCamera().position.toString());
            lastCamPos = cam.getCamera().position;
        }




    }



    public void toggleStatistics(){
        statsBar.setVisible(!statsBar.isVisible());
        btnStats.setText(statsBar.isVisible() ? "Stat OFF" : "Stats ON");
    }

    /**
     * Renders the selection rectangle
     * around the selected entity
     */
    private void renderSelection(){
        Entity selected = editor.getSelectedEntity();
        if(selected != null){
            EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
            if(!manager.entityExists(selected.getID())){
                return;
            }
            PhysicsComponent phys = (PhysicsComponent)selected.getComponent(PhysicsComponent.ID);


            CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);

            // Size of the rectangle
            float magnifierFactor = 1.5f; //55 / cam.getCamera().zoom;
            float sizeX = phys.getWidth() * 2 *  magnifierFactor;
            float sizeY = phys.getHeight() * 2 * magnifierFactor;

            shapeRenderer.setProjectionMatrix(cam.getCamera().combined  /*stage.getBatch().getProjectionMatrix()*/);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Or Filled
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(phys.getPosition().x-sizeX*0.5f, phys.getPosition().y-sizeY*0.5f, sizeX, sizeY);
            shapeRenderer.end();
        }

    }

    private void drawGrid(){
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        OrthographicCamera camera = cam.getCamera();

        int tileWidth = 2;
        int tileHeight = 2;
        int mapWidth = 300;
        int mapHeight = 300;
        int startX = -mapWidth;
        int startY = -mapHeight;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);
        for(int x = startX; x < mapWidth; x += tileWidth)
            shapeRenderer.line(x, startX, x, mapHeight);
        for(int y = startY; y < mapHeight; y += tileHeight)
            shapeRenderer.line(startY, y, mapWidth, y);
        shapeRenderer.end();
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


    public GameScreenConfigView getConfigView() {
        return configView;
    }

    public TextButton getBtnUndo() {
        return btnUndo;
    }

    public TextButton getBtnRedo() {
        return btnRedo;
    }

    public EntityInspector getInspector() {
        return inspector;
    }

    public TextButton getBtnZoomIn() {
        return btnZoomIn;
    }

    public TextButton getBtnZoomOut() {
        return btnZoomOut;
    }

    public TextButton getBtnEntityFromPrefab() {
        return btnEntityFromPrefab;
    }

    public Table getCenter() {
        return center;
    }

    public void setInspector(EntityInspector inspector) {
        this.inspector = inspector;
    }
}
