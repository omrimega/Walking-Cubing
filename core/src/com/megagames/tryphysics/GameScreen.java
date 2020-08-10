package com.megagames.tryphysics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Stack;

import static com.megagames.tryphysics.Constants.PPM;

public class GameScreen implements Screen {

    //לבנות מסך פתיחה שכולל, כפתור כניסה, תיבת טקסט לרשום את השם, וכמה דמויות

    //physics
    public World world;
    public Body player, platform, block, enemy, bullet, qbullet;
    public Stack <Body> bodytodestroy;

    public float life = 10;

    public int cBullets = 0, i=0;
    public boolean canDestroy = false;
    private Box2DDebugRenderer phybug;
    private int ammo = 30;
    private boolean canShoot = true;
    private int zero = 0;
    private int ammoCountDown = 0;
    private Boolean timerBool = false;
    private float time = 0;

    //if
    private boolean canJump = true;

    //screen
    private OrthographicCamera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;

    //array pf backgrounds
    private Texture[] backgrounds;
    private Texture background, wall;

    //array of logo
    private Texture logo;

    //timing
    private float[] backgroundOFFsets = {0, 0};
    private float backgroundMaxScrollingSpeed;

    //world parameters
    private final int WORLD_WIDTH = Gdx.graphics.getWidth();
    private final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    //show Sprites automatic
    private Array<Body> tmpBodies = new Array<Body>();

    //sprites
    private Texture[] spr;
    private TextureRegion sprPlayer;
    private Sprite boxSprite;

    //font
    private BitmapFont fontName;
    private BitmapFont love;

    //help numbers
    private float nLove=0, nFontName=0;

    //effect
    private ParticleEffect fire;

    private TextField Name;



    GameScreen() {
        //camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //background
        backgrounds = new Texture[2];
        backgrounds[0] = new Texture("background0.png");
        backgrounds[1] = new Texture("background1.png");
        background = new Texture("background.png");
        //SPRITES
        spr = new Texture[5];
        spr[0] = new Texture("player.png");
        sprPlayer = new TextureRegion(spr[0]);
        wall = new Texture("wall.png");

        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;
        //logo
        logo = new Texture("logo.png");
        //physics
        world = new World(new Vector2(0, -9.8f), false);
        phybug = new Box2DDebugRenderer();

        platform = createBox(30, 0, 3000, 50);
        block = createBox(3000, 3050, 50, 3000);
        block = createBox(30, 6050, 3000, 50);
        block = createBox(-3050, 3050, 50, 3000);
        player = createPlayer(50, 30, 35, 35);
        bullet = createBullet(-1,10, 5);
        bodytodestroy = new Stack<>();

        Skin skin = new Skin(Gdx.files.internal("name.png"));
        Name = new TextField("", skin);
        Name.setPosition(player.getPosition().x, player.getPosition().y);
        Name.setSize(200,200);


        for (int i =0; i < 5; i++) {
            Body b = createPlayer(50*i, 30, 50, 50); // mass : 2.4414062
        }

        //contact dec
        world.setContactListener(new WorldContactListener(this));
        //world.setContactListener(this);

        //font
        fontName = new BitmapFont(Gdx.files.internal("BitMap.fnt"));
        love = new BitmapFont(Gdx.files.internal("gameFont.fnt"));

        //effect
        fire = new ParticleEffect();
        fire.load(Gdx.files.internal("fireEffect"), Gdx.files.internal(""));


        batch = new SpriteBatch();
    }


    @Override
    public void render(float deltaTime) {

        update(Gdx.graphics.getDeltaTime());

        //graphics
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //scrolling background
        //renderBackground(deltaTime);
        //renderLogo(deltaTime);
        batch.draw(background, -3000, 0, 30000,3000);
        drawSprites();
        drawPlayerINFO();

        fire.update(deltaTime);
        fire.draw(batch);
        Name.draw(batch, 120);
        batch.end();

        phybug.render(world, camera.combined.scl(PPM));

    }

