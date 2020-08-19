package com.megagames.tryphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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

    private TryPhysicsGame gamein;

    //world parameters
    private final int WORLD_WIDTH = Gdx.graphics.getWidth();
    private final int WORLD_HEIGHT = Gdx.graphics.getHeight();


    IntroScreen(TryPhysicsGame gameout) {

        gamein = gameout;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        logo = new Texture("intro.png");

        intro = new Image(logo);
        intro.setOrigin(intro.getWidth() / 2, intro.getHeight() / 2);
        intro.setPosition(WORLD_WIDTH/2-(intro.getWidth()/2), WORLD_HEIGHT/2-intro.getHeight()/2);
        batch = new SpriteBatch();


    }


    @Override
    public void render(float delta) {

        //graphics
        Gdx.gl.glClearColor(255f, 255f, 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        intro.act(Gdx.graphics.getDeltaTime()*900); //900

        batch.begin();
        intro.draw(batch, 1);
        batch.end();

    }

    @Override
    public void show() {

        SequenceAction sequenceAction = new SequenceAction();
        intro.setColor(1f,1f,1f,0f);
        sequenceAction.addAction(Actions.fadeIn(1500f));
        sequenceAction.addAction(Actions.fadeOut(1500f));
        sequenceAction.addAction(sequence(fadeOut(3f), run(new Runnable() {
            @Override
            public void run() {
                gamein.setScreen(gamein.menuScreen);
            }
        })));
        intro.addAction(sequenceAction);


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
