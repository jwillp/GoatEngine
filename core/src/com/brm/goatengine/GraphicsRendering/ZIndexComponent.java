package com.brm.GoatEngine.GraphicsRendering;

import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.Comparator;
import java.util.Map;

/**
 * Used to Index Displayable things
 */
public class ZIndexComponent extends EntityComponent{

    public final static String ID = "Z_INDEX_COMPONENT";

    public int index; // z index, lowest the more in the background


    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public ZIndexComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return the map built
     */
    @Override
    protected Map<String, String> makeMap() {
        EntityComponentMap map = new EntityComponentMap();
        map.put("z_index", String.valueOf(this.index));
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map){
        index = Integer.parseInt(map.get("z_index"));

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
