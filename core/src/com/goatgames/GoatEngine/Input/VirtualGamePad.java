package com.goatgames.goatengine.input;

import com.badlogic.gdx.controllers.Controller;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A virtual gamepad abstracting the a physical game pad
 * enables polling
 */
public class VirtualGamePad{


    private final Controller controller; // Gdx Controller object
    private final GamePadMap gamePadMap; // The game pad map to use
    private ArrayList<GamePadMap.Button> justReleasedButtons; //A list of the buttons that where just released
    private ArrayList<GamePadMap.Button> pressedButtons; //A list of the pressed buttons of the gamepad


    public VirtualGamePad(Controller controller, GamePadMap gamePadMap){
        this.controller = controller;
        this.gamePadMap = gamePadMap;
        this.pressedButtons = new ArrayList<GamePadMap.Button>();
        this.justReleasedButtons = new ArrayList<GamePadMap.Button>();
    }


    /**
     * Method simulating a button press
     * @param btn
     */
    public void pressButton(GamePadMap.Button btn){
        this.pressedButtons.add(btn);
    }

    /**
     * Method simulating a button release
     */
    public void releaseButton(GamePadMap.Button btn){
        this.pressedButtons.remove(btn);
        this.justReleasedButtons.add(btn);
    }

    /**
     * Releases a collection of buttons
     * @param buttons
     */
    public void releaseButtons(Collection<GamePadMap.Button> buttons){
        this.pressedButtons.removeAll(buttons);
    }

    /**
     * Releases all pressed button
     */
    public void releaseAll(){
        this.pressedButtons.clear();
        this.justReleasedButtons.clear();
    }

    /**
     * Returns whether or not a button is pressed
     * @param btn
     * @return
     */
    public boolean isButtonPressed(GamePadMap.Button btn){
        return this.pressedButtons.contains(btn);
    }

    /**
     * Returns whether or not there is any button pressed
     * If there is no button press returns true otherwise false
     * @return
     */
    public boolean isAnyButtonPressed(){
        return !this.pressedButtons.isEmpty();
    }

    /**
     * Returns whether or not a button was just released
     * @param btn
     * @return
     */
    public boolean isButtonJustReleased(GamePadMap.Button btn){
        return this.justReleasedButtons.contains(btn);
    }



    // GETTERS && SETTERS //

    public ArrayList<GamePadMap.Button> getPressedButtons() {
        return pressedButtons;
    }

    /**
     * Returns value of axis
     * @param axis
     */
    public float getAxis(GamePadMap.Axis axis){
        int rawCode = gamePadMap.getAxisRawCode(axis);
        if(rawCode != -1)
            return controller.getAxis(rawCode);
        return -10000;
    }

    /**
     * Returns value of axis
     * @param axis
     */
    public float getAxisStr(String axis){
        return getAxis(GamePadMap.Axis.valueOf(axis));
    }



}
