package com.megagames.tryphysics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megagames.tryphysics.SplashScreen;


public class TryPhysicsGame extends Game {

	public static final int V_WIDTH = 16*120;
	public static final int V_HEIGHT = 9*120;

	public GameScreen gameScreen;
	public IntroScreen introScreen;
	public SplashScreen splashScreen;

	public OrthographicCamera camera;


	@Override
	public void create() {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		gameScreen = new GameScreen();
		introScreen = new IntroScreen();
		splashScreen = new SplashScreen(this);
		setScreen(introScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
		introScreen.dispose();
		splashScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
//		gameScreen.resize(width, height);
	}
}
