package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

import java.util.*;


/**
 * Used to add tags to an entity
 */
public class TagsComponent extends EntityComponent {

    public static final String ID = "TAGS_COMPONENT";
    private HashSet<String> tags;

    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public TagsComponent(Map<String, String> map) {
        super(map);
    }

    public TagsComponent(boolean b) {
        super(b);
        tags = new HashSet<String>();
    }


    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        // Convert array to csv
        String csv = this.tags.toString().replace(", ", ";").replace("[", "").replace("]", "");
        map.put("tags", csv);
        return map;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map){

        // Convert
        if(map.get("tags").equals("")){
            tags = new HashSet<String>();
        }else{
            tags = new HashSet<String>(Arrays.asList(map.get("tags").split(";")));
        }
    }

    /**
     * Adds a new tag to an entity
     * @param tag
     */
    public void addTag(String tag){
        this.tags.add(tag);
    }

    /**
     * Removes a tag from the entity
     * @param tag
     */
    public void removeTag(String tag){
        this.tags.remove(tag);
    }

    /**
     * Returns whether or not the entity has a certain tag
     * @param tag
     * @return
     */
    public boolean hasTag(String tag){
        return this.tags.contains(tag);
    }

    /**
     * Returnas all the tags of the entity
     * @return
     */
    public HashSet<String> getTags() {
        return tags;
    }

    @Override
    public String getId() {
        return ID;
    }


    // FACTORY //
    public static class Factory implements EntityComponentFactory {
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map){
            GAssert.that(componentId.equals(TagsComponent.ID),
                    "Component Factory Mismatch: TagsComponent.ID != " + componentId);
            TagsComponent component = new TagsComponent(map);
            return component;
        }
    }









}
