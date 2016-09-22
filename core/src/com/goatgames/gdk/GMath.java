package com.goatgames.gdk;

/**
 * common Math
 */
public abstract class GMath {

    /**
     * Returns whether a values is more or less a given number around another value.
     * E.g.: 3.2 is more or less 3 with a buffer of 0.5
     *
     * @param value the value to test
     * @param moreOrLess the more or less buffer
     * @return true if more or less, false otherwise
     */
    public static boolean isMoreOrLess(float value, float goalValue, float moreOrLess){
        return ((goalValue - moreOrLess) <= value) && (value <= (goalValue + moreOrLess));
    }
}
