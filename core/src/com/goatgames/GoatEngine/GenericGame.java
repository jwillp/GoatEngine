package com.goatgames.goatengine;

import com.badlogic.gdx.Game;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.input.Events.ControllerConnectedEvent;
import com.goatgames.goatengine.utils.Logger;

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
            Logger.debug("Controller Connected");
        }
    }


    @Override
    public void render() {
        try{
            GoatEngine.update();
        }catch (RuntimeException e){
            Logger.fatal("AN ERROR OCCURRED");
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
