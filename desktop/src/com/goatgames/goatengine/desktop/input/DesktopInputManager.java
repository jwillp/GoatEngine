package com.goatgames.goatengine.desktop.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controllers;
import com.goatgames.goatengine.input.InputManager;

/**
 * Desktop input manager supporting, keyboard and mouse input as well as GamePad input
 */
public class DesktopInputManager extends InputManager {

    private final GamePadManager gamePadManager;
    private final KeyboardInputManager keyboardInputManager;

    public DesktopInputManager() {
        super();
        gamePadManager = new GamePadManager(this);
        keyboardInputManager = new KeyboardInputManager(this);
    }

    @Override
    public void init(){
        super.init();
        Controllers.addListener(gamePadManager);
        multiplexer.addProcessor(keyboardInputManager);
    }

    public void addInputProcessor(InputProcessor processor){
        super.addInputProcessor(processor);

        // Make sure keyboardInputManager is always last // TODO more efficient way
        multiplexer.removeProcessor(keyboardInputManager);
        multiplexer.addProcessor(multiplexer.size(), keyboardInputManager);
    }

    public GamePadManager getGamePadManager() {
        return gamePadManager;
    }
    public KeyboardInputManager getKeyboardInputManager() {
        return keyboardInputManager;
    }
}
