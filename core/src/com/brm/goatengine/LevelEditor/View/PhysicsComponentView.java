package com.brm.GoatEngine.LevelEditor.View;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.Arrays;

/**
 * View for the physics Component
 */
public class PhysicsComponentView extends ComponentView {


    final String LBL_POSX = "Position X";
    final String LBL_POSY = "Position Y";
    final String LBL_VELX = "Velocity X";
    final String LBL_VELY = "Velocity Y";
    final String LBL_BODYTYPE = "Body Type";


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
        addStringField(LBL_POSX, pox);
        addStringField(LBL_POSY, poy);

        addEmptyRow();

        // Velocity X, Y
        String vex = String.valueOf(phys.getVelocity().x)/*.substring(0,cutIndex)*/;
        String vey = String.valueOf(phys.getVelocity().y)/*.substring(0,cutIndex)*/;
        addStringField(LBL_VELX, vex);
        addStringField(LBL_VELY, vey);

        addEmptyRow();

        // Body Type
       SelectBox<String> combo =  addStringList(LBL_BODYTYPE,
               Arrays.toString(BodyDef.BodyType.values()).replaceAll("^.|.$", "").split(", ")
       );
        combo.setSelected(phys.getBody().getType().toString());

    }

    @Override
    protected void onApply() {
        PhysicsComponent phys = (PhysicsComponent) component;
        phys.setPosition(Float.parseFloat(stringFields.get(LBL_POSX).getText()),
                         Float.parseFloat(stringFields.get(LBL_POSX).getText()));

        phys.setVelocity(Float.parseFloat(stringFields.get(LBL_POSX).getText()),
                Float.parseFloat(stringFields.get(LBL_POSX).getText()));

        String bodyType = enumFields.get(LBL_BODYTYPE).getSelected().toString();
        phys.setBodyType(BodyDef.BodyType.valueOf(bodyType));
    }
}
