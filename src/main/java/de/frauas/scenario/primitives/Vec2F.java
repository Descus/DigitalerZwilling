package de.frauas.scenario.primitives;

import de.frauas.scenario.components.Drawable;

import java.awt.*;

public record Vec2F(float x, float y) implements Drawable {
    public static final int DRAWN_RADIUS = 10;
    
    public static final Vec2F zero = new Vec2F(0,0);
    public static final Vec2F one = new Vec2F(1,1);
    public static final Vec2F up = new Vec2F(0,1);
    public static final Vec2F right = new Vec2F(1,0);

    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.cyan);
        g2d.fillOval((int) (x * scale.x - DRAWN_RADIUS * scale.x / 2), (int) (-y * scale.y - DRAWN_RADIUS * scale.y / 2), (int) (DRAWN_RADIUS * scale.x), (int) (DRAWN_RADIUS * scale.y));
        g2d.dispose();
    }
    
    public Vec2F add(Vec2F other){
        return new Vec2F(x + other.x, y + other.y);
    }

    public Vec2F sub(Vec2F other){
        return new Vec2F(x - other.x, y - other.y);
    }
    
    public Vec2F scale(float amount){
        return new Vec2F(x * amount, y * amount);
    }
    
    public Vec2F rotate(float degAngle){
        double radAngle = Math.toRadians(degAngle);
        return new Vec2F(
                x * (float) Math.cos(radAngle) - y * (float) Math.sin(radAngle), 
                x * (float) Math.sin(radAngle) + y * (float) Math.cos(radAngle));
    }
    
    public float length(){
        return (float) Math.sqrt(lengthSquared());
    }
    
    public float lengthSquared(){
        return x * x + y * y;
    }
    
    public Vec2F min(Vec2F other){
        return new Vec2F(Math.min(x, other.x), Math.min(y, other.y));
    }
    
    public Vec2F max(Vec2F other){
        return new Vec2F(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vec2F abs(){
        return new Vec2F(Math.abs(x), Math.abs(y));
    }
    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }
}
