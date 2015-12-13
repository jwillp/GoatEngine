package com.brm.GoatEngine.GraphicsRendering;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * A particle Effect
 */
public class GParticleEmitter extends ParticleEffect {

    private boolean isLooping = false;

   /*@Override
    public void update(float delta) {
        super.update(delta);
        if(isLooping()){
            this.reset();
        }
    }*/

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }
}