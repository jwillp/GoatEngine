package com.goatgames.goatengine.screenmanager;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.EntitySystemManager;
import com.goatgames.goatengine.ui.UIEngine;

/**
 * Game Screen interface
 */
public interface IGameScreen {

    /**
     * Cleans up the screen
     */
    void cleanUp();

    /**
     * Initialises the game screen
     * @param screenManager Game Screen manager of the screen
     */
    void init(GameScreenManager screenManager);

    /**
     * Pauses the screen until it resumes
     * @param screenManager
     */
    void pause(GameScreenManager screenManager);

    /**
     * Resumes the game screen after it had paused
     * @param screenManager manager of the screen
     */
    void resume(GameScreenManager screenManager);

    /**
     * Pre Update call of the game loop
     * @param screenManager manager of the screen
     */
    void preUpdate(GameScreenManager screenManager);

    /**
     * Render call of the game loop
     * @param screenManager manager of the screen
     * @param deltaTime delta time
     */
    void draw(GameScreenManager screenManager, float deltaTime);

    /**
     * Update logic call of the game loop
     * @param screenManager
     * @param deltatTime
     */
    void update(GameScreenManager screenManager, float deltatTime);

    /**
     * Returns the config of the game screen
     * @return config of the screen
     */
    IGameScreenConfig getConfig();

    /**
     * Returns the entity manager of the screen
     * @return the entity manager of the screen
     */
    EntityManager getEntityManager();

    /**
     * Returns the entity system manager of the screen
     * @return entity system manager of the screen
     */
    EntitySystemManager getEntitySystemManager();

    /**
     * Returns a string representing the name of the screen
     * @return the name of the screen
     */
    String getName();

    /**
     * Indicates if the screen is initialised
     * @return true if the screen is initialised, otherwise false.
     */
    boolean isInitialised();

    /**
     * Returns the UI Engine of the screen
     * @return UI Engine of the screen
     */
    UIEngine getUIEngine();

    public class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super(String.format("Could not find game screen : %s File not found : %s%s",
                    name, GoatEngine.config.getString("screens.directory"), name));
        }
    }
}
