package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Command deleting an entity
 */
public class DeleteEntityCommand extends UndoCommand {

    Entity deletedEntity;
    HashMap<String, EntityComponent> components;

    public DeleteEntityCommand(Entity e){
        deletedEntity = e;
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
    }

    /**
     * Used to do a command
     */
    @Override
    public void redo() {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        HashMap<String, EntityComponent> components = manager.getComponentsForEntity(deletedEntity.getID());
        manager.deleteEntity(deletedEntity.getID());
    }
}
