package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.utils.NotImplementedException;

import java.util.ArrayList;


/**
 * Used to emit particles from the entity's position
 */
public class ParticleEmitterComponent extends EntityComponent{

    public final static String ID = "PARTICLE_EMITTER_COMPONENT";

    private ArrayList<GParticleEmitter> effects = new ArrayList<GParticleEmitter>();

    private float alpha = 1;

    public ParticleEmitterComponent(){
        super(true);
    }

    public ParticleEmitterComponent(NormalisedEntityComponent data) {
        super(data);
    }

    /**
     * Adds a new Particle effect to the list
     * @param particle
     * @param pos
     * @param startNow
     */
    public void addEffect(FileHandle particle, Vector2 pos, boolean startNow){
        GParticleEmitter effect = new GParticleEmitter();
        effect.load(particle, Gdx.files.internal("particles/"));
        effect.setPosition(pos.x, pos.y);
        if(startNow){
            effect.start();
        }
        effects.add(effect);
    }

    /**
     * Adds a new particle effect starting right away
     * @param particle
     * @param pos
     */
    public void addEffect(FileHandle particle, Vector2 pos){
        addEffect(particle, pos, true);
    }

    public ArrayList<GParticleEmitter> getEffects() {
        return effects;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public NormalisedEntityComponent normalise() {
        //NormalisedEntityComponent data = super.normalise();
        // TODO complete
        throw new NotImplementedException();
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        // TODO complete
        throw new NotImplementedException();
    }

    @Override
    public String getId() {
        return ID;
    }
}
