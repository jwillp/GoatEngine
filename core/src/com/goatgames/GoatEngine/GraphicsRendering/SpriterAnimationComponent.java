package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Spriter;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * An animation component specially made for Spriter animations
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

    public SpriterAnimationComponent(NormalisedEntityComponent data) {
        super(data);
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

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.put("offset_x", String.valueOf(offsetX));
        data.put("offset_y", String.valueOf(offsetY));
        data.put("scale", String.valueOf(scale));
        data.put("animation_file", this.animationFile);
        data.put("animation_title", this.getAnimation().name);
        data.put("entity_name", this.getPlayer().getEntity().name);
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data){
        super.denormalise(data);
        this.offsetX = Float.parseFloat(data.get("offset_x"));
        this.offsetY = Float.parseFloat(data.get("offset_y"));
        this.scale = Float.parseFloat(data.get("scale"));
        animationFile = data.get("animation_file");
        Spriter.load(GoatEngine.fileManager.getFileHandle(data.get("animation_file")).read(), data.get("animation_file"));

        // We only create a new player if there is not already one
        if(player == null){
            try{
                player = Spriter.newPlayer(data.get("animation_file"), data.get("entity_name"));
            }catch (java.lang.ArrayIndexOutOfBoundsException e){
                throw new SpriterEntityNotFoundException(data.get("entity_name"));
            }
        }

        // We'll specify the animation to play if it was provided, else we use the default one
        if(data.containsKey("animation_title")){
            String animationTitle = data.get("animation_title");
            if(player.getEntity().getAnimation(animationTitle) != null){
                player.setAnimation(animationTitle);
            }
        }
    }

    /**
     * Used to clone a component
     *
     * @return
     */
    @Override
    public EntityComponent clone() {
        return new SpriterAnimationComponent(normalise());
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

    public String getAnimationFile() {
        return animationFile;
    }

    private class SpriterEntityNotFoundException extends RuntimeException {
        public SpriterEntityNotFoundException(String entityName) {
            super("Spriter Aniamtion Entity not found: " + entityName);
        }
    }
}
