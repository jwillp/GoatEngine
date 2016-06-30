package com.goatgames.goatengine.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * A loader used to load text files from disk
 */
public class TextAssetLoader extends AsynchronousAssetLoader<TextAsset, TextAssetLoader.TextParameter> {

    TextAsset data;

    public TextAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {
        this.data = null;
        this.data = new TextAsset(file);
    }

    @Override
    public TextAsset loadSync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {
        TextAsset data = this.data;
        this.data = null;
        return data;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextParameter parameter) {
        return null;
    }

    public class TextParameter extends AssetLoaderParameters<TextAsset> {
    }
}
