package de.frauas.scenario.primitives;

import de.frauas.scenario.components.Drawable;

import java.awt.*;

public record Line2(Vec2 a, Vec2 b) implements Drawable {
    private static final float LINE_WIDTH = 1f;

    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.setColor(Color.black);
        g2d.drawLine(
                (int) (a.x() * scale.x()), (int) (-a.y() * scale.y()),
                (int) (b.x() * scale.x()), (int) (-b.y() * scale.y()));
        a.draw(g2d, scale, deltaTime);
        b.draw(g2d, scale, deltaTime);
        g2d.dispose();
    }
    
    public Vec2 lerp(float t) {

        return new Vec2(
                (int) ((1 - t) * a.x() + t * b.x()),
                (int) ((1 - t) * a.y() + t * b.y()));
    }
}
