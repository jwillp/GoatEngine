package com.goatgames.goatengine.screenmanager;

/**
 * Interface for Game Screen Loader
 */
public interface IGameScreenLoader {

    /**
     * Loads a game screen by its "name"
     * @param screenName name of the screen to oad
     * @return GameScreen Instance. (not initialised)
     */
    IGameScreen load(String screenName);
}
