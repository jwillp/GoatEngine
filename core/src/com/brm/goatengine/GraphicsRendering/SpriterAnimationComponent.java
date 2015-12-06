package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.brm.GoatEngine.ECS.core.EntityComponent;

/**
 * An animation component specialy made for Spriter animations
 */
public class SpriterAnimationComponent extends EntityComponent {
    public final static String ID = "SPRITER_ANIMATION_COMPONENT";

    private Player player;

    private float alpha = 1; //alpha level

    private float offsetX;
    private float offsetY;
    private float scale;

    private Color color = Color.WHITE;

    private boolean isComplete = false;

    public SpriterAnimationComponent(Player player) {
        this(player,0,0,1);
    }

    public SpriterAnimationComponent(Player player, float offsetX, float offsetY, float scale){
        this.player = player;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scale = scale;

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




    public SpriterAnimationComponent(XmlReader.Element element){
        this.deserialize(element);
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
     * Deserialize a component
     *
     * @param componentData the data as an XML element
     */
    @Override
    public void deserialize(Element componentData) {

    }
}