package com.brm.GoatEngine.Files;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

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

    /**
     * This is recursive
     */
    public static Array<FileHandle> getFilesInDir(String dir){

        FileHandle dirHandle = Gdx.files.internal(dir);
        Array<FileHandle> handles = new Array<FileHandle>();
        getHandles(dirHandle, handles);
        return handles;
    }

    /**
     * Returns file handles
     * @param begin
     * @param handles
     */
    private static void getHandles(FileHandle begin, Array<FileHandle> handles)
    {
        FileHandle[] newHandles = begin.list();
        for (FileHandle f : newHandles) {
            if (f.isDirectory()) {
                getHandles(f, handles);
            } else {
                handles.add(f);
            }
        }
    }
}
