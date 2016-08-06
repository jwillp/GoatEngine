package com.goatgames.goatengine.screenmanager;

/**
 * Loader for lua based Game Screens
 */
public class LuaGameScreenLoader implements IGameScreenLoader {
    @Override
    public IGameScreen load(String screenName) {
        return new LuaGameScreen(screenName);
    }
}
