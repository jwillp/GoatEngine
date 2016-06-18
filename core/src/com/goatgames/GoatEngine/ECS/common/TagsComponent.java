package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Used to add tags to an entity
 */
public class TagsComponent extends EntityComponent {

    public static final String ID = "TAGS_COMPONENT";
    private HashSet<String> tags;

    public TagsComponent(NormalisedEntityComponent data) {
        super(data);
    }

    public TagsComponent(boolean enabled) {
        super(enabled);
        tags = new HashSet<>();
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        // Convert array to csv
        String csv = this.tags.toString().replace(", ", ";").replace("[", "").replace("]", "");
        data.put("tags", csv);
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data){
        super.denormalise(data);
        if(data.get("tags").equals("")){
            tags = new HashSet<String>();
        }else{
            tags = new HashSet<String>(Arrays.asList(data.get("tags").split(";")));
        }
    }

    @Override
    public EntityComponent clone() {
        return new TagsComponent(normalise());
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
