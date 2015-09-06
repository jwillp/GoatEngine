package com.brm.GoatEngine.Files;

/**
 * Used to handle Files
 */
public class FileSystem{

    public static String sanitiseDir(String directory){
        if(!directory.endsWith("/")){
            directory += "/";
        }
        return directory;
    }


}
