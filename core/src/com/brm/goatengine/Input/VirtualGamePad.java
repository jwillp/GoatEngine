package com.brm.GoatEngine.Input;

import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A virtual gamepad to be used by the AI
 * This makes an entity controllable (it can use the buttons to shoot, to attack to move etc..)
 * This is a Component it must therfore be attached to an entity
 */
public class VirtualGamePad extends EntityComponent {

    public final static String ID = "VIRTUAL_GAME_PAD";

    @Override
    public String getId() {
        return ID;
    }

    public enum InputSource{ USER_INPUT, AI_INPUT }

    private ArrayList<VirtualButton> justReleasedButtons; //A list of the buttons that where just released
    private ArrayList<VirtualButton> pressedButtons; //A list of the pressed buttons of the gamepad
    public InputSource inputSource; // Who makes the input (AI or User?)



    public VirtualGamePad(InputSource inputSource){
        super(true);
        this.setInputSource(inputSource);
        this.pressedButtons = new ArrayList<VirtualButton>();
        this.justReleasedButtons = new ArrayList<VirtualButton>();
    }


    /**
     * Method simulating a button press
     * @param btn
     */
    public void pressButton(VirtualButton btn){
        this.pressedButtons.add(btn);
    }

    /**
     * Method simulating a button release
     */
    public void releaseButton(VirtualButton btn){
        this.pressedButtons.remove(btn);
        this.justReleasedButtons.add(btn);
    }

    /**
     * Releases a collection of buttons
     * @param buttons
     */
    public void releaseButtons(Collection<VirtualButton> buttons){
        this.pressedButtons.removeAll(buttons);
    }

    /**
     * Releases all pressed button
     */
    public void releaseAll(){
        this.pressedButtons.clear();
        this.justReleasedButtons.clear();
    }


    /**
     * Returns whether or not a button is pressed
     * @param btn
     * @return
     */
    public boolean isButtonPressed(VirtualButton btn){
        return this.pressedButtons.contains(btn);
    }

    /**
     * Returns whether or not there is any button pressed
     * If there is no button press returns true otherwise false
     * @return
     */
    public boolean isAnyButtonPressed(){
        return !this.pressedButtons.isEmpty();
    }



    /**
     * Returns whether or not a button was just released
     * @param btn
     * @return
     */
    public boolean isButtonJustReleased(VirtualButton btn){
        return this.justReleasedButtons.contains(btn);
    }







    // GETTERS && SETTERS //
    /**
     * Returns the Input Source
     * @return
     */
    public InputSource getInputSource() {
        return inputSource;
    }

    private void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public ArrayList<VirtualButton> getPressedButtons() {
        return pressedButtons;
    }


    @Override
    public void onDetach(Entity entity) {

    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        return new HashMap<String, String>();
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> pod) {

    }
}
