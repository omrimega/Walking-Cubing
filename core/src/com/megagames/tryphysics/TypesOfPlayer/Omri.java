package com.megagames.tryphysics.TypesOfPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.megagames.tryphysics.GameScreen;

import static com.megagames.tryphysics.Constants.PPM;

public class Omri {

    //game class
    private GameScreen gameScreen;

    //box2d
    private World world;
    private Body omriBody;
    private Body potatoBody;
    //temp BOX2D body
    private Array<Body> tmpBodies = new Array<Body>();

    //info about life, ammo, speedXY, positon
    private float life = 10;
    private int ammo = 30;
    private int posX, posY;
    private float speedX, speedY;
    private int radius;

    //draw omri animation
    private Image img, imgPotato;
    private Texture tx, txPotato;

    //Scene2d
    private Stage stage;
    private Skin skin;

    //can if boolean
    private boolean canShoot = true;
    private boolean delayShoot = false;
    private int resetTimer = 0;
    private float time = 0;
    private int ammoCountDown = 0;
    public boolean potatoDestroy = false;
    private boolean LeftSide = true;

    //particle effect
    private ParticleEffect particlePotato;

    //help numbers for count
    private float nLove=0, nFontName=0;





    public Omri(GameScreen game) {
        tx = new Texture("omri.png");
        img = new Image(tx);
        gameScreen = game;
        world = game.world;
        stage = game.stage;
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(game.viewport);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage.addActor(img);
        radius = 40;
        omriBody = createPlayer(700,700, radius);
        txPotato = new Texture("potato.png");
        imgPotato = new Image(txPotato);
        potatoBody = createPotato(2, 40,20);

        //effect
        particlePotato = new ParticleEffect();
        particlePotato.load(Gdx.files.internal("particle/potato"), Gdx.files.internal(""));
    }

    public void OmriPlay () {
        DrawOmri();
        ContactOmri();
        InputOmri();
        DrawPotato();
        DrawAmmo();
        DrawPotatoParticle();
        DrawPlayerINFO();
        DrawSide();


        if (delayShoot) {
            timer(20);
        }
    }

    public Body createPlayer(int x, int y, int radius) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x/PPM, y/PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 4f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.2f; // Make it bounce a little bit

        pBody.createFixture(fixtureDef);
        shape.dispose();

        pBody.setUserData("omri");

