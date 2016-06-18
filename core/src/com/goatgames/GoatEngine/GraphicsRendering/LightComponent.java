package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.graphics.Color;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.utils.GAssert;

import java.util.Map;

/**
 * Used to display simple fake lights (alpha blending)
 */
public class LightComponent extends SpriteComponent {

    public final static String ID = "LIGHT_COMPONENT";

    public LightComponent(NormalisedEntityComponent data) {
        super(data);
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.put("color", color.toString());
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        // Parse Color
        String colorHex = data.get("color").replace("#","");
        if(colorHex.length() == 6) colorHex += "FF";
        this.color = new Color(Color.valueOf(colorHex));
    }

    @Override
    public String getId() {
        return ID;
    }
}
