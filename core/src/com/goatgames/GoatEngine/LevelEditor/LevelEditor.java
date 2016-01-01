package com.goatgames.goatengine.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.input.events.KeyPressedEvent;
import com.goatgames.goatengine.input.events.MouseClickEvent;
import com.goatgames.goatengine.input.events.MouseDragEvent;
import com.goatgames.goatengine.input.events.MouseScrolledEvent;
import com.goatgames.goatengine.leveleditor.view.EntityInspector;
import com.goatgames.goatengine.leveleditor.view.GameScreenConfigView;
import com.goatgames.goatengine.leveleditor.view.LevelEditorView;
import com.goatgames.goatengine.utils.Logger;
import com.goatgames.goatengine.leveleditor.commands.*;
import com.kotcrab.vis.ui.VisUI;

import java.util.Stack;

/**
 * Level Editor
 */
public class LevelEditor extends ChangeListener implements GameEventListener {

    // Copy of the ECS

    private LevelEditorView view;
    private boolean enabled = false; // Whether or not the Level Editor is enabled



    // Undo Stack
    private Stack<UndoCommand> undoStack;
    private Stack<UndoCommand> redoStack;
    private Entity selectedEntity;


    public LevelEditor(){
        super();
        Logger.info("Level Editor initialisation ... ");
        undoStack = new Stack<UndoCommand>();
        redoStack = new Stack<UndoCommand>();

        view = new LevelEditorView(this);

        GoatEngine.eventManager.registerListener(this);

        Logger.info("Level Editor initialised");
    }



    public void update(float delta){
        if(enabled) {
            this.view.render(delta);
            if(selectedEntity != null)
                this.view.getInspector().inspectEntity(selectedEntity);
            else
                this.view.getInspector().clear();
        }else{
            view.renderStats(); // Always render stats
        }
    }





    /**
     * @param event
     * @param actor The event target, which is the actor that emitted the change event.
     */
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        // CONSOLE
        if(actor == view.getBtnConsole()){
            executeCommand(new ToggleConsoleCommand());
            return;
        }

        // PLAY PAUSE
        if(actor == view.getBtnPlayPause()){
            executeCommand(new TogglePlayPauseCommand());
            return;
        }

        // STATS
        if(actor == view.getBtnStats()){
            executeCommand(new ToggleStatisticsCommand(this));
            return;
        }

        if(actor == view.getBtnScreenSettings()){
            view.getCenter().addActor(new GameScreenConfigView(view.getBtnConsole().getSkin()));
        }


        // QUIT
        if(actor == view.getBtnQuit()){
            executeCommand(new QuitEditorCommand(this));
            return;
        }



        /// Zoom In & Out Command
        if(actor == view.getBtnZoomIn()){
            executeCommand(new ZoomCameraCommand(ZoomCameraCommand.Mode.IN));
            return;
        }

        if(actor == view.getBtnZoomOut()){
            executeCommand(new ZoomCameraCommand(ZoomCameraCommand.Mode.OUT));
            return;
        }

        if(actor == view.getBtnSaveChanges()){
            executeCommand(new SaveLevelCommand(this));
        }

        // UNDOABLE COMMANDS
        // CREATE ENTITY
        if(actor == view.getBtnCreateEntity()){
            executeCommand(new CreateEntityCommand());
            return;
        }

        if(actor == view.getBtnEntityFromPrefab()){
            executeCommand(new CreateEntityFromPrefabCommand(this));
            return;
        }

        if(actor == view.getBtnUndo()){
            this.undo();
            return;
        }

        if(actor == view.getBtnRedo()){
            this.redo();
            return;
        }
    }


    /**
     * Executes a given command, if the command is undoable it is added to the undo stack
     * @param command
     */
    public void executeCommand(EditorCommand command){
        if (command instanceof UndoCommand){
            undoStack.add((UndoCommand)command);
        }
        command.exec();
    }

    /**
     * Undo the last undoable command
     */
    public void undo(){
        if(undoStack.isEmpty())
            return;
        UndoCommand c = undoStack.pop();
        c.undo();
        redoStack.add(c);
    }


    /**
     * Redo the last command
     */
    public void redo(){
        if(redoStack.isEmpty())
            return;
        UndoCommand c = redoStack.pop();
        c.redo();
        undoStack.add(c);
    }





    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        // When opening the editor pause the game
        if(enabled){
            GoatEngine.gameScreenManager.pause();
        }else{
            // when closing the editor resume the game
            GoatEngine.gameScreenManager.resume();
        }
    }

    public LevelEditorView getView() {
        return view;
    }

    @Override
    public void onEvent(GameEvent e) {

        // is the current screen ready to capture events?
        if(!GoatEngine.gameScreenManager.getCurrentScreen().isInitialized()){
            return;
        }


        if(e.isOfType(MouseClickEvent.class)){
            onMouseClick((MouseClickEvent) e);
            return;
        }

        if(e.isOfType(KeyPressedEvent.class)){
            onKeyPressed((KeyPressedEvent) e);
            return;
        }



        if(e.isOfType(MouseDragEvent.class)){
            onMouseDrag((MouseDragEvent) e);
            return;
        }

        if(e.isOfType(MouseScrolledEvent.class)){
            onMouseSrcoll((MouseScrolledEvent) e);
            return;
        }


    }

    private void onMouseDrag(MouseDragEvent e) {
        if(!enabled) return;
        // If mouse drag + space pressed = moving camera
        boolean spaceMouseCtrl = GoatEngine.inputManager.getKeyboardInputManager().isKeyPressed(Input.Keys.SPACE);
        boolean middleClickMouseCtrl = Gdx.input.isButtonPressed(Input.Buttons.MIDDLE);
        if(middleClickMouseCtrl || spaceMouseCtrl){
            executeCommand(new MoveDragCameraCommand(e.screenX, e.screenY, e.lastScreenX, e.lastScreenY));
        }else{
            // Move currently selected entity (if any)
            if(selectedEntity != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                executeCommand(new MoveEntityDragCommand(selectedEntity, e.screenX, e.screenY));
        }
    }


    private void onMouseSrcoll(MouseScrolledEvent e){
        if(!enabled) return;
        if(e.amount == 0) return;
        ZoomCameraCommand.Mode mode = e.amount > 0 ? ZoomCameraCommand.Mode.OUT : ZoomCameraCommand.Mode.IN;
        executeCommand(new ZoomCameraCommand(mode));
    }


    private void onKeyPressed(KeyPressedEvent e) {
        if(e.getKey() == Input.Keys.F2){
            this.setEnabled(!isEnabled());
            return;
        }

                             // Remove Entity
        if(e.getKey() == Input.Keys.DEL && selectedEntity != null){
            executeCommand(new DeleteEntityCommand(selectedEntity, this));
        }
    }


    private void onMouseClick(MouseClickEvent event){
        if(!enabled) return;
        executeCommand(new SelectEntityAtPositionCommand(event.screenX,event.screenY, this));
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Entity selectedEntity) {
        // Free old entity
        if(this.selectedEntity != null) {
            GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().freeEntity(this.selectedEntity);
        }
        //this.getView().setInspector(new EntityInspector(VisUI.getSkin()));
        this.selectedEntity = selectedEntity;

    }

    public void clean(){
        VisUI.dispose();
    }

}
