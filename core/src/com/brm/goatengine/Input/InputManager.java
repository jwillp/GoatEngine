package com.brm.GoatEngine.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Global input manager
 */
public class InputManager{

    private final GameControllerManager gameControllerManager;
    private final KeyboardInputManager keyboardInputManager;
    private final InputMultiplexer multiplexer;


    public InputManager(){
        multiplexer = new InputMultiplexer();
        gameControllerManager = new GameControllerManager(this);
        keyboardInputManager = new KeyboardInputManager(this);
    }


    public void init(){
        Controllers.addListener(gameControllerManager);
        multiplexer.addProcessor(keyboardInputManager);
        setInputProcessor(multiplexer);
    }


    public GameControllerManager getGameControllerManager() {
        return gameControllerManager;
    }

    public KeyboardInputManager getKeyboardInputManager() {
        return keyboardInputManager;
    }


    public void addInputProcessor(InputProcessor processor){
        multiplexer.addProcessor(processor);

        //Make sure keyboardInputManager is always last // TODO more efficiant way
        multiplexer.removeProcessor(keyboardInputManager);
        multiplexer.addProcessor(multiplexer.size(), keyboardInputManager);

    }

    public void removeInputProcessor(InputProcessor processor){
        multiplexer.removeProcessor(processor);
    }

    /**
     * Lets an input processor reserve all input
     */
    public void reserve(InputProcessor processor){
        setInputProcessor(processor);
    }

    /**
     * When an input processor has reserved all input give the control back
     * to the other processors
     */
    public void release(){
        setInputProcessor(multiplexer);
    }



    private void setInputProcessor(final InputProcessor processor){
        Gdx.input.setInputProcessor(processor);
    }

    public int getProcessorCount() {
        return multiplexer.size();
    }
}
