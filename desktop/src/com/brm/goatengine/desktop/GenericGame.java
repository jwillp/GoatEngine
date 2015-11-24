package com.brm.GoatEngine.desktop;

import com.badlogic.gdx.Game;
import com.brm.GoatEngine.EventManager.GameEvent;
import com.brm.GoatEngine.EventManager.GameEventListener;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Input.Events.ControllerConnectedEvent;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Generic Game class used to test the engine
 */
public class GenericGame extends Game implements GameEventListener{
    /**
     * Called when the {@link com.badlogic.gdx.Application} is first created.
     */
    @Override
    public void create() {
        GoatEngine.init();
        GoatEngine.eventManager.registerListener(this);


    }

    @Override
    public void onEvent(GameEvent e) {
        if(e.isOfType(ControllerConnectedEvent.class)){
            Logger.debug("Contoller Connected");
        }
    }


    @Override
    public void render() {
        super.render();
        try{
            GoatEngine.update();
        }catch (RuntimeException e){
            Logger.fatal("AN ERROR OCCURED");
            Logger.fatal(e.getMessage());
            Logger.logStackTrace(e);
            throw e;
        }
    }

    @Override
    public void dispose() {
        GoatEngine.cleanUp();
    }
}
