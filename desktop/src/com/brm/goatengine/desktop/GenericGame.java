package com.brm.goatengine.desktop;

import com.badlogic.gdx.Game;
import com.brm.GoatEngine.EventManager.GameEvent;
import com.brm.GoatEngine.EventManager.GameEventListener;
import com.brm.GoatEngine.GoatEngine;
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
        Logger.debug(e.toString());
    }


    @Override
    public void render() {
        super.render();
        GoatEngine.update();
    }

    @Override
    public void dispose() {
        GoatEngine.cleanUp();
    }
}
