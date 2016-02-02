package com.goatgames.goatengine.leveleditor.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.ecs.PrefabFactory;
import com.goatgames.goatengine.leveleditor.LevelEditor;
import com.goatgames.goatengine.leveleditor.view.WarningDialog;
import com.goatgames.goatengine.utils.Logger;
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
        fileChooser.setDirectory(Gdx.files.internal(GEConfig.DATA_DIR + "prefabs"));
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
        try {
            prefabFactory.createEntity(file.toString());
        }catch(Exception e){
            new WarningDialog(
                    "Invalid Prefab" + "\n" + e.getMessage(),
                    editor.getView().getBtnConsole().getSkin()).show(editor.getView().getStage());
            e.printStackTrace();
        }
    }


}
