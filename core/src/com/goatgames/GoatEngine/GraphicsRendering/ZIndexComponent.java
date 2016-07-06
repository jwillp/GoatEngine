package com.goatgames.goatengine.graphicsrendering;

import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.Comparator;

/**
 * Used to Index Displayable things
 */
public class ZIndexComponent extends EntityComponent{

    public final static String ID = "Z_INDEX_COMPONENT";

    public int index; // z index, lowest the more in the background

    public ZIndexComponent(NormalisedEntityComponent data) {
        super(data);
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.put("z_index", String.valueOf(this.index));
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data){
        index = Integer.parseInt(data.get("z_index"));
    }

    @Override
    public String getId() {
        return ID;
    }

    /**
     * Comparator used to sort in ascending order (smallest z to greatest z)
     * source: https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/SortedSpriteTest.java
     */
    public static class ZIndexComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            ZIndexComponent s1 = (ZIndexComponent) e1.getComponent(ZIndexComponent.ID);
            ZIndexComponent s2 = (ZIndexComponent) e2.getComponent(ZIndexComponent.ID);

            return (s2.index - s1.index) < 0 ? 1 : -1;
        }
    }
}
