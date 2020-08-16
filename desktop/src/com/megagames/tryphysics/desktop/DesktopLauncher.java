package com.megagames.tryphysics.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.megagames.tryphysics.TryPhysicsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 800*1;
		config.width = config.height*16/9*1;
		config.fullscreen = false;
		config.backgroundFPS = 60;
		config.foregroundFPS =60;
		new LwjglApplication(new TryPhysicsGame(), config);
	}
}
