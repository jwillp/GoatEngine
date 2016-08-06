package com.goatgames.goatengine.files;

import java.io.File;
import java.io.InputStream;

/**
 * Interface used to represent a handle on a file or directory.
 * IFileHandles should always be cosntructed using the IFileManager,
 * therefore not providing any public constructors.
 */
public interface IFileHandle {

    /**
     * Returns the path of a file/directory. Backward slashes will be replaced by forward slashes
     * @return the path of the file or directory
     */
    String getPath();

    /**
     * Returns the name of the file/directory, without any parent paths.
     * E.g.: for a file contained in /adir/dir/afile.txt --> will return file.txt
     * @return the name of the file or directory
     * */
    String getName();

    /**
     * Returns the extension of the file, if any.
     * @return the extension if the file has one, empty string otherwise
     */
    String getExtension();

    /**
     * Returns the name of the file without any parent paths (see getName) and without the extension
     * @return the name of the file without any extension or path
     */
    String getNameWithoutExtension();

    /**
     * Returns the path of the file without any extension  e.g. dir/dir2/file.png -> dir/dir2/file.
     * backward slashes will be replaced by forward slashes
     * @return the path of the file without any extension
     */
    String getPathWithoutExtension();

    /**
     * Indicates whether or not the file/directory exists
     * @return true if the file or directory exists, false otherwise
     */
    boolean exists();

    /**
     * Returns the path to the parent directory of the file/directory, if any.
     * backward slashes will be replaced by forward slashes
     * @return the parent of the file/directory, empty string if none
     */
    String getParent();

    /**
     * Indicates if the current file handle is on a directory or not.
     * @return true if on a directory, false otherwise
     */
    boolean isDirectory();

    /**
     * Indicates if the current file handle is on a file or not (as opposed to a directory).
     * @return true if handle is on a file, false otherwise
     */
    boolean isFile();

    /**
     * Reads the entire content of the file into a String
     * @return a String representing the entire content of the file. (Empty string for directories)
     */
    String readString();

    /**
     * Returns a java.io.File instance that represents the file rerpesented by the current handle.
     * @return java.io.File instance
     */
    File getFile();

    /**
     * Returns the last modified time in milliseconds for this file. Zero is returned if the file doesn't exist.
     * @return the last modified time in milliseconds.
     */
    long getLastModifiedTime();

    /**
     * Returns the file manager that created this file handle.
     * @return file manager
     */
    IFileManager getFileManager();

    /**
     * Reads a file as an input stream
     * @return InputStream
     */
    InputStream read();

    /**
     * Writes a string to the file.
     * @param string the string to write
     * @param append if <code>false</code>, the file should be overwritten if it exists, otherwise
     *               append text at the end of the file
     */
    void writeString(String string, boolean append);

    /**
     * Writes a string to the file.
     * @param string the string to write
     * @param append if <code>false</code>, the file should be overwritten if it exists, otherwise
     *               append text at the end of the file
     * @param charset the charset to use
     */
    void writeString(String string, boolean append, String charset);
}
