package com.goatgames.goatengine.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ezware.dialog.task.TaskDialogs;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.GenericGame;
import com.goatgames.goatengine.utils.DesktopExceptionHandler;

import javax.swing.*;
import java.awt.*;


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

        // Load configuration
		GEConfig.loadConfig();

		cfg.title = GEConfig.DevGeneral.GAME_NAME;
		cfg.width = GEConfig.DevGeneral.VIEWPORT_WIDTH;
		cfg.height = GEConfig.DevGeneral.VIEWPORT_HEIGHT;
		cfg.fullscreen = GEConfig.DevGeneral.FULLSCREEN;

        cfg.vSyncEnabled = false;
       // cfg.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
        cfg.backgroundFPS = 60; // Setting to 0 disables background fps throttling

        cfg.useGL30 = false;
        cfg.forceExit = true;
        cfg.vSyncEnabled = false;

		new LwjglApplication(new GenericGame(), cfg);
    }
}
