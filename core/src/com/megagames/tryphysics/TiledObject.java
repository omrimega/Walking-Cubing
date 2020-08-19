package com.megagames.tryphysics;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import static com.megagames.tryphysics.Constants.PPM;


public class TiledObject {

    public void buildBuildingsBodies(TiledMap tiledMap, World world, String layer){
        MapObjects objects = tiledMap.getLayers().get(layer).getObjects();
        for (MapObject object: objects) {
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

            //create a dynamic within the world body (also can be KinematicBody or StaticBody
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bodyDef);

            //create a fixture for each body from the shape
            Fixture fixture = body.createFixture(getShapeFromRectangle(rectangle),1f);
            fixture.setFriction(0.1F);

            //setting the position of the body's origin. In this case with zero rotation
            body.setTransform(getTransformedCenterForRectangle(rectangle),0);
        }
    }

    public static final float TILE_SIZE = 32;
//Also you can get tile width with: Float.valueOf(tiledMap.getProperties().get("tilewidth",Integer.class));

    public static Shape getShapeFromRectangle(Rectangle rectangle){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width*0.5F/ TILE_SIZE,rectangle.height*0.5F/ TILE_SIZE);
        return polygonShape;
    }

    public static Vector2 getTransformedCenterForRectangle(Rectangle rectangle){
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        return center.scl(1/TILE_SIZE);
    }
}
