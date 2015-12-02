package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.EntityComponent;

/**
 * View for the physics Component
 */
public class PhysicsComponentView extends ComponentView {

    public PhysicsComponentView(EntityComponent c, Skin skin) {
        super(c, skin);
    }

    @Override
    protected void initContent() {

        PhysicsComponent phys = (PhysicsComponent) component;

        int cutIndex = 5; // only 3 digit behind floating point

        // Position X, Y
        String pox = String.valueOf(phys.getPosition().x)/*.substring(0,cutIndex)*/;
        String poy = String.valueOf(phys.getPosition().y)/*.substring(0,cutIndex)*/;
        addString("Position X", pox);
        addString("Position Y", poy);

        // Velocity X, Y
        String vex = String.valueOf(phys.getVelocity().x)/*.substring(0,cutIndex)*/;
        String vey = String.valueOf(phys.getVelocity().y)/*.substring(0,cutIndex)*/;
        addString("Velocity X", vex);
        addString("Velocity Y", vey);

        // Body Type
        addString("Body type", String.valueOf(phys.getBody().getType()));
    }
}
