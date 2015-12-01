package com.brm.GoatEngine.LevelEditor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.EventManager.GameEvent;
import com.brm.GoatEngine.EventManager.GameEventListener;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Input.Events.KeyPressedEvent;
import com.brm.GoatEngine.Input.Events.MouseClickEvent;
import com.brm.GoatEngine.Input.Events.MouseDragEvent;
import com.brm.GoatEngine.LevelEditor.Commands.*;
import com.brm.GoatEngine.LevelEditor.Components.EditorLabelComponent;
import com.brm.GoatEngine.LevelEditor.View.GameScreenConfigView;
import com.brm.GoatEngine.LevelEditor.View.LevelEditorView;
import com.brm.GoatEngine.Rendering.CameraComponent;
import com.brm.GoatEngine.Utils.Logger;


import java.util.Stack;

/**
 * Level Editor
 */
public class LevelEditor extends ChangeListener implements GameEventListener{

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
        if(enabled)
            this.view.render(delta);
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
            view.getStage().addActor(new GameScreenConfigView(view.getBtnConsole().getSkin()));
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

        // UNDOABLE COMMANDS
        // CREATE ENTITY
        if(actor == view.getBtnCreateEntity()){
            executeCommand(new CreateEntityCommand());
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



    }

    private void onMouseDrag(MouseDragEvent e) {
        // If mouse drag + space pressed = moving camera
        if(GoatEngine.inputManager.getKeyboardInputManager().isKeyPressed(Input.Keys.SPACE)){
            executeCommand(new MoveDragCameraCommand(e.screenX, e.screenY, e.lastScreenX, e.lastScreenY));
        }else{
            // Move currently selected entity (if any)
            if(selectedEntity != null)
                executeCommand(new MoveEntityDragCommand(selectedEntity, e.screenX, e.screenY));
        }
    }

    private void onKeyPressed(KeyPressedEvent e) {
        if(e.getKey() == Input.Keys.F2){
            this.setEnabled(!isEnabled());
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
        this.selectedEntity = selectedEntity;
    }
}
