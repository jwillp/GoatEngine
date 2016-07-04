package com.goatgames.goatengine.files;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * File Manager interface using LibGdx's file system
 * TODO Tests
 */
public class GdxFileManager implements IFileManager {
    @Override
    public IFileHandle getFileHandle(String path) {
        path = sanitizePath(path);
        return new GdxFileHandle(path, this);
    }

    @Override
    public List<IFileHandle> getDirectories(String base) {
        base = sanitizePath(base);
        List<IFileHandle> files = getFiles(base);
        for (Iterator<IFileHandle> iterator = files.iterator(); iterator.hasNext(); ) {
            IFileHandle file = iterator.next();
            if(!file.isDirectory()){
                files.remove(file);
            }
        }
        return files;
    }

    @Override
    public List<IFileHandle> getFiles(String dir) {
        dir = sanitizePath(dir);
        GdxFileHandle dirHandle = (GdxFileHandle)getFileHandle(dir);

        // Get Children
        List<IFileHandle> files = new ArrayList<>(); // Handles to return
        FileHandle[] fileHandles = dirHandle.getGdxHandle().list(); // Gdx handles

        // Convert file handles to GdxFileHandles
        for (FileHandle fileHandle : fileHandles) {
            files.add(getFileHandle(fileHandle.path()));
        }
        return files;
    }

    @Override
    public List<IFileHandle> getFiles(String dir, boolean recursively) {
        dir = sanitizePath(dir);
        IFileHandle dirHandle = new GdxFileHandle(dir, this);
        List<IFileHandle> handles = new ArrayList<>();
        getHandles((GdxFileHandle) dirHandle,handles);
        return handles;
    }

    /**
     * Returns file handles (used to find file handles recursively)
     * @param begin
     * @param handles
     */
    private void getHandles(GdxFileHandle begin, List<IFileHandle> handles) {
        FileHandle[] newHandles = begin.getGdxHandle().list();
        for (FileHandle f : newHandles) {
            if (f.isDirectory()) {
                getHandles((GdxFileHandle) getFileHandle(f.path()), handles);
            } else {
                handles.add(getFileHandle(f.path()));
            }
        }
    }

    @Override
    public boolean exists(String path) {
        path = sanitizePath(path);
        return getFileHandle(path).exists();
    }

    @Override
    public boolean mkdirs(String path) {
        path = sanitizePath(path);
        FileHandle handle = (FileHandle) getFileHandle(path);
        handle.mkdirs();
        return true;
    }

    @Override
    public boolean delete(String path) {
        path = sanitizePath(path);
        FileHandle handle = (FileHandle) getFileHandle(path);
        return handle.delete();
    }

    @Override
    public boolean deleteDirectory(String path) {
        path = sanitizePath(path);
        FileHandle handle = (FileHandle) getFileHandle(path);
        return handle.deleteDirectory();
    }

    @Override
    public boolean emptyDirectory(String path) {
        path = sanitizePath(path);
        FileHandle handle = (FileHandle) getFileHandle(path);
        handle.emptyDirectory();
        return true;
    }

    @Override
    public boolean copyTo(String path, String newLocation) {
        path = sanitizePath(path);
        FileHandle handle = (FileHandle) getFileHandle(path);
        handle.copyTo((FileHandle) getFileHandle(newLocation));
        return true;
    }

    @Override
    public boolean moveTo(String path, String newLocation) {
        path = sanitizePath(path);
        FileHandle handle = (FileHandle) getFileHandle(path);
        handle.moveTo((FileHandle) getFileHandle(newLocation));
        return true;
    }

    @Override
    public String sanitizePath(String path) {
        // Replaces backward slashes of a path with forward slashes
        path = path.replace("\\", "/");
        return path;
    }
}
