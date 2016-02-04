package com.goatgames.goatengine.input;

/**
 * Xbox 360 Game Pad Map
 */
public class Xbox360Map extends GamePadMap {

    public Xbox360Map(){


        /* BUTTONS */
        mapButton(Button.BUTTON_A, 0);
        mapButton(Button.BUTTON_B, 1);
        mapButton(Button.BUTTON_X, 2);
        mapButton(Button.BUTTON_Y, 3);

        mapButton(Button.BUTTON_RB, 5);
        mapButton(Button.BUTTON_LB, 4);

        mapButton(Button.BUTTON_BACK, 6);
        mapButton(Button.BUTTON_START, 7);

        mapButton(Button.BUTTON_LS, 8);
        mapButton(Button.BUTTON_RS, 9);


        /* DPAD */
        mapPOV(0);


        /* AXIS */
        /* ANALOG STICKS */

        // Left stick
        mapAxis(Axis.AXIS_LX, 1);  //-1 is up | +1 is down
        mapAxis(Axis.AXIS_LY, 0);  //-1 is left | +1 is right

        // Right stick
        mapAxis(Axis.AXIS_RX, 3);  //-1 is up | +1 is down
        mapAxis(Axis.AXIS_RY, 2);  //-1 is left | +1 is right


        mapAxis(Axis.TRIGGER, 4); /* LT and RT are on the same Axis! LT > 0 | RT < 0 */

    }
}
