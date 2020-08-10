package com.megagames.tryphysics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


import static com.megagames.tryphysics.Constants.PPM;

public class Bullet {

    Body bullet;
    World worldg;
    int timer = 0;
    GameScreen gay;
    Body [] body;



    public Bullet(GameScreen gameScreen) {
        gay = gameScreen;
    }

    public void update() {

    }

    public void lifetime() {
        System.out.println(timer);
        timer++;
        if (timer > 500)
            gay.world.destroyBody(bullet);
    }


    public Body createBullet(GameScreen game, World world, Sprite boxSprite, int x, int width, int height, float speed) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(game.player.getPosition().x +x, game.player.getPosition().y);
        pBody = world.createBody(def);
        pBody.setBullet(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM,height/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;

        pBody.createFixture(fixtureDef);
        shape.dispose();

        boxSprite = new Sprite(new Texture("bullet.png"));
        boxSprite.setSize(width, height);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);

        pBody.setUserData(boxSprite);

        pBody.setLinearVelocity(speed, 0);

        bullet = pBody;
        worldg = world;

        return pBody;
    }
}
