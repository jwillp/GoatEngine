package com.brm.GoatEngine.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.brm.GoatEngine.Input.GameControllerManager;
import com.brm.GoatEngine.Input.KeyboardInputManager;

/**
 * Global input manager
 */
public class InputManager{

    private final GameControllerManager gameControllerManager;

    private final KeyboardInputManager keyboardInputManager;

    public InputManager(){
        gameControllerManager = new GameControllerManager(this);
        keyboardInputManager = new KeyboardInputManager(this);
    }


    public void init(){
        Controllers.addListener(gameControllerManager);
        Gdx.input.setInputProcessor(keyboardInputManager);
    }


    public GameControllerManager getGameControllerManager() {
        return gameControllerManager;
    }

    public KeyboardInputManager getKeyboardInputManager() {
        return keyboardInputManager;
    }








}
