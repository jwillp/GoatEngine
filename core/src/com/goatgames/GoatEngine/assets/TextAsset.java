package com.goatgames.goatengine.assets;

import com.badlogic.gdx.files.FileHandle;

/**
 * Represents a loaded text file asset
 */
public class TextAsset {
    private String data;

    public TextAsset(FileHandle file){
        this.data = new String(file.readBytes());
    }

    /**
     * Returns the text read from file
     * @return the text read from file
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the text of the asset
     * @param data the text
     */
    public void setData(String data) {
        this.data = data;
    }
}
