package com.brm.GoatEngine.LevelEditor.Commands;

import com.badlogic.gdx.math.Vector3;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.GraphicsRendering.CameraComponent;


/**
 * Moves the camera around according to a mouse drag
 */
public class MoveDragCameraCommand extends EditorCommand {

    // Current mouse pos
    private final int screenX;
    private final int screenY;

    // Last mouse pos
    private final int lastScreenX;
    private final int lastScreenY;

    public MoveDragCameraCommand(int screenX, int screenY, int lastScreenX, int lastScreenY){
        this.screenX = screenX;
        this.screenY = screenY;
        this.lastScreenX = lastScreenX;
        this.lastScreenY = lastScreenY;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);

        Vector3 delta = new Vector3(screenX - lastScreenX,
                                    screenY - lastScreenY,
                                    0);
        delta.x *= -1;  // invert
        float smoothing = 0.08f * cam.getCamera().zoom;
        delta.x *= smoothing;
        delta.y *= smoothing;
        cam.getCamera().translate(delta);
    }












}
