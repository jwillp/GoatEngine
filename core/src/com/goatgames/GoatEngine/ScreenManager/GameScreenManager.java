package com.goatgames.goatengine.screenmanager;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;

import java.util.Stack;

/**
 *
 * @author fireraccoon
 *
 *  We have a stack of screens so we play the screen at the top of the list
 *
 */
public class GameScreenManager {

    private IGameScreenLoader screenLoader;

    // ATTRIBUTES //
    private Stack<IGameScreen> screens = new Stack<IGameScreen>();
    private boolean isRunning;


    public GameScreenManager(IGameScreenLoader loader){
        this.screenLoader = loader;
    }

    // METHODS //

    /**
     * Initialises the manager
     */
    public void init() {
        this.isRunning = true;
        String mainScreenName = GoatEngine.config.screen.mainScreen;

        // Possible causes for this:
        //  - The GEImplSpecs for ScreenLoader returns null.
        if(!GAssert.notNull(screenLoader, "screenLoader == null")) return;

        // Possible causes for this:
        //  - The ScreenLoader does not recognise the screen name:
        //    Maybe there is a typo in the screen loader or in the configuration
        //    for the given screen screen.
        IGameScreen mainScreen = screenLoader.load(mainScreenName);
        if(!GAssert.notNull(mainScreen, "mainScreen == null")) return;

        this.addScreen(mainScreen);
    }

    /**
     * Does necessary clean ups of the manager
     */
    public void cleanUp() {
        GoatEngine.logger.info("Screen Manager cleaning up");
    }



    public boolean isRunning() {
        return this.isRunning;
    }


    // Screen Management //
    private void changeScreen(IGameScreen screen){

        if(!this.screens.isEmpty()){
            this.screens.peek().cleanUp();
            this.screens.pop(); //We delete it;
        }
        this.screens.push(screen);
        this.screens.peek().init(this);
    }

    /**
     * Changes a screen
     * @param screenPath
     */
    public void changeScreen(String screenPath){
        IGameScreen screen = screenLoader.load(screenPath);
        changeScreen(screen);
    }

    /**
     * Adds a screen on top of the list , so it poses the currently running screen
     * @param screen
     */
    private void addScreen(IGameScreen screen){
        GoatEngine.logger.info("> Game Engine adding Screen ...");
        if(!this.screens.isEmpty())
            this.screens.peek().pause(this);

        this.screens.push(screen);
        this.screens.peek().init(this);
        GoatEngine.logger.info("> Game Engine Screen Added");
    }

    /**
     * Adds a screen on top of the list , so it poses the currently running screen
     * @param screenPath path to the screen config file
     */
    public void addScreen(String screenPath){
        IGameScreen screen = screenLoader.load(screenPath);
        addScreen(screen);
    }

    /**
     * delete last game screen in the stack
     */
    public void popScreen(){
        if(!this.screens.isEmpty()){
            this.screens.peek().cleanUp();
            this.screens.pop();
        }
        //if we still have another screen we resume it
        if(!this.screens.isEmpty()){
            this.screens.peek().resume(this);
        }
    }


    // LOOP //
    public void handleEvents() {
        if(!this.screens.empty()){
            this.screens.peek().preUpdate(this);
        }else{
            handleEmptyStack();
        }

    }

    public void update(float deltaTime) {
        if(!this.screens.empty()){
            this.screens.peek().update(this, deltaTime);
        }else{
            handleEmptyStack();
        }

    }

    public void draw(float deltaTime) {
        if(!this.screens.empty()){
            this.screens.peek().draw(this, deltaTime);
        }else{
            handleEmptyStack();
        }
    }


    /**
     * Pauses the whole manager until it resumes
     */
    public void pause(){
        this.isRunning = false;
    }

    /**
     * Resumes the manager after a pause
     */
    public void resume(){
        this.isRunning = true;
    }

    /**
     * Returns the current screen
     * @return
     */
    public IGameScreen getCurrentScreen() {
        if(!this.screens.empty()){
            return this.screens.peek();
        }else{
           handleEmptyStack();
           return null;
        }
    }

    private void handleEmptyStack(){
        /*if(GEConfig.ScreenManager.ON_EMPTY_STACK.equals(GEConfig.ScreenManager.FATAL)){*/
            EmptyScreenManagerException ex = new EmptyScreenManagerException();
            GoatEngine.logger.fatal(ex.getMessage());
            throw ex;
        /*}else if(GEConfig.ScreenManager.ON_EMPTY_STACK.equals(GEConfig.ScreenManager.EXIT)){
            // TODO notify GoatEngine that we want to quit
        }*/
    }

    /**
     * Exception thrown when the manager's stack is empty
     */
    public class EmptyScreenManagerException extends RuntimeException{
        public EmptyScreenManagerException(){
            super("There is no Game Screen in the Screen Manager's stack");
        }
    }

}
