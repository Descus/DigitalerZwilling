package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Mat2F;
import de.frauas.scenario.primitives.SDF;
import de.frauas.scenario.primitives.Vec2F;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import static de.frauas.Settings.DEBUG;
import static de.frauas.Settings.POINT_DEBUG_RADIUS;


@Setter
@Getter
public class Obstacle implements Drawable {
    private Vec2F position;
    private Vec2F dimension;
    private int height;
    
    public Obstacle(Vec2F position, Vec2F dimension, int height) {
        this.position = position;
        this.dimension = dimension;
        this.height = height;
    }
    
    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.magenta);
        g2d.fillRect((int) (position.x() * scale.x()),
                (int) ((-position.y() - dimension.y()) * scale.y()),
                (int) (dimension.x() * scale.x()),
                (int) (dimension.y() * scale.y()));
        g2d.setColor(Color.black);
        g2d.drawString("" + height, 
                (int) ((position.x() + dimension.x() / 2f) * scale.x()),
                (int) ((-position.y() - dimension.y() / 2f) * scale.y()));
        if(DEBUG) {
            g2d.setColor(Color.cyan);
            g2d.fillOval( (int) ((position.x() - POINT_DEBUG_RADIUS / 2f) * scale.x()),
                    (int) ((-position.y() - POINT_DEBUG_RADIUS / 2f) * scale.y()),
                    (int) (POINT_DEBUG_RADIUS * scale.x()), (int) (POINT_DEBUG_RADIUS * scale.y()));

            g2d.fillOval( (int) ((position.x() + dimension.x() - POINT_DEBUG_RADIUS / 2f) * scale.x()),
                    (int) ((-position.y() - dimension.y() - POINT_DEBUG_RADIUS / 2f) * scale.y()),
                    (int) (POINT_DEBUG_RADIUS * scale.x()), (int) (POINT_DEBUG_RADIUS * scale.y()));
        }
        g2d.dispose();
    }

    @Override
    public float getSDF(Vec2F otherPosition) {
        float th = dimension.y()/2;
        Vec2F a = new Vec2F(position.x(),  position.y() + th);
        Vec2F b = new Vec2F(position.x() + dimension.x(), position.y() + th);
        Vec2F dist = b.sub(a);
        float l = dist.length();
        Vec2F  d = dist.scale(1/l);
        Vec2F  q = (otherPosition.sub(a.add(b).scale(0.5f)));
        q = new Mat2F(d.x(),-d.y(),d.y(),d.x()).mult(q);
        q = q.abs().sub(new Vec2F(l,th).scale(0.5f));
        return q.max(Vec2F.zero).length() + (float) Math.min(Math.max(q.x(),q.y()),0.0);
    }
}