    private void drawPlayerINFO() {
        fontName.draw(batch,"OMRIMEGA"+" "+drawAMMO()+" "+drawCanJump() ,player.getPosition().x*PPM-100,player.getPosition().y*PPM+85);
        love.draw(batch,""+drawHearts(),player.getPosition().x*PPM-100,player.getPosition().y*PPM+37);
        if (player.getLinearVelocity().x < 3 && player.getLinearVelocity().x > -3 && player.getLinearVelocity().y <3 && player.getLinearVelocity().y > -3 && canJump) {
            if (nLove <0.5)
            nLove += 0.05;
            love.setColor(255,0,0,nLove);
            if (nFontName <0.5)
                nFontName += 0.05;
            fontName.setColor(1,1,1,nFontName);

        } else {
            if (nLove > 0.2)
                nLove -= 0.05;
            love.setColor(255,0,0,nLove);
            if (nFontName > 0.2)
                nFontName -= 0.05;
            fontName.setColor(1,1,1,nFontName);
        }

        if (ammo <= 0) {
            ammoCountDown++;
        }
        if (ammoCountDown == 300) {
            ammoCountDown = 0;
            ammo = 30;
        }

        if (timerBool) {
            timer(10);
        }

    }

    private String drawHearts() {
        String h =  "♥♥♥♥♥♥♥";
        if (life <10.1 && life >8.6)
            h = "♥♥♥♥♥♥♥";
        else if (life <8.6 && life > 7.2)
            h = "♥♥♥♥♥♥";
        else if (life <7.2 && life > 5.8)
            h = "♥♥♥♥♥";
        else if (life <5.8 && life > 4.4)
            h = "♥♥♥♥";
        else if (life <4.4 && life > 3)
            h = "♥♥♥";
        else if (life <3 && life > 1.6)
            h = "♥♥";
        else if (life <1.6 && life > 0)
            h = "♥";
        else if (life <= 0)
            h = "";


        return h;
    }

    private String drawAMMO() {
        System.out.println((int)time);
        String showAmmo = ""+ammo;
        if (ammo == 0) {
            if ((int)time % 2 == 0)
                showAmmo = ".";
            else
                showAmmo = "..";

        } else showAmmo = ""+ammo;
        return showAmmo;
    }

    private String drawCanJump() {
        String j = "J";
        if (canJump) {
            j = "J";
        } else
            j = "";
        return j;
    }

