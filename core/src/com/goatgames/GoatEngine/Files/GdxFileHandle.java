package com.goatgames.goatengine.files;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.goatgames.gdk.io.IFileHandle;
import com.goatgames.gdk.io.IFileManager;

import java.io.File;
import java.io.InputStream;

/**
 * File Handle using LibGdx File Handle
 */
public class GdxFileHandle implements IFileHandle {

    private IFileManager fileManager;
    private FileHandle gdxHandle;

    protected GdxFileHandle(String path, IFileManager fileManager) {
        gdxHandle = Gdx.files.internal(path);
        this.fileManager = fileManager;
    }

    protected GdxFileHandle(FileHandle handle, IFileManager fileManager){
        this.gdxHandle = handle;
        this.fileManager = fileManager;
    }

    public FileHandle getGdxHandle(){
        return gdxHandle;
    }

    @Override
    public String getPath() {
        return gdxHandle.path();
    }

    @Override
    public String getName() {
        return gdxHandle.name();
    }

    @Override
    public String getExtension() {
        return gdxHandle.extension();
    }

    @Override
    public String getNameWithoutExtension() {
        return gdxHandle.nameWithoutExtension();
    }

    @Override
    public String getPathWithoutExtension() {
        return gdxHandle.pathWithoutExtension();
    }

    @Override
    public boolean exists() {
        return gdxHandle.exists();
    }

    @Override
    public String getParent() {
        return gdxHandle.parent().path();
    }

    @Override
    public boolean isDirectory() {
        return gdxHandle.isDirectory();
    }

    @Override
    public boolean isFile() {
        return !isDirectory();
    }

    @Override
    public String readString() {
        return gdxHandle.readString();
    }

    @Override
    public File getFile() {
        return gdxHandle.file();
    }

    @Override
    public long getLastModifiedTime() {
        return gdxHandle.lastModified();
    }

    @Override
    public IFileManager getFileManager() {
        return fileManager;
    }

    @Override
    public InputStream read() {
        return gdxHandle.read();
    }

    @Override
    public void writeString(String string, boolean append) {
        // We cannot write using an internal handler, convert it to local
        if(gdxHandle.type() == Files.FileType.Internal ){
            gdxHandle = Gdx.files.local(string);
        }
        gdxHandle.writeString(string, append);
    }

    @Override
    public void writeString(String string, boolean append, String charset) {
        // We cannot write using an internal handler, convert it to local
        if(gdxHandle.type() == Files.FileType.Internal ){
            gdxHandle = Gdx.files.local(gdxHandle.path());
        }
        gdxHandle.writeString(string, append, charset);
    }
}
