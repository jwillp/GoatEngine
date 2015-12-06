package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.math.Vector3;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.GraphicsRendering.CameraComponent;

/**
 * Command moving an entity by dragging it
 */
public class MoveEntityDragCommand extends EditorCommand{

    private Entity entity;
    private final int newMouseX;
    private final int newMouseY;

    public MoveEntityDragCommand(Entity entity, int newMouseX, int newMouseY){
        this.entity = entity;
        this.newMouseX = newMouseX;
        this.newMouseY = newMouseY;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        final Vector3 pos = new Vector3();
        // Translate the mouse coordinates to world coordinates
        cam.getCamera().unproject(pos.set(newMouseX, newMouseY, 0));

        PhysicsComponent phys = (PhysicsComponent)entity.getComponent(PhysicsComponent.ID);
        phys.setPosition(pos.x, pos.y);

    }


}
