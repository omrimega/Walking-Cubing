package com.megagames.tryphysics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.megagames.tryphysics.TypesOfPlayer.Omri;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Menu;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class TryPhysicsGame extends Game {

	public IntroScreen introScreen;
	public MenuScreen menuScreen;
	public GameScreen gameScreen;

	public OrthographicCamera camera;

	public String name = "";
	public String type = "omri.png";

	public Socket socket;

	public HashMap<String, Omri> players;

	public Stage stages;


	@Override
	public void create() {


		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

		introScreen = new IntroScreen(this);
		menuScreen = new MenuScreen(this);

//		players = new HashMap<String, Omri>();

//		ConnectSocket();
//		configSocketEvents();
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		introScreen.dispose();
		menuScreen.dispose();
		gameScreen.dispose();
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

	public void ConnectSocket(){
		try {
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		} catch (Exception e){
			System.out.println(e);
		}
	}

	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + id);
				}catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connect: " + id);
					players.put(id, new Omri(gameScreen));
				}catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New PlayerID");
				}
			}
		});
	}


}
