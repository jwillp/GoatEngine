package com.goatgames.gdk;

/**
 * Utils for String
 */
public abstract class GStringUtil {

    /**
     * Indicates if an given string represents an integer
     *
     * @param str string to test
     * @return true if string represents integer, otherwise false
     */
    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
