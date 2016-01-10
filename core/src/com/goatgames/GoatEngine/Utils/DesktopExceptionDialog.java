package com.goatgames.goatengine.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import javax.swing.*;

/**
 * Gui Dialog to be thrown when an exception occurs
 */
public class DesktopExceptionDialog {
    public static void show(String message){
        if(Gdx.app.getType() == Application.ApplicationType.Desktop){
            JOptionPane.showMessageDialog(null, message);
        }
    }

    public static void uncheckedShow(String message){
            JOptionPane.showMessageDialog(null, message);
    }
}
