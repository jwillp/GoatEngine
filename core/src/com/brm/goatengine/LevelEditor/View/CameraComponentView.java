package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Rendering.CameraComponent;

/**
 * Used to display camera component
 */
public class CameraComponentView extends ComponentView {

    public CameraComponentView(EntityComponent c, Skin skin) {
        super(c, skin);
    }

    @Override
    protected void initContent() {
        CameraComponent camComp = (CameraComponent)component;
        OrthographicCamera cam = camComp.getCamera();

        addStringField("Position X", String.valueOf(cam.position.x));
        addStringField("Position Y", String.valueOf(cam.position.y));
        addStringField("Position Z", String.valueOf(cam.position.z));

        addStringField("Zoom", String.valueOf(cam.zoom));

        addStringField("Viewport Width", String.valueOf(cam.viewportWidth));
        addStringField("Viewport Height", String.valueOf(cam.viewportHeight));
    }

    @Override
    protected void onApply() {

    }
}
