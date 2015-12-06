package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.GraphicsRendering.CameraComponent;
import com.brm.GoatEngine.Physics.CircleColliderDef;
import com.brm.GoatEngine.Physics.Collider;


/**
 * Command used to create an entity
 */
public class CreateEntityCommand extends UndoCommand{


    private Entity createdEntity;


    public CreateEntityCommand(){

    }

    /**
     * Used to undo a command
     */
    @Override
    public void undo(){
        GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().deleteEntity(createdEntity.getID());
    }

    /**
     * Used to do a command
     */
    @Override
    public void redo() {
        Entity e = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager().createEntity();
        // By default give PhysicsComponent so we can move it and see it
        // Positioned at camera center

        CameraComponent cam = (CameraComponent) GoatEngine.gameScreenManager.getCurrentScreen()
                .getEntityManager().getComponents(CameraComponent.ID).get(0);



        e.addComponent(new PhysicsComponent(
                        GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld(),
                        BodyDef.BodyType.DynamicBody,
                        new Vector2(cam.getCamera().position.x, cam.getCamera().position.y),
                        1,1
                ),
                PhysicsComponent.ID
        );
        CircleColliderDef circDef = new CircleColliderDef();
        circDef.tag = "ok";
        Collider.addCircleCollider(e, circDef);
        createdEntity = e;
    }
}
