package com.goatgames.goatengine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.goatgames.goatengine.GenericGame;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.files.GdxFileManager;

import javax.swing.*;


public class GEDesktopLauncher {

    /**
     * Runs the goat engine for a Desktop configuration
     * @param gameSpecificListener : For use by NCB, can be null
     */
    public void run(GameEventListener gameSpecificListener){
        // SETUP SOME DESKTOP SPECIFIC

        // Catch exceptions to be displayed in a dialog
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // GoatEngine
        GoatEngine.fileManager = new GdxFileManager();

        // Libgdx application
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

        cfg.width = 0;
        cfg.height = 0;

        // cfg.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
        cfg.backgroundFPS = 60; // Setting to 0 disables background fps throttling

        cfg.useGL30 = false;
        cfg.forceExit = true;
        cfg.vSyncEnabled = false;

        new LwjglApplication(new GenericGame(gameSpecificListener), cfg);
    }

	public static void main (String[] arg) {
        GEDesktopLauncher GEDesktopLauncher = new GEDesktopLauncher();
        GEDesktopLauncher.run(null);
    }
}
