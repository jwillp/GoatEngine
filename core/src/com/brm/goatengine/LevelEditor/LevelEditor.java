package com.brm.GoatEngine.LevelEditor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brm.GoatEngine.EventManager.GameEvent;
import com.brm.GoatEngine.EventManager.GameEventListener;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.LevelEditor.Events.ExitEditorEvent;
import com.brm.GoatEngine.LevelEditor.Events.ToggleConsoleEvent;
import com.brm.GoatEngine.LevelEditor.View.LevelEditorView;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Level Editor
 */
public class LevelEditor extends ChangeListener implements GameEventListener {

    // Copy of the ECS


    private LevelEditorView view;
    private boolean enabled; // Whether or not the Level Editor is enabled


    public LevelEditor(){
        super();
        Logger.info("Level Editor initialisation ... ");

        enabled = true;

        view = new LevelEditorView(this);

        GoatEngine.eventManager.registerListener(this);

        Logger.info("Level Editor initialised");
    }



    public void update(float delta){
        if(enabled)
            this.view.render(delta);
    }







    @Override
    public void onEvent(GameEvent e) {
        if(e instanceof ToggleConsoleEvent) {
            GoatEngine.console.toggle();
        }

        if(e instanceof  ExitEditorEvent){
            enabled = false;
        }
    }


    /**
     * @param event
     * @param actor The event target, which is the actor that emitted the change event.
     */
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        if(actor == view.getBtnConsole()){
            GoatEngine.console.toggle();
        }

        if(actor == view.getBtnQuit()){
            this.enabled = false;
        }

        if(actor == view.getBtnPlayPause()){
            if(GoatEngine.gameScreenManager.isRunning()){
                GoatEngine.gameScreenManager.pause();
            }else{
                GoatEngine.gameScreenManager.resume();
            }
        }
    }




    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
