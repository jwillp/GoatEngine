package com.brm.GoatEngine.LevelEditor.Commands;

import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Rendering.CameraComponent;

/**
 * Command for Zooming In and Out
 */
public class ZoomCameraCommand extends EditorCommand {

    public enum Mode{IN, OUT}

    public final Mode mode;
    public ZoomCameraCommand(Mode mode) {
        this.mode = mode;
    }

    /**
     * Executes the logic of the command
     */
    @Override
    public void exec() {
        EntityManager manager = GoatEngine.gameScreenManager.getCurrentScreen().getEntityManager();
        CameraComponent cam = (CameraComponent) manager.getComponents(CameraComponent.ID).get(0);
        float zoomFactor = 0.2f;
        float zoomChange = mode == Mode.IN ? -zoomFactor :zoomFactor;
        cam.getCamera().zoom += zoomChange;
    }
}