        return pBody;
    }

    public void DrawOmri() {
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies)
            if (body.getUserData() == "omri") {
                img.draw(gameScreen.batch, 1);
                img.setPosition(body.getPosition().x * PPM - img.getWidth() / 2, body.getPosition().y * PPM - img.getHeight() / 2);
                img.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                img.setOriginX(img.getImageWidth() / 2);
                img.setOriginY(img.getImageHeight() / 2);
                img.setWidth(radius);
                img.setHeight(radius);
            }
    }

    public void DrawPotato() {

        world.getBodies(tmpBodies);
        for (Body body : tmpBodies)
            if (body.getUserData() == "potato") {
                imgPotato.draw(gameScreen.batch,1);
                imgPotato.setPosition(body.getPosition().x * PPM-imgPotato.getWidth()/2, body.getPosition().y * PPM-imgPotato.getHeight()/2);
                imgPotato.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                imgPotato.setOriginX(imgPotato.getImageWidth()/2);
                imgPotato.setOriginY(imgPotato.getImageHeight()/2);
                imgPotato.setWidth(40);
                imgPotato.setHeight(20);
            }
    }

    public void ContactOmri() {
        //System.out.println(img.isTouchable());
    }

    public void timer(int time) {
        if (resetTimer < time) {
            canShoot = false;
            resetTimer++;
        } else {
            canShoot = true;
            resetTimer = 0;
            delayShoot = false;
        }

    }

    public void InputOmri() {
        float moveForce = 0;
        float moveUp = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveForce -= 0.5f;
            omriBody.applyAngularImpulse((float) 00.1, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveForce += 0.5f;
            omriBody.applyAngularImpulse((float) -00.1, true);
        }
        if ((int) omriBody.getLinearVelocity().x < 16 && (int) omriBody.getLinearVelocity().x > -16) {
            omriBody.setLinearVelocity(omriBody.getLinearVelocity().x + moveForce, omriBody.getLinearVelocity().y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if ((int) omriBody.getLinearVelocity().x < 3 && (int) omriBody.getLinearVelocity().x > -3)
                omriBody.setLinearVelocity(omriBody.getLinearVelocity().x + moveForce, omriBody.getLinearVelocity().y);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.UP)) { //&&canJump
            moveUp += 11;
        }
        if ((int) omriBody.getLinearVelocity().y == 0) {
            omriBody.setLinearVelocity(omriBody.getLinearVelocity().x, omriBody.getLinearVelocity().y + moveUp);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            LeftSide = true;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            LeftSide = false;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println(gameScreen.gamein.socket.id());
            delayShoot = true;

            if (LeftSide && ammo > 0 && canShoot) {
                potatoBody = createPotato(-1.3f,40, 20);
                potatoBody.setLinearVelocity(-17, 7);
                potatoBody.setAngularVelocity(2);
                ammo--;
                //fire.setPosition(omriBody.getPosition().x*PPM, omriBody.getPosition().y*PPM);
                //fire.reset();
            }

            else if (!LeftSide && ammo > 0 && canShoot) {
                potatoBody = createPotato(1.3f,40, 20);
                potatoBody.setLinearVelocity(+17, 7);
                potatoBody.setAngularVelocity(-2);
                ammo--;
            }
        }



    }

    public Body createPotato(float x, int width, int height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(omriBody.getPosition().x +x, omriBody.getPosition().y);
        pBody = gameScreen.world.createBody(def);
        pBody.setBullet(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM,height/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 4f;
        fixtureDef.restitution = 0.4f;
        fixtureDef.friction = 1;

        pBody.createFixture(fixtureDef);
        shape.dispose();


        pBody.setUserData("potato");


        return pBody;
    }

    private String DrawAmmo() {
        time += 0.1f;
        //System.out.println((int)time);
        String showAmmo = ""+ammo;
        if (ammo == 0) {
            if ((int)time % 2 == 0)
                showAmmo = ".";
            else
                showAmmo = "..";

        } else showAmmo = ""+ammo;
        return showAmmo;

    }

    public void DrawPotatoParticle() {
        particlePotato.update(gameScreen.delta);
        particlePotato.draw(gameScreen.batch);

    }

    public void WhenPotatoDestroy() {
        particlePotato.start();
        particlePotato.setPosition(potatoBody.getPosition().x*PPM, potatoBody.getPosition().y*PPM);
        particlePotato.reset();
    }

    private void DrawPlayerINFO() {
        gameScreen.fontName.draw(gameScreen.batch,gameScreen.gamein.name+" "+drawAMMO()+" " ,omriBody.getPosition().x*PPM-100,omriBody.getPosition().y*PPM+85);
        gameScreen.love.draw(gameScreen.batch,""+drawHearts(),omriBody.getPosition().x*PPM-100,omriBody.getPosition().y*PPM+37);
        if (omriBody.getLinearVelocity().x < 3 && omriBody.getLinearVelocity().x > -3 && omriBody.getLinearVelocity().y <3 && omriBody.getLinearVelocity().y > -3) {
            if (nLove <0.5)
                nLove += 0.05;
            gameScreen.love.setColor(255,0,0,nLove);
            if (nFontName <0.5)
                nFontName += 0.05;
            gameScreen.fontName.setColor(1,1,1,nFontName);

        } else {
            if (nLove > 0.2)
                nLove -= 0.05;
            gameScreen.love.setColor(255,0,0,nLove);
            if (nFontName > 0.2)
                nFontName -= 0.05;
            gameScreen.fontName.setColor(1,1,1,nFontName);
        }

        if (ammo <= 0) {
            ammoCountDown++;
        }
        if (ammoCountDown == 300) {
            ammoCountDown = 0;
            ammo = 30;
        }


    }

    private String drawAMMO() {
        //System.out.println((int)time);
        String showAmmo = ""+ammo;
        if (ammo == 0) {
            if ((int)time % 2 == 0)
                showAmmo = ".";
            else
                showAmmo = "..";

        } else showAmmo = ""+ammo;
        return showAmmo;
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

    public void DrawSide() {
        if (LeftSide)
            gameScreen.fontName.draw(gameScreen.batch,"<-" ,omriBody.getPosition().x*PPM-radius-25,omriBody.getPosition().y*PPM+4);
        else if (!LeftSide)
            gameScreen.fontName.draw(gameScreen.batch,"->" ,omriBody.getPosition().x*PPM+radius-10,omriBody.getPosition().y*PPM+4);
    }
}
