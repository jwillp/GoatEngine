package com.brashmonkey.spriter.gdxIntegration;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Timeline;

public class LibGdxSpriterDrawer extends Drawer<Sprite> {
    SpriteBatch batch;
    ShapeRenderer renderer;

    public LibGdxSpriterDrawer(Loader<Sprite> loader, SpriteBatch batch, ShapeRenderer renderer) {
        super(loader);
        this.batch = batch;
        this.renderer = renderer;
    }

    public void setColor(float r, float g, float b, float a) {
        this.renderer.setColor(r, g, b, a);
    }

    public void rectangle(float x, float y, float width, float height) {
        this.renderer.rect(x, y, width, height);
    }

    public void line(float x1, float y1, float x2, float y2) {
        this.renderer.line(x1, y1, x2, y2);
    }

    public void circle(float x, float y, float radius) {
        this.renderer.circle(x, y, radius);
    }

    public void draw(Timeline.Key.Object object) {
        Sprite sprite = (Sprite)this.loader.get(object.ref);
        float newPivotX = sprite.getWidth() * object.pivot.x;
        float newX = object.position.x - newPivotX;
        float newPivotY = sprite.getHeight() * object.pivot.y;
        float newY = object.position.y - newPivotY;
        sprite.setX(newX);
        sprite.setY(newY);
        sprite.setOrigin(newPivotX, newPivotY);
        sprite.setRotation(object.angle);
        sprite.setColor(batch.getColor());
        sprite.setScale(object.scale.x, object.scale.y);
        sprite.draw(this.batch, object.alpha);
    }
}
