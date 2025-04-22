package de.frauas.scenario.primitives;

import de.frauas.scenario.components.Drawable;

import java.awt.*;

public class Line2<T extends Number> implements Drawable {
    private static final float LINE_WIDTH = 1f;
    private final Vec2<T> a;
    private final Vec2<T> b;
    
    public Line2(Vec2<T> a, Vec2<T> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void Draw(Graphics2D g, Vec2<Float> scale) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.setColor(Color.black);
        g2d.drawLine(
                (int) (a.x.intValue() * scale.x),(int) (-a.y.intValue() * scale.y),
                (int) (b.x.intValue() * scale.x), (int) (-b.y.intValue() * scale.y));
        a.Draw(g2d, scale);
        b.Draw(g2d, scale);
        g2d.dispose();
    }
    
    public Vec2<Integer> Lerp(float t) {

        return new Vec2<>(
                (int) ((1 - t) * a.x.intValue() + t * b.x.intValue()),
                (int) ((1 - t) * a.y.intValue() + t * b.y.intValue()));
    }
}
