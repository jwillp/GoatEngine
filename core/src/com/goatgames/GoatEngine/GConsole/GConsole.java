package com.goatgames.goatengine.gconsole;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.strongjoshua.console.Console;

/**
 * A custom GConsole with some more functionalities
 */
public class GConsole extends Console {

    public GConsole(){
        super();
        // Position at top of screen
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/4);
        this.setPosition(0,Gdx.graphics.getHeight());
    }


    /**
     * Changes the opacity of the console
     * @param alpha
     */
    public void setOpacity(float alpha){
        Color c = this.consoleWindow.getColor();
        this.consoleWindow.setColor(c.r, c.b, c.b, alpha);
    }

    /**
     * Logs a certain string, can specify logLevel using a string
     * useful for Javascript
     * @param msg
     * @param levelString
     */
    public <T> void log(T msg, String levelString){
        this.log(msg.toString(), LogLevel.valueOf(levelString));
    }

}