    private void drawSprites() {
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies)
            if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.setPosition(body.getPosition().x/32*1024-sprite.getWidth()/2,body.getPosition().y/32*1024-sprite.getHeight()/2);
                sprite.draw(batch);
                //System.out.println(body.getPosition().x/32*1024-sprite.getWidth()/2);
            }
    }

    public void update(float deltaTime) {
            world.step(1 / 45f, 6, 2);

        //Gdx.app.log("bodies on the world: "+world.getBodyCount() ,"");
        //Gdx.app.log("bodies to destroy: "+bodytodestroy.capacity() ,"");
        //Gdx.app.log("life: "+life ,"");



        if (canDestroy && bodytodestroy.empty() == false) {
            world.destroyBody(bodytodestroy.pop());
                //world.destroyBody(qbullet);
                canDestroy = false;
            }

        time += 0.1f;
      //  System.out.println("bullet mass" + bullet.getMass()*10000);
      //  System.out.println("player mass" + player.getMass()*10000);





        cameraUpdate(deltaTime);
        inputUpdate(deltaTime);

    }

    public void cameraUpdate(float deltaTime) {
        Vector3 position = camera.position;
        position.x = player.getPosition().x * PPM;
        position.y = player.getPosition().y * PPM +100;
        camera.position.set(position);
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            camera.zoom += 0.05;
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
            camera.zoom -= 0.05;

        camera.update();
    }

    public void inputUpdate(float deltaTime) {
        float moveForce = 0;
        int moveUp = 0;

        if (!Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveForce -= 1f;
                player.applyAngularImpulse((float) 0.1, true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveForce += 1f;
                player.applyAngularImpulse((float) -0.1, true);
            }  } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    moveForce += 0.3f;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    moveForce -= 0.3f;
                }
            }


        if (!Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if ((int) player.getLinearVelocity().x < 16 && (int) player.getLinearVelocity().x > -16)
                player.setLinearVelocity(player.getLinearVelocity().x + moveForce, player.getLinearVelocity().y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if ((int) player.getLinearVelocity().x < 3 && (int) player.getLinearVelocity().x > -3)
                player.setLinearVelocity(player.getLinearVelocity().x + moveForce, player.getLinearVelocity().y);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.UP) && canJump) {
            moveUp += 15;
            canJump = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveUp -= 50;
        }
        if ((int) player.getLinearVelocity().y == 0)
            player.setLinearVelocity(player.getLinearVelocity().x, player.getLinearVelocity().y + moveUp);

        //System.out.println("Velocity y: " + (int) player.getLinearVelocity().x);

        // for (int i = 0; bullets[i] == null && i<499 && bullets[i+1] != null; i++) {
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            timerBool = true;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && ammo > 0 && canShoot) {
                bullet = createBullet(-1,10, 5);
                bullet.setLinearVelocity(-60, 3);
                ammo--;
                fire.setPosition(player.getPosition().x*PPM, player.getPosition().y*PPM);
                fire.reset();
            }

            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && ammo > 0 && canShoot) {
                bullet = createBullet(1,10, 5);
                bullet.setLinearVelocity(+60, 3);
                moveForce = -0.7f;
                ammo--;
                fire.setPosition(player.getPosition().x*PPM, player.getPosition().y*PPM);
                fire.reset();
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            Boolean fullScreen = Gdx.graphics.isFullscreen();
            Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
            if (fullScreen == true)
                Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
            else
                Gdx.graphics.setFullscreenMode(currentMode);        }

            //PUSH SKILL
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Body bullet2 = createBullet(-30, 10,5 );
            bullet2.setLinearVelocity(60, 3);
            if (player.getLinearVelocity().x>1 && Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getLinearVelocity().x < 35) {
                player.applyLinearImpulse(5,0, player.getPosition().x, player.getPosition().y, true);
                fire.setPosition(player.getPosition().x*PPM, player.getPosition().y*PPM);
                fire.reset();

            }
            if (player.getLinearVelocity().x<0 && Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getLinearVelocity().x > -35) {
                player.applyLinearImpulse(-5,0, player.getPosition().x, player.getPosition().y, true);
                fire.setPosition(player.getPosition().x*PPM, player.getPosition().y*PPM);
                fire.reset();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R))
            Gdx.app.exit();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {

        }


    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    //setting PHYSICS BODIES
    public Body createBox(int x, float y, int width, int height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x/2/PPM, (float) y/2/PPM);
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
        def.position.set(720*x/100/PPM, 480*y/100/PPM);
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

    public Body createBullet(int x, int width, int height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(player.getPosition().x +x, player.getPosition().y);
        pBody = world.createBody(def);
        pBody.setBullet(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM,height/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        fixtureDef.restitution = 0.4f;
        fixtureDef.friction = 1;

        pBody.createFixture(fixtureDef);
        shape.dispose();

        boxSprite = new Sprite(new Texture("bullet.png"));
        boxSprite.setSize(width, height);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);

        pBody.setUserData(boxSprite);


        return pBody;
    }

    public void isJump(boolean getBoolJump) {
        canJump = getBoolJump;
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
        world.dispose();
        phybug.dispose();
        spr[0].dispose();
        boxSprite.getTexture().dispose();
        fire.dispose();
    }

    public void timer(int time) {
        if (zero < time) {
            canShoot = false;
            zero++;
        } else {
            canShoot = true;
            zero = 0;
            timerBool = false;
        }

    }

    private void renderBackground(float deltaTime) {

        backgroundOFFsets[0] += deltaTime * backgroundMaxScrollingSpeed /4.5;
        backgroundOFFsets[1] += deltaTime * backgroundMaxScrollingSpeed /0.9;

        for (int layer = 0; layer < backgroundOFFsets.length; layer++) {
            if (backgroundOFFsets[layer] > WORLD_HEIGHT) {
                backgroundOFFsets[layer] = 0;
            }

            batch.draw(backgrounds[layer], 0, -backgroundOFFsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroundOFFsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    private void renderLogo(float deltaTime) {
        batch.draw(logo,20, 70, WORLD_WIDTH/2, WORLD_HEIGHT/2);
    }



}
