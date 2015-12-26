package com.goatgames.goatengine.leveleditor.Commands;

import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;

/**
 * Command performed when the user updates the editor
 * label of an entity
 */
public class UpdateEntityComponentCommand extends UndoCommand{

    private Entity entity;
    private EntityComponent oldValue;
    private EntityComponent newValue;

    public UpdateEntityComponentCommand(Entity entity, EntityComponent componentInstance) {
        this.entity = entity;
        newValue = componentInstance;
    }


    /**
     * Used to undo a command
     */
    @Override
    public void undo() {
        entity.addComponent(oldValue, newValue.getId());
    }

    /**
     * Used to do a command
     */
    @Override
    public void redo() {
        oldValue = entity.getComponent(newValue.getId());
        entity.addComponent(newValue, newValue.getId());
    }
}
