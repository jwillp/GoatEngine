package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.brm.GoatEngine.Files.FileSystem;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
import com.brm.GoatEngine.LevelEditor.View.SelectFileDialog;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Creates an entity using a prefab
 */
public class CreateEntityFromPrefabCommand extends EditorCommand {


    private LevelEditor editor;

    public CreateEntityFromPrefabCommand(LevelEditor editor){
        this.editor = editor;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        // Detect all prefabs
        Array<FileHandle> files =  FileSystem.getFilesInDir("data/prefabs");
        Logger.debug(files.size);
        // Display a Choose Prefab window
        SelectFileDialog d = new SelectFileDialog("prefab", Gdx.files.internal("data/prefabs/"), new SelectFileDialog.FileDialogListener() {
            @Override
            public void selected(FileHandle file) {
                CreateEntityFromPrefabCommand.this.onSelected(file);
            }
        });
        editor.getView().getCenter().add(d);
    }


    public void onSelected(FileHandle file){
        Logger.debug("SELECTED " + file.toString());
    }


}
