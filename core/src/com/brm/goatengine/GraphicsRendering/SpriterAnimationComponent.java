package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Spriter;
import com.brm.GoatEngine.ECS.core.EntityComponent;

/**
 * An animation component specialy made for Spriter animations
 */
public class SpriterAnimationComponent extends EntityComponent {
    public final static String ID = "SPRITER_ANIMATION_COMPONENT";

    public class SpriterAnimationComponentPOD extends EntityComponentPOD{
        @SerializeName("offset_x")
        public float offsetX;
        @SerializeName("offset_y")
        public float offsetY;

        public float scale;

        @SerializeName("animation_file")
        public String animationFile;

        @SerializeName("entity_name")
        public String entityName;
    }



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
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scale = scale;

        Spriter.load(Gdx.files.internal(animFile).read(), animFile);
        player = Spriter.newPlayer(animFile, spriterEntityName);

        this.createPlayerListener();
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
    protected EntityComponentPOD makePOD() {
        SpriterAnimationComponentPOD pod = new SpriterAnimationComponentPOD();
        pod.offsetX = offsetX;
        pod.offsetY = offsetY;
        pod.scale = scale;
        pod.animationFile = this.getAnimation().name;
        pod.entityName = this.getPlayer().getEntity().name;
        return pod;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromPOD(EntityComponentPOD pod){
        SpriterAnimationComponentPOD animPod = (SpriterAnimationComponentPOD) pod;
        this.offsetX = animPod.offsetX;
        this.offsetY = animPod.offsetY;
        this.scale = animPod.scale;

        Spriter.load(Gdx.files.internal(animPod.animationFile).read(), animPod.animationFile);
        player = Spriter.newPlayer(animPod.animationFile, animPod.entityName);
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
    public String getId() {
        return ID;
    }
}