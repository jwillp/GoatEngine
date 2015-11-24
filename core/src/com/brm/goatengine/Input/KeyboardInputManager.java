package com.brm.GoatEngine.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Input.Events.*;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Manages Keyboard and Mouse Inputs
 */
public class KeyboardInputManager implements InputProcessor {

    private final InputManager inputManager;

    private boolean isDragging = false;

    private int lastMouseButton = -1;

    public KeyboardInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }



    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        //GoatEngine.console.log(String.valueOf(keycode), "WARNING");
        GoatEngine.eventManager.fireEvent(new KeyPressedEvent(keycode));
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        GoatEngine.eventManager.fireEvent(new KeyReleasedEvent(keycode));
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.lastMouseButton = button;
        isDragging = false;
        GoatEngine.eventManager.fireEvent(new MousePressEvent(screenX, screenY, button));
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        GoatEngine.eventManager.fireEvent(new MouseReleasedEvent(screenX, screenY, button));

        if(!isDragging && button == lastMouseButton){
            GoatEngine.eventManager.fireEvent(new MouseClickEvent(screenX, screenY, button));
            Logger.debug("CLICK");
        }

        lastMouseButton = -1;
        isDragging = false;

        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        this.isDragging = true;

        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
