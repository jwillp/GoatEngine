package com.goatgames.goatengine.desktop.input;

import com.badlogic.gdx.utils.IntMap;

/**
 * Game Pad Button Map
 */
public abstract class GamePadMap{

    protected IntMap<Button> buttonMap;
    protected int dpadCode;
    protected IntMap<Axis> axisMap;

    public GamePadMap(){
        this.buttonMap = new IntMap<>();
        this.axisMap = new IntMap<>(2);
    }

    /**
     * Converts a raw code and returns it's standard button name
     * @param rawCode the raw code of the button
     * @return the Button corresponding to the raw button
     */
    public Button getButton(int rawCode) {
        Button button = buttonMap.get(rawCode);
        return (button == null) ? Button.UNMAPPED : button;
    }

    /**
     * Converts an axis and returns it's standard axis name
     * @param rawCode
     * @return
     */
    public Axis getAxis(int rawCode) {
        Axis axis = axisMap.get(rawCode);
        return  (axis == null) ? Axis.UNMAPPED : axis;
    }

    /**
     * Returns the raw code of an axis
     * @param axis
     * @return -1 if not found
     */
    public int getAxisRawCode(Axis axis){
        return axisMap.findKey(axis,true, -1);
    }

    /**
     * Maps a standard button to a rawCode
     * @param button
     * @param rawCode
     */
    protected void mapButton(Button button, int rawCode){
        buttonMap.put(rawCode, button);
    }

    /**
     * Maps a standard button to a rawCode
     * @param axis
     * @param rawCode
     */
    protected void mapAxis(Axis axis, int rawCode){
        axisMap.put(rawCode, axis);
    }

    /**
     * Maps a standard button to a rawCode
     * @param rawCode
     */
    protected void mapPOV(int rawCode){
        dpadCode = rawCode;
    }

    /**
     * Standard Buttons
     */
    public enum Button {
        BUTTON_A,
        BUTTON_B,
        BUTTON_X,
        BUTTON_Y,

        BUTTON_START,
        BUTTON_BACK,

        // The DPAD as a whole is considered a button. This button is pressed in directions
        // such as north south east west and combinations such as northwest northeast etc..
        DPAD,

        BUTTON_LB,
        BUTTON_L2,

        BUTTON_RB,
        BUTTON_R2,

        // Analog Sticks
        // The pressing of an analog stick is considered as a button
        BUTTON_LS,
        BUTTON_RS,

        /* Used when a button should not be recognized or when
        the button simply can't be recognized by a map */
        UNMAPPED,
    }

    /**
     * An axis means wither a joy stick or a trigger
     */
    public enum Axis{
        AXIS_LX,  /* Left Joystick X axis */
        AXIS_LY,  /* Left Joystick Y axis */
        AXIS_RX,  /* Right Joystick X axis */
        AXIS_RY,  /* Right Joystick Y axis **/
        TRIGGER,  /* Triggers */
        UNMAPPED, /* UNMAPPED */
    }
}
