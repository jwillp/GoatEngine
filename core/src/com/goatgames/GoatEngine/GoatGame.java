package com.goatgames.goatengine;

import com.badlogic.gdx.Game;
import com.goatgames.goatengine.eventmanager.EventListener;
import com.goatgames.goatengine.eventmanager.engineevents.ScreenResizedEvent;


/**
 * Generic Game class used to test the engine
 */
public class GoatGame extends Game{

    private final EventListener gameSpecificListener;

    public GoatGame(EventListener gameSpecificListener) {
        this.gameSpecificListener = gameSpecificListener;
    }

    /**
     * Called when the {@link com.badlogic.gdx.Application} is first created.
     */
    @Override
    public void create() {
        GoatEngine.init();

        // NCB: Register game specific listener if any
        if(this.gameSpecificListener != null)
            GoatEngine.eventManager.register(this.gameSpecificListener);
    }

    @Override
    public void render() {
        GoatEngine.update();
    }

    @Override
    public void dispose() {
        GoatEngine.cleanUp();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        GoatEngine.eventManager.fireEvent(new ScreenResizedEvent(width, height));
    }
}
