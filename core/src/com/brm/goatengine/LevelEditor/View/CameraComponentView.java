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

    private final static String LBL_POSITION_X = "Position X";
    private final static String LBL_POSITION_Y = "Position Y";
    private final static String LBL_POSITION_Z = "Position Z";

    private final static String LBL_ZOOM = "Zoom";

    private final static String LBL_VIEWPORT_W = "Viewport Width";
    private final static String LBL_VIEWPORT_H = "Viewport Height";

    @Override
    protected void initContent() {
        CameraComponent camComp = (CameraComponent)component;
        OrthographicCamera cam = camComp.getCamera();

        addStringField(LBL_POSITION_X, String.valueOf(cam.position.x));
        addStringField(LBL_POSITION_Y, String.valueOf(cam.position.y));
        addStringField(LBL_POSITION_Z, String.valueOf(cam.position.z));

        addStringField(LBL_ZOOM, String.valueOf(cam.zoom));

        addStringField(LBL_VIEWPORT_W, String.valueOf(cam.viewportWidth));
        addStringField(LBL_VIEWPORT_H, String.valueOf(cam.viewportHeight));
    }

    @Override
    protected void onApply() {
        CameraComponent camComp = (CameraComponent)component;
        OrthographicCamera cam = camComp.getCamera();

        cam.position.set(Float.parseFloat(stringFields.get(LBL_POSITION_X).getText()),
                         Float.parseFloat(stringFields.get(LBL_POSITION_X).getText()),
                         Float.parseFloat(stringFields.get(LBL_POSITION_X).getText())
        );

        cam.zoom = Float.parseFloat(stringFields.get(LBL_ZOOM).getText());
        cam.viewportWidth = Float.parseFloat(stringFields.get(LBL_VIEWPORT_W).getText());
        cam.viewportHeight = Float.parseFloat(stringFields.get(LBL_VIEWPORT_H).getText());
    }
}
