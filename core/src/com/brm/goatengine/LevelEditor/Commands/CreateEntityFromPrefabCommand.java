package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.Gdx;
import com.brm.GoatEngine.LevelEditor.LevelEditor;

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
        Gdx.files.internal("data/prefabs/*");

        // Display a Choose Prefab window

    }
}
