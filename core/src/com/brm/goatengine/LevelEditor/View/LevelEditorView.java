package com.brm.GoatEngine.LevelEditor.View;

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
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.GraphicsRendering.CameraComponent;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
import com.brm.GoatEngine.UI.UIEngine;
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
        int entityCount = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().getEntityCount();
        labelEntityCount.setText(Integer.toString(entityCount));
        labelFPS.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));

        labelNativeHeap.setText(Long.toString(Gdx.app.getNativeHeap()/1024/1024) + "MB");
        labelJavaHeap.setText(Long.toString(Gdx.app.getJavaHeap()/1024/1024) + "MB");

        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        labelCameraPosition.setText(cam.getCamera().position.toString());

        // Selection rendering
        renderSelection();
        // Grid
        // drawGrid();
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
            // Position of the rectangle
            Vector3 highlightPos = new Vector3(phys.getPosition().x, phys.getPosition().y, 0);


            CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);

            // Translate the rectangle's world coordinates to camera coordinates
            cam.getCamera().project(highlightPos);

            // Size of the rectangle
            float magnifierFactor = 55 / cam.getCamera().zoom;
            float sizeX = phys.getWidth() * magnifierFactor;
            float sizeY = phys.getHeight() * magnifierFactor;


            shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Or Filled
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(highlightPos.x-sizeX*0.5f, highlightPos.y-sizeY*0.5f, sizeX, sizeY);
            shapeRenderer.end();
        }

    }

    private void drawGrid(){

       // Gdx.gl.glLineWidth(1);
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        OrthographicCamera camera = cam.getCamera();

        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);

        drawVerticalLines();
        drawHorizontalLines();

        shapeRenderer.end();

    }



    private void drawVerticalLines () {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        OrthographicCamera camera = cam.getCamera();
        float xStart = camera.position.x - camera.viewportWidth / 2;
        float xEnd = xStart + camera.viewportWidth;

        float leftDownY = (camera.position.y - camera.viewportHeight / 2);
        float linesToDraw = (camera.viewportHeight / gridSize) + 10;

        float drawingPointStart = leftDownY / gridSize;
        float drawingPointEnd = drawingPointStart + linesToDraw;

        for (int i = MathUtils.round(drawingPointStart); i < MathUtils.round(drawingPointEnd); i++)
            shapeRenderer.line(xStart, i * gridSize, xEnd, i * gridSize);
    }

    private void drawHorizontalLines () {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        OrthographicCamera camera = cam.getCamera();
        float yStart = camera.position.y - camera.viewportHeight / 2;
        float yEnd = yStart + camera.viewportHeight;

        float leftDownX = (camera.position.y - camera.viewportWidth / 2);
        float linesToDraw = (camera.viewportWidth / gridSize) + 10;

        float drawingPointStart = leftDownX / gridSize;
        float drawingPointEnd = drawingPointStart + linesToDraw;

        for (int i = MathUtils.round(drawingPointStart); i < MathUtils.round(drawingPointEnd); i++)
            shapeRenderer.line(i * gridSize, yStart, i * gridSize, yEnd);
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
}
