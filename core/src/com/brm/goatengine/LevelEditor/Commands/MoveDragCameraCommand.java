package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.math.Vector3;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Rendering.CameraComponent;

/**
 * Moves the camera around according to a mouse drag
 */
public class MoveDragCameraCommand extends EditorCommand {


    private final int toX;
    private final int toY;
    private final int lastX;
    private final int lastY;

    public MoveDragCameraCommand(int toX, int toY, int lastX, int lastY){
        this.toX = toX;
        this.toY = toY;
        this.lastX = lastX;
        this.lastY = lastY;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);

        Vector3 delta = new Vector3(toX - lastX,
                                    toY - lastY,
                                    0);
        delta.x *= -1;  // invert
        

        cam.getCamera().translate(delta);


    }












}
