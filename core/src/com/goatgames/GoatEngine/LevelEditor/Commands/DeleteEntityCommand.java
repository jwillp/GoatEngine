package com.goatgames.goatengine.leveleditor.Commands;

import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.leveleditor.LevelEditor;

import java.util.HashMap;
import java.util.Map;

/**
 * Command deleting an entity
 */
public class DeleteEntityCommand extends UndoCommand {

    Entity deletedEntity;
    HashMap<String, EntityComponent> components;
    LevelEditor editor;

    public DeleteEntityCommand(Entity e, LevelEditor editor){
        deletedEntity = e;
        this.editor = editor;
    }


    /**
     * Used to undo a command
     */
    @Override
    public void undo() {
        for (Map.Entry<String, EntityComponent> entry : components.entrySet()) {
            String compName = entry.getKey();
            EntityComponent component = entry.getValue();
            deletedEntity.addComponent(component, compName);
        }
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
    }

    /**
     * Used to do a command
     */
    @Override
    public void redo() {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        components = manager.getComponentsForEntity(deletedEntity.getID());
        manager.deleteEntity(deletedEntity.getID());
        editor.setSelectedEntity(null);

    }
}