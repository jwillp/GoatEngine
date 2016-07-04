package com.goatgames.goatengine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Global input manager
 */
public class InputManager{

    protected final GestureManager gestureManager;
    protected final InputMultiplexer multiplexer;

    public InputManager(){
        multiplexer = new InputMultiplexer();
        gestureManager = new GestureManager(this);
    }

    /**
     * Initialises the input manager
     */
    public void init(){
        multiplexer.addProcessor(new GestureDetector(gestureManager)); // Do not call addInputProcessor for this
        setInputProcessor(multiplexer);
    }

    /**
     * Adds a new input processor
     * @param processor the input processor to add
     */
    public void addInputProcessor(InputProcessor processor){
        multiplexer.addProcessor(processor);
    }

    /**
     * Removes an input processor from the manager
     * @param processor the input processor to remove
     */
    public void removeInputProcessor(InputProcessor processor){
        multiplexer.removeProcessor(processor);
    }

    /**
     * Lets an input processor reserve all input
     */
    public void reserve(InputProcessor processor){
        setInputProcessor(processor);
    }

    /**
     * When an input processor has reserved all input give the control back
     * to the other processors
     */
    public void release(){
        setInputProcessor(multiplexer);
    }

    /**
     * Sets the global input processor
     * @param processor the processor to use as global input processor
     */
    private void setInputProcessor(final InputProcessor processor){
        Gdx.input.setInputProcessor(processor);
    }

    /**
     * Returns the number of processors
     * @return the number of processors in use by the input manager
     */
    public int getProcessorCount() {
        return multiplexer.size();
    }
}
