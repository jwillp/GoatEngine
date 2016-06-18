package com.goatgames.goatengine.utils;

/**
 * Classed use to test assertions
 */
public class GAssert{

    /**
     * Assert that test is true otherwise log failure
     * @param test the statement to test
     * @param messageOnFail a message to display in case of failure
     */
    public static boolean that(boolean test, String messageOnFail){
        if(!test){
            logAssertionError(messageOnFail);
        }
        return test;
    }

    /**
     * Assert that test is true otherwise log failure
     * @param test the statement to test
     * @param messageOnFail a message to display in case of failure
     */
    public static boolean that(boolean test, String messageOnFail, String fileName){
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
        return test;
    }

    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean notNull(Object o, String messageOnFail){
        boolean test = o != null;
        if(!test){
            logAssertionError(messageOnFail);
        }
        return test;
    }

    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean notNull(Object o, String messageOnFail, String fileName){
        boolean test = (o != null);
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
        return test;
    }

    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean isNull(Object o, String messageOnFail){
        boolean test = (o == null);
        if(!test){
            logAssertionError(messageOnFail);
        }
        return test;
    }

    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static boolean isNull(Object o, String messageOnFail, String fileName){
        boolean test = (o == null);
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
        return test;
    }

    /**
     * Logs the assertion error to the logger
     * @param message
     */
     private static void logAssertionError(String message){
         Logger.error("ASSERTION FAIL " + message + " at: " + new Exception().getStackTrace()[2].toString());
     }

    /**
     * Logs the assertion error to the logger
     * @param messageOnFail
     * @param fileName
     */
    private static void logAssertionError(String messageOnFail, String fileName) {
        Logger.error("ASSERTION FAIL " + messageOnFail + " in: " + fileName);
    }
}
