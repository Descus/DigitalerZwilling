package de.frauas.scenario.primitives;

import de.frauas.scenario.components.Drawable;

import java.awt.*;

public record Vec2F(float x, float y) implements Drawable {
    public static final int DRAWN_RADIUS = 10;

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

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }
}
