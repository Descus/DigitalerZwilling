package de.frauas.scenario.primitives;

import de.frauas.scenario.components.Drawable;

import java.awt.*;

public record Vec2(int x, int y) implements Drawable {
    public static final int DRAWN_RADIUS = 10;

    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.cyan);
        g2d.fillOval(
                (int) (x * scale.x() - DRAWN_RADIUS * scale.x() / 2), 
                (int) (-y * scale.y() - DRAWN_RADIUS * scale.y() / 2), 
                (int) (DRAWN_RADIUS * scale.x()), 
                (int) (DRAWN_RADIUS * scale.y()));
        g2d.dispose();
    }
    
    public Vec2 add(Vec2 other){
        return new Vec2(x + other.x, y + other.y);
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }
}
