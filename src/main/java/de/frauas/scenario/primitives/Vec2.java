package de.frauas.scenario.primitives;

import de.frauas.scenario.components.Drawable;

import java.awt.*;

public class Vec2<T extends Number> implements Drawable {
    public static final int DRAWN_RADIUS = 10;
    public final T x;
    public final T y;

    public Vec2(T x, T y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void Draw(Graphics2D g, Vec2<Float> scale) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.cyan);
        g2d.fillOval((int) (x.intValue() * scale.x) - DRAWN_RADIUS / 2, (int) (-y.intValue() * scale.y) - DRAWN_RADIUS / 2, DRAWN_RADIUS, DRAWN_RADIUS);
        g2d.dispose();
    }
}
