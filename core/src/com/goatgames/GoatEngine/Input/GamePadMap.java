package com.goatgames.goatengine.input;

import com.badlogic.gdx.utils.IntMap;

/**
 * Game Pad Button Map
 */
public class GamePadMap{

    protected IntMap<Button> buttonMap;
    protected int dpadCode;
    protected IntMap<AnalogStick> analogStickMap;
    protected IntMap<Trigger> triggerMap;

    public Button getButton(int rawCode) {
        return buttonMap.get(rawCode);
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
        ANALOG_STICK_LEFT_BUTTON,
        ANALOG_STICK_RIGHT_BUTTON,
    }

    /**
     * Triggers
     */
    public enum Trigger{
        LEFT,
        RIGHT,
    }

    public enum AnalogStick{
        LEFT,
        RIGHT,
    }

}
