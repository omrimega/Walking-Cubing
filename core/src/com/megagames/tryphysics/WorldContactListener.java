package com.megagames.tryphysics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.megagames.tryphysics.GameScreen.*;
import com.megagames.tryphysics.TypesOfPlayer.Omri;

public class WorldContactListener implements ContactListener {
    GameScreen world;
    Omri omri;
    public WorldContactListener(GameScreen gameScreen, Omri omriOUT) {
        world = gameScreen;
        omri = omriOUT;
    }

    @Override
    public void beginContact(Contact contact) {

        //Gdx.app.log("x: "+ world.player.getPosition().x, "");

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        //Gdx.app.log("contact player: "+world.player , "");
        //Gdx.app.log("contact: platform: "+ world.platform, "");
//        Gdx.app.log("fixA : "+fixA.getUserData() , "");
 //       Gdx.app.log("fixB: "+ fixB.getBody().getUserData(), "");

        if (fixB.getBody().getUserData() == "potato") {
            omri.WhenPotatoDestroy();
            world.bodytodestroy.push(fixB.getBody());
            world.canDestroy = true;

        }
        if (fixA.getBody().getUserData() == "potato") {
            omri.WhenPotatoDestroy();
            world.bodytodestroy.push(fixA.getBody());
            world.canDestroy = true;
        }

        if (fixA.getBody() == world.player && fixB.getBody() == world.platform || fixA.getBody() == world.platform && fixB.getBody() == world.player || fixA.getBody().getMass() == 2.4414062f && fixB.getBody() == world.player || fixB.getBody().getMass() == 2.4414062f && fixA.getBody() == world.player) {
            world.isJump(true);
            System.out.println(fixA.getBody().getMass()*1.f);
        }


        /*if (world.bullets[world.i] == fixA.getBody() || world.bullets[world.i] == fixB.getBody()) {
            Gdx.app.log("contact bullet", "");
            world.canDestroy = true;
        }*/

        //Gdx.app.log("fixA: " +fixB.getBody().getMass()*10000, "");
        //Gdx.app.log("fixB: " +fixB.getBody().getMass()*10000, "");

        if (fixA.getBody().getMass()*10000f == 9.765625f){
            world.bodytodestroy.push(fixA.getBody());
            //world.qbullet = fixA.getBody();
            world.canDestroy = true;
        }
        if (fixB.getBody().getMass()*10000f == 9.765625f){
            world.bodytodestroy.push(fixB.getBody());
            //world.qbullet = fixB.getBody();
            world.canDestroy = true;
        }
        if ((fixA.getBody().getMass()*10000f == 9.765625f && fixB.getBody() == world.player) ||(fixB.getBody().getMass()*10000f == 9.765625f && fixA.getBody() == world.player)  ){
            world.life -= 0.1f;
        }


    }


    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("contact off", "");
       // world.isJump(false);
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if (fixA.getBody().getMass()*10000f == 9.765625f){
            world.bodytodestroy.push(fixA.getBody());
            //world.qbullet = fixA.getBody();
            world.canDestroy = true;
        }
        if (fixB.getBody().getMass()*10000f == 9.765625f){
            world.bodytodestroy.push(fixB.getBody());
            //world.qbullet = fixB.getBody();
            world.canDestroy = true;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
