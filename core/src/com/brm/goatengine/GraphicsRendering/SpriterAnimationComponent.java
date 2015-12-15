package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Spriter;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Files.FileSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * An animation component specialy made for Spriter animations
 */
public class SpriterAnimationComponent extends EntityComponent {
    public final static String ID = "SPRITER_ANIMATION_COMPONENT";
    private String animationFile;

    private Player player;

    private float offsetX;
    private float offsetY;
    private float scale;

    private boolean isComplete = false;

    /**
     * Ctor
     * @param animFile
     * @param spriterEntityName
     * @param offsetX
     * @param offsetY
     * @param scale
     */
    public SpriterAnimationComponent(String animFile, String spriterEntityName, float offsetX, float offsetY, float scale){
        super(true);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scale = scale;
        animationFile = animFile;
        Spriter.load(Gdx.files.internal(animFile).read(), animFile);
        player = Spriter.newPlayer(animFile, spriterEntityName);

        this.createPlayerListener();
    }

    public SpriterAnimationComponent(Map<String, String> map) {
        super(map);
    }


    public Player getPlayer() {
        return this.player;
    }


    public void setAnimation(String name){
        isComplete = false;
        this.player.setAnimation(name);
    }

    public com.brashmonkey.spriter.Animation getAnimation() {
        return this.player.getAnimation();
    }


    public boolean isComplete(){
        return isComplete;
    }


    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }



    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String>  makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("offset_x", String.valueOf(offsetX));
        map.put("offset_y", String.valueOf(offsetY));
        map.put("scale", String.valueOf(scale));
        map.put("animation_file", this.animationFile);
        map.put("animation_title", this.getAnimation().name);
        map.put("entity_name", this.getPlayer().getEntity().name);
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map){
        this.offsetX = Float.parseFloat(map.get("offset_x"));
        this.offsetY = Float.parseFloat(map.get("offset_y"));
        this.scale = Float.parseFloat(map.get("scale"));
        animationFile = map.get("animation_file");
        Spriter.load(FileSystem.getFile(map.get("animation_file")).read(), map.get("animation_file"));

        // We only create a new player if there is not already one
        if(player == null){
            try{
                player = Spriter.newPlayer(map.get("animation_file"), map.get("entity_name"));
            }catch (java.lang.ArrayIndexOutOfBoundsException e){
                throw new SpriterEntityNotFoundException(map.get("entity_name"));
            }
        }

        // We'll specify the animation to play if it was provided, else we use the default one

        if(map.containsKey("animation_title")){
            String animationTitle = map.get("animation_title");
            if(player.getEntity().getAnimation(animationTitle) != null){
                player.setAnimation(animationTitle);
            }
        }

    }


    /**
     * Adds a Player Listener to the current player
     */
    private void createPlayerListener(){
        this.player.addListener(new Player.PlayerListener() {
            @Override
            public void animationFinished(com.brashmonkey.spriter.Animation animation){
                SpriterAnimationComponent.this.isComplete = true;
            }

            @Override
            public void animationChanged(com.brashmonkey.spriter.Animation oldAnim, com.brashmonkey.spriter.Animation newAnim){
                SpriterAnimationComponent.this.isComplete = false;
            }

            @Override
            public void preProcess(Player player) {}

            @Override
            public void postProcess(Player player) {}

            @Override
            public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {}
        });
    }


    @Override
    public void onDetach(Entity entity) {
        // Spriter.remove(this.player); // TODO Update spriter library
    }

    @Override
    public String getId() {
        return ID;
    }

    private class SpriterEntityNotFoundException extends RuntimeException {
        public SpriterEntityNotFoundException(String entityName) {
            super("Spriter Aniamtion Entity not found: " + entityName);
        }
    }
}