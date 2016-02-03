package com.goatgames.goatengine.input;

import com.badlogic.gdx.utils.IntMap;

/**
 * Xbox 360 Game Pad Map
 */
public class Xbox360Map extends GamePadMap {

    public Xbox360Map(){

        this.buttonMap = new IntMap<>();
        this.analogStickMap = new IntMap<>(2);
        this.triggerMap = new IntMap<>(2);

        /* BUTTONS */
        buttonMap.put(0, Button.BUTTON_A);
        buttonMap.put(1, Button.BUTTON_B);
        buttonMap.put(2, Button.BUTTON_X);
        buttonMap.put(3, Button.BUTTON_Y);

        buttonMap.put(5, Button.BUTTON_RB);
        buttonMap.put(4, Button.BUTTON_LB);

        buttonMap.put(6, Button.BUTTON_BACK);
        buttonMap.put(7, Button.BUTTON_START);

        buttonMap.put(8, Button.ANALOG_STICK_LEFT_BUTTON);
        buttonMap.put(9, Button.ANALOG_STICK_RIGHT_BUTTON);


        /* DPAD */
        this.dpadCode = 0;

        /* TRIGGERS */
        // TODO
        //triggerMap.put()


        /* ANALOG STICKS */
        analogStickMap.put(1, AnalogStick.LEFT);
        analogStickMap.put(0, AnalogStick.RIGHT);





    }
}
