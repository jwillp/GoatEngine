package com.goatgames.goatengine.files;

import java.util.List;

/**
 * Interface used for file management
 */
public interface IFileManager {

    /**
     * Returns a file handle from a file path
     * @return
     */
    IFileHandle getFileHandle(String path);

    /**
     * Returns a list of file handles representing directories contained in a base directory
     * @param base base directory to scan for directories
     * @return List of directories as file handles
     */
    List<IFileHandle> getDirectories(String base);

    /**
     * Returns a list of file handles representing the files contained in a directory
     * @param dir directory to scan for files
     * @return List of file handles
     */
    List<IFileHandle> getFiles(String dir);

    /**
     * Returns a list of file handles representing the files contained in a directory
     * @param dir directory to scan for files
     * @param recursively indicates if the files returned must contain the files in sub directories.
     * @return List of file handles
     */
    List<IFileHandle> getFiles(String dir, boolean recursively);

    /**
     * Indicates if a directory or file exist
     * @param path path of the directory/file to test
     * @return true if it exists, false otherwise
     */
    boolean exists(String path);

    /**
     * Creates the directory named by this abstract pathname, including any
     * necessary but nonexistent parent directories.  Note that if this
     * operation fails it may have succeeded in creating some of the necessary
     * parent directories.
     * @param path
     *
     * @return <code>true</code> if and only if the directory was created,
     *          along with all necessary parent directories; <code>false</code>
     *          otherwise
     */
    boolean mkdirs(String path);

    /**
     * Deletes the file or empty directory represented by path.
     * @param path path of the file or directory to delete
     * @return <code>true</code> if and only if the directory/file was deleted.
     *         <code>false</code> otherwise
     */
    boolean delete(String path);

    /**
     * Deletes a directory and all children, recursively.
     * @param path path to the directory to delete
     * @return <code>true</code> if and only if the directory and all its children were deleted;
     *         <code>false</code> otherwise
     */
    boolean deleteDirectory(String path);

    /**
     * Deletes all children of directory represented by path, recursively.
     * @param path path to the directory to delete
     * @return <code>true</code> if and only if the directory and all its children were deleted;
     *         <code>false</code> otherwise
     */
    boolean emptyDirectory(String path);

    /**
     * Copies a file or directory to a specified location
     * @param path the path to the file or directory to copy.
     * @param newLocation the new path of the directory or file to copy
     * @return <code>true</code> if the operation was successful;
     *         <code>false</code> otherwise
     */
    boolean copyTo(String path, String newLocation);

    /**
     * Moves a file or directory to a specified location
     * @param path the path to the file or directory to move.
     * @param newLocation the new path of the directory or file to move
     * @return <code>true</code> if the operation was successful;
     *         <code>false</code> otherwise
     */
    boolean moveTo(String path, String newLocation);

    /**
     * Sanitises a path for the File Manager's specific needs. Mostly replacing backward slashes with forward slashes.
     * @param path the path to sanitize
     * @return sanitized path as a String
     */
    String sanitizePath(String path);
}
