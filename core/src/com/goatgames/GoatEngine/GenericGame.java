package com.goatgames.goatengine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.eventmanager.engineevents.EngineEvents;
import com.goatgames.goatengine.input.events.ControllerConnectedEvent;
import com.goatgames.goatengine.utils.DesktopExceptionHandler;
import com.goatgames.goatengine.utils.Logger;




/**
 * Generic Game class used to test the engine
 */
public class GenericGame extends Game{
    /**
     * Called when the {@link com.badlogic.gdx.Application} is first created.
     */
    @Override
    public void create() {
        // Set ExceptionHandler
        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
            Thread.setDefaultUncaughtExceptionHandler(new DesktopExceptionHandler());
        }

        // Load configuration
        GEConfig.loadConfig();

        Gdx.graphics.setTitle(GEConfig.DevGeneral.GAME_NAME);
        Gdx.graphics.setDisplayMode(
                GEConfig.DevGeneral.VIEWPORT_WIDTH,
                GEConfig.DevGeneral.VIEWPORT_HEIGHT,
                GEConfig.DevGeneral.FULLSCREEN);

        GoatEngine.init();
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
        GoatEngine.eventManager.fireEvent(new EngineEvents.ScreenResizedEvent(width, height));
    }
}
