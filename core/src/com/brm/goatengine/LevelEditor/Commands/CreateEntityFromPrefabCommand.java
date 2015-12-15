package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.brm.GoatEngine.ECS.PrefabFactory;
import com.brm.GoatEngine.Files.FileSystem;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
import com.brm.GoatEngine.Utils.Logger;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

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
        // Display a Choose Prefab window
        //chooser creation
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(Gdx.files.internal("data/prefabs"));
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected (FileHandle file) {
                CreateEntityFromPrefabCommand.this.onSelected(file);
            }
        });

        editor.getView().getCenter().add(fileChooser);
    }


    public void onSelected(FileHandle file){
        Logger.debug("SELECTED " + file.toString());
        PrefabFactory prefabFactory = new PrefabFactory();
        prefabFactory.createEntity(file.toString());
    }


}
