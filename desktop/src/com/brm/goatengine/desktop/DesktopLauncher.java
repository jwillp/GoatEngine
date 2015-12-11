package com.brm.GoatEngine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brm.GoatEngine.GEConfig;
import org.ini4j.Ini;


public class DesktopLauncher {
	public static void main (String[] arg) {

        Ini ini = new Ini();

        // lets add a section, it will create needed intermediate sections as well
        ini.add("root/child/sub");

        //
        Ini.Section root;
        Ini.Section sec;

        root = ini.get("root");
        sec = root.getChild("child").getChild("sub");

        // or...
        sec = root.lookup("child", "sub");

        // or...
        sec = root.lookup("child/sub");

        // or even...
        sec = ini.get("root/child/sub");


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
