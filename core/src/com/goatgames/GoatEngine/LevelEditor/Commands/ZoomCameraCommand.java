package com.goatgames.goatengine.leveleditor.commands;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.graphicsrendering.CameraComponent;

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
