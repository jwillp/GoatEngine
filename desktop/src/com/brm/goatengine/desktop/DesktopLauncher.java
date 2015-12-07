package com.brm.GoatEngine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brm.GoatEngine.GEConfig;


public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		// Load configuration
		GEConfig.loadConfig();

		cfg.title = GEConfig.DevGeneral.GAME_NAME;
		cfg.width = GEConfig.DevGeneral.VIEWPORT_WIDTH;
		cfg.height = GEConfig.DevGeneral.VIEWPORT_HEIGHT;
		cfg.fullscreen = GEConfig.DevGeneral.FULLSCREEN;


		new LwjglApplication(new GenericGame(), cfg);
	}
}
