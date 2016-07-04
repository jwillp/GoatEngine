package com.goatgames.goatengine.desktop.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.input.InputManager;
import com.goatgames.goatengine.desktop.input.events.gamepad.*;

/**
 * Manages Game Controllers (GamePads)
 */
public class GamePadManager implements ControllerListener{

    private final InputManager inputManager;

    private Array<Controller> availableControllers; // List of controllers that are not being used
    private Array<Controller> capturedControllers; // List of controllers captured for use by the engine
    Xbox360Map xbox360Map = new Xbox360Map();

    private Array<VirtualGamePad> virtualGamePads;

    public GamePadManager(InputManager inputManager){
        this.inputManager = inputManager;
    }

    public void init(){
        // This initialisation must be delayed as the Controllers object depends on Libgdx's initialisation

        availableControllers = Controllers.getControllers();
        // Detect Available User Game Pad Maps

        virtualGamePads = new Array<>(availableControllers.size);
        for(int i=0; i<availableControllers.size; i++){
            Controller ctrl = availableControllers.get(i);
            virtualGamePads.add(new VirtualGamePad(ctrl,new Xbox360Map()));
        }

    }


    public void update(){

    }

    /**
     * Makes a Controller available again
     */
    public void releaseController(Controller controller){
        this.availableControllers.add(controller);
    }

    /**
     * Removes a controller from the available list
     * and returns it
     */
    public Controller captureController(){
        try{
            Controller controller = this.availableControllers.first();
            this.availableControllers.removeIndex(0);
            return controller;
        }catch(IllegalStateException e){
            throw new NoControllersAvailableException();
        }
    }

    /**
     * A {@link com.badlogic.gdx.controllers.Controller} got connected.
     *
     * @param controller
     */
    @Override
    public void connected(Controller controller) {
        if(!this.availableControllers.contains(controller, true)){
            this.availableControllers.add(controller);
        }
        int gamePadId = getControllerId(controller);
        GoatEngine.eventManager.fireEvent(new GamePadConnectedEvent(gamePadId));
        GoatEngine.logger.debug("CONTROLLER CONNECTED! " + controller.getName());
    }

    /**
     * A {@link com.badlogic.gdx.controllers.Controller} got disconnected.
     *
     * @param controller
     */
    @Override
    public void disconnected(Controller controller) {
        if(this.availableControllers.contains(controller, true)){
            this.availableControllers.removeValue(controller, true);
        }
        int gamePadId = getControllerId(controller);
        GoatEngine.eventManager.fireEvent(new GamePadDisconnectedEvent(gamePadId));
    }

    /**
     * A button on the {@link com.badlogic.gdx.controllers.Controller} was pressed. The buttonCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts button constants for known controllers.
     *
     * @param controller
     * @param buttonCode
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        int gamePadId = getControllerId(controller);
        GamePadMap.Button button = translateButtonRawCode(gamePadId,buttonCode);
        virtualGamePads.get(gamePadId).pressButton(button);
        GoatEngine.eventManager.fireEvent(new GamePadButtonPressedEvent(gamePadId, button,buttonCode));
        return true;
    }

    /**
     * A button on the {@link com.badlogic.gdx.controllers.Controller} was released. The buttonCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts button constants for known controllers.
     *
     * @param controller
     * @param buttonCode
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        int gamePadId = getControllerId(controller);
        GamePadMap.Button button = translateButtonRawCode(gamePadId,buttonCode);
        virtualGamePads.get(gamePadId).releaseButton(button);
        GoatEngine.eventManager.fireEvent(new GamePadButtonReleasedEvent(gamePadId, button,buttonCode));
        return true;
    }

    /**
     * An axis on the {@link com.badlogic.gdx.controllers.Controller} moved. The axisCode is controller specific. The axis value is in the range [-1, 1]. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts axes constants for known controllers.
     *
     * @param controller
     * @param axisCode
     * @param value      the axis value, -1 to 1
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        int gamePadId = getControllerId(controller);
        GamePadMap.Axis axis =  translateAnalogStickRawCode(gamePadId,axisCode);
        if(axis == GamePadMap.Axis.UNMAPPED) return false; // As if nothing had happened
        GoatEngine.eventManager.fireEvent(new AxisMovedEvent(gamePadId, axis, value));
        return true;
    }

    /**
     * A POV on the {@link com.badlogic.gdx.controllers.Controller} moved. The povCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts POV constants for known controllers.
     *
     * @param controller
     * @param povCode
     * @param value
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        int gamePadId = getControllerId(controller);
        int rawCode = povCode;
        GamePadMap.Button button = translateButtonRawCode(gamePadId,povCode);
        if(button == GamePadMap.Button.UNMAPPED) return false; // As if nothing happened
        GoatEngine.eventManager.fireEvent(new DPADMovedEvent(gamePadId, rawCode, value));
        return true;
    }

    /**
     * An x-slider on the {@link com.badlogic.gdx.controllers.Controller} moved. The sliderCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts slider constants for known controllers.
     *
     * @param controller
     * @param sliderCode
     * @param value
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    /**
     * An y-slider on the {@link com.badlogic.gdx.controllers.Controller} moved. The sliderCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts slider constants for known controllers.
     *
     * @param controller
     * @param sliderCode
     * @param value
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    /**
     * An accelerometer value on the {@link com.badlogic.gdx.controllers.Controller} changed. The accelerometerCode is controller specific. The
     * <code>com.badlogic.gdx.controllers.mapping</code> package hosts slider constants for known controllers. The value is a
     * {@link com.badlogic.gdx.math.Vector3} representing the acceleration on a 3-axis accelerometer in m/s^2.
     *
     * @param controller
     * @param accelerometerCode
     * @param value
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    /**
     * Translate a Raw button code to a standard virtual one
     * for a certain controller
     * @param controllerId the controller for which we need to do the translation
     * @param rawCode the raw button code
     * @return the standard Button
     */
    private GamePadMap.Button translateButtonRawCode(int controllerId, int rawCode){

        // Depending on the controller
        Controller ctrl = this.availableControllers.get(controllerId);
        if(ctrl.getName().contains("360")){
            return this.xbox360Map.getButton(rawCode);
        }
        return GamePadMap.Button.UNMAPPED;
    }

    /**
     * Translate a raw analog stick code to a standard virtual one
     * for a certain controller
     * @param controllerId the controller for which we need to do the translation
     * @param rawCode the raw button code
     * @return the standard analog stick name
     */
    private GamePadMap.Axis translateAnalogStickRawCode(int controllerId, int rawCode){
        Controller ctrl = this.availableControllers.get(controllerId);
        if(ctrl.getName().contains("360")){
            return this.xbox360Map.getAxis(rawCode);
        }
        return GamePadMap.Axis.UNMAPPED;
    }

    /**
     * Returns the controller id of a certain controller
     * @param controller the controller for which we want to get the id
     * @return the id of the controller
     */
    private int getControllerId(Controller controller){
        return availableControllers.indexOf(controller, true);
    }

    /**
     * Returns A VirtualGamePad Instance, or null if not found
     * @param id id of the game Pad to return
     * @return VirtualGamePad Instance, if not found returns null
     */
    public VirtualGamePad getGamePad(int id){
        try{
            return virtualGamePads.get(id);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * Thrown when there are no Controllers available
     */
    public class NoControllersAvailableException extends RuntimeException{

        public NoControllersAvailableException(){
            super("No controllers where found");
        }
    }
}
