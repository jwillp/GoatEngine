package com.brm.GoatEngine.ECS.common;

import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * Used to add tags to an entity
 */
public class TagsComponent extends EntityComponent {

    public static final String ID = "TAGS_COMPONENT";
    private HashSet<String> tags = new HashSet<String>();

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
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String>  map = new HashMap<String, String>();
        map.put("tags", tags.toString());
        return map;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map){

        tags = new HashSet<String>(Arrays.asList(map.get("tags").replace("\\[|\\]", "").split(";")));
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
}
