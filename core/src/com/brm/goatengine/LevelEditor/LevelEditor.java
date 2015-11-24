package com.brm.GoatEngine.LevelEditor;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.EventManager.GameEvent;
import com.brm.GoatEngine.EventManager.GameEventListener;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Input.Events.MouseReleasedEvent;
import com.brm.GoatEngine.LevelEditor.Commands.*;
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


        // UNDOABLE COMMANDS
        // CREATE ENTITY
        if(actor == view.getBtnCreateEntity()){
            executeCommand(new CreateEntityCommand());
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
        if(e.isOfType(MouseReleasedEvent.class)){
            onMouseClick((MouseReleasedEvent) e);
        }
    }


    private void onMouseClick(MouseReleasedEvent event){
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        final Vector3 pos = new Vector3();
        // translate the mouse coordinates to world coordinates
        cam.getCamera().unproject(pos.set(event.screenX, event.screenY, 0));

        // ask the world which bodies are within the given
        // bounding box around the mouse pointer
        World world = GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld();
        final Body[] hitBody = {null};
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                // if the hit point is inside the fixture of the body
                // we report it
                if (fixture.testPoint(pos.x, pos.y)) {
                    hitBody[0] = fixture.getBody();
                    return false;
                } else
                    return true;

            }
        }, pos.x - 0.0001f, pos.y - 0.0001f, pos.x + 0.0001f, pos.y + 0.0001f);

        // If we hit something
        if (hitBody != null) {

        }



    }



}
