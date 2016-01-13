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
    public static void that(boolean test, String messageOnFail){
        if(!test){
            logAssertionError(messageOnFail);
        }
    }

    /**
     * Assert that test is true otherwise log failure
     * @param test the statement to test
     * @param messageOnFail a message to display in case of failure
     */
    public static void that(boolean test, String messageOnFail, String fileName){
        if(!test){
            logAssertionError(messageOnFail, fileName);
        }
    }




    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static void notNull(Object o, String messageOnFail){
        if(o == null){
            logAssertionError(messageOnFail);
        }
    }


    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static void notNull(Object o, String messageOnFail, String fileName){
        if(o == null){
            logAssertionError(messageOnFail, fileName);
        }
    }


    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static void isNull(Object o, String messageOnFail){
        if(o != null){
            logAssertionError(messageOnFail);
        }
    }


    /**
     * Assert that something is null
     * @param o something
     * @param messageOnFail message to log on test failure
     */
    public static void isNull(Object o, String messageOnFail, String fileName){
        if(o != null){
            logAssertionError(messageOnFail, fileName);
        }
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
