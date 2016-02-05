package com.goatgames.goatengine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.GenericGame;

import javax.swing.*;


public class DesktopLauncher {
	public static void main (String[] arg) {




        // SETUP SOME DESKTOP SPECIFIC

        // Catch exceptions to be displayed in a dialog
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

        cfg.width = 0;
        cfg.height = 0;

       // cfg.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
        cfg.backgroundFPS = 60; // Setting to 0 disables background fps throttling

        cfg.useGL30 = false;
        cfg.forceExit = true;
        cfg.vSyncEnabled = false;

		new LwjglApplication(new GenericGame(), cfg);
    }
}
