package com.brm.GoatEngine.Files;

/**
 * Used to handle Files
 */
public class FileSystem{

    /**
     * Makes sure that the directory passed ens with a "/"
     * @param directory
     * @return
     */
    public static String sanitiseDir(String directory){
        if(!directory.endsWith("/")){
            directory += "/";
        }
        return directory;
    }


}
