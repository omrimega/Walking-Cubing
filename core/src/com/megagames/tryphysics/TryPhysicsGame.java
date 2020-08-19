package com.megagames.tryphysics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.awt.Menu;


public class TryPhysicsGame extends Game {

	public GameScreen gameScreen;
	public IntroScreen introScreen;
	public MenuScreen menuScreen;

	public OrthographicCamera camera;

	public String name = "";


	@Override
	public void create() {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

		gameScreen = new GameScreen(this);
		introScreen = new IntroScreen(this);
		menuScreen = new MenuScreen(this);

		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		gameScreen.dispose();
		introScreen.dispose();
		menuScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
//		gameScreen.resize(width, height);
		//introScreen.resize(width, height);
	}
}
