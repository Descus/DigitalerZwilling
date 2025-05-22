package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Mat2F;
import de.frauas.scenario.primitives.SDF;
import de.frauas.scenario.primitives.Vec2F;

import java.awt.*;

public class Obstacle implements Drawable, SDF {
    public Vec2F position;
    public Vec2F size;
    public int height;
    
    public Obstacle(Vec2F position, Vec2F size, int height) {
        this.position = position;
        this.size = size;
        this.height = height;
    }
    
    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.magenta);
        
        g2d.fillRect(
                (int) (position.x() * scale.x()), 
                (int) (-position.y() * scale.y()), 
                (int) (size.x() * scale.x()), 
                (int) (size.y() * scale.y()));
        g2d.setColor(Color.black);
        
        g2d.drawString("" + height, 
                (int) ((position.x() + size.x() / 2f) * scale.x()), 
                (int) ((-position.y() + size.y() / 2f) * scale.y()));
        g2d.dispose();
    }

    @Override
    public float getSDF(Vec2F otherPosition) {
        float th = size.y()/2;
        Vec2F a = new Vec2F(position.x(),  position.y() + th);
        Vec2F b = new Vec2F(position.x() + size.x(), position.y() + th);
        Vec2F dist = b.sub(a);
        float l = dist.length();
        Vec2F  d = dist.scale(1/l);
        Vec2F  q = (otherPosition.sub(a.add(b).scale(0.5f)));
        q = new Mat2F(d.x(),-d.y(),d.y(),d.x()).mult(q);
        q = q.abs().sub(new Vec2F(l,th).scale(0.5f));
        return q.max(Vec2F.zero).length() + (float) Math.min(Math.max(q.x(),q.y()),0.0);
    }
}
