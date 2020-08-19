package com.megagames.tryphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.megagames.tryphysics.Constants.PPM;

public class MenuScreen implements Screen {

    private TryPhysicsGame gamein;

    //world parameters
    private final int WORLD_WIDTH = Gdx.graphics.getWidth();
    private final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    //screen
    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private Stage stage;
    private TextField name;
    private TextButton btnGame;
    private Label title, arrow, playerName;

    private Texture background;

    //box2d
    public World world;
    public Body player, platform;
    private Box2DDebugRenderer phybug;
    private Sprite boxSprite;
    private Array<Body> tmpBodies = new Array<Body>();
    private Image bodysprite;





    MenuScreen(TryPhysicsGame gameout) {
        gamein = gameout;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH/2, WORLD_HEIGHT/2);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(viewport);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.getFont("bitfont").getData().setScale(1.4f);

        background = new Texture(Gdx.files.internal("backgroundMenu.png"));
        Image image1 = new Image(background);
        image1.setWidth(WORLD_WIDTH);
        image1.setHeight(WORLD_HEIGHT);
        stage.addActor(image1);

        name = new TextField("your name", skin);
        name.setPosition(WORLD_WIDTH/2-380/2,WORLD_HEIGHT/2);
        name.setSize(380,80);
        name.setMaxLength(10);
        name.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                name.setText("");
            }
        });

        playerName = new Label("", skin);
        playerName.setColor(Color.GRAY);

        btnGame = new TextButton("Accept", skin);
        btnGame.setPosition(WORLD_WIDTH/2-btnGame.getWidth()/2,WORLD_HEIGHT/2-name.getHeight());
        btnGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                gamein.setScreen(gamein.gameScreen);
            }
        });

        title = new Label("Write your Name: ", skin);
        title.setPosition(WORLD_WIDTH/2-title.getWidth()/2,WORLD_HEIGHT/2+name.getHeight()+title.getHeight()/2);

        arrow = new Label(" <-     -> ", skin);
        arrow.setColor(Color.GRAY);


        stage.addActor(title);
        stage.addActor(btnGame);
        stage.addActor(name);
        stage.addActor(arrow);

        world = new World(new Vector2(0, -9.8f), false);
        phybug = new Box2DDebugRenderer();
        player = createPlayer(50, 30, 35, 35);
        platform = createBox(WORLD_WIDTH/2, 0, WORLD_WIDTH , 50);

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        world.step(1 / 45f, 6, 2);

        //graphics
        Gdx.gl.glClearColor(255f, 255f, 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        stage.draw();
        drawPlayerINFO();
        drawSprites();
        arrow.setPosition(player.getPosition().x*PPM - arrow.getWidth()/2,player.getPosition().y*1024/PPM);

        batch.end();

        phybug.render(world, camera.combined.scl(PPM));

        gamein.name = name.getText();
        if (player.getPosition().x>WORLD_WIDTH/PPM || player.getPosition().x<0)
            player.setTransform(WORLD_WIDTH/2/32,5,0);

        int moveForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveForce -= 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveForce += 1f;
        }
        player.setLinearVelocity(player.getLinearVelocity().x + moveForce, player.getLinearVelocity().y);

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
    public void show() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    private void drawPlayerINFO() {
        playerName.setText(gamein.name);
        playerName.draw(batch,200);
        playerName.setPosition(player.getPosition().x*PPM - 150,player.getPosition().y*PPM+85);

    }

    public Body createBox(int x, float y, int width, int height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x/PPM,y/PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM,height/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.6f;
        pBody.createFixture(fixtureDef);
        shape.dispose();

        //test sprite option
        boxSprite = new Sprite(new Texture("wall.png"));
        boxSprite.setSize(width, height);
        //boxSprite.setPosition(x/2-(width/2),y/2-(height/2));
        pBody.setUserData(boxSprite);



        return pBody;
    }
    public Body createPlayer(int x, int y, int width, int height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(WORLD_WIDTH/2/PPM, 50/PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM,height/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.2f; // Make it bounce a little bit

        pBody.createFixture(fixtureDef);
        shape.dispose();

        boxSprite = new Sprite(new Texture("player.png"));
        boxSprite.setSize(width, height);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);

        pBody.setUserData(boxSprite);

        return pBody;
    }
    private void drawSprites() {
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies)
            if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.setPosition(body.getPosition().x / 32 * 1024 - sprite.getWidth() / 2, body.getPosition().y / 32 * 1024 - sprite.getHeight() / 2);
                sprite.draw(batch);
                //System.out.println(body.getPosition().x/32*1024-sprite.getWidth()/2);


            }
    }
        private void drawImages() {
            world.getBodies(tmpBodies);
            for (Body body : tmpBodies)
                if (body.getUserData() != null && body.getUserData() instanceof Image) {
                    Image image = (Image) body.getUserData();
                    image.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                    image.setPosition(body.getPosition().x, body.getPosition().y/PPM, 200);
                    image.draw(batch, 200);
                }
    }
}
