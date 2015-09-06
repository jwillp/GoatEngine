package com.brm.goatengine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "GOAT ENGINE";
		cfg.width = 840; //1920;
		cfg.height = 680; //1080;
		cfg.fullscreen = false;

		new LwjglApplication(new GenericGame(), cfg);
	}
}
