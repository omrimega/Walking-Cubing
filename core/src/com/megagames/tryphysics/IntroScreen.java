package com.megagames.tryphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class IntroScreen implements Screen {

    //screen
    private OrthographicCamera camera;
    private Viewport viewport;

    private Texture logo;
    private Image intro;

    private Batch batch;

    //world parameters
    private final int WORLD_WIDTH = Gdx.graphics.getWidth();
    private final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    IntroScreen() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH/2, WORLD_HEIGHT/2);

        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        logo = new Texture("intro.png");
        intro = new Image(logo);
        intro.setOrigin(intro.getWidth() / 2, intro.getHeight() / 2);
        intro.setPosition(WORLD_WIDTH/2-intro.getWidth()/2, WORLD_HEIGHT/2-intro.getHeight()/2);

        batch = new SpriteBatch();


        }


    @Override
    public void render(float delta) {

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            System.out.println("X: " + Gdx.input.getX());
            System.out.println("Y: " + Gdx.input.getY());
            System.out.println("camera: " + camera.position.y);
        }

        //graphics
        Gdx.gl.glClearColor(255f, 255f, 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        intro.act(20f);

        batch.begin();
        intro.draw(batch, 1);
        //batch.draw(logo, WORLD_WIDTH/2,WORLD_HEIGHT/2-logo.getHeight()/2, logo.getWidth(),logo.getHeight());
        batch.end();

    }

    @Override
    public void show() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.fadeIn(1500f));
        intro.setColor(1f,1f,1f,0f);
        intro.addAction(sequenceAction);
        sequenceAction.addAction(Actions.fadeOut(1500f));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        logo.dispose();
    }

}
