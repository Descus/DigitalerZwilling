package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;

import java.awt.*;

public class Obstacle implements Drawable {
    public Vec2<Integer> position;
    public Vec2<Integer> size;
    public int height;
    
    public Obstacle(Vec2<Integer> position, Vec2<Integer> size, int height) {
        this.position = position;
        this.size = size;
        this.height = height;
    }
    
    @Override
    public void Draw(Graphics2D g, Vec2<Float> scale) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.magenta);
        g2d.fillRect((int) (position.x * scale.x), (int) (-position.y * scale.y), (int)(size.x * scale.x), (int) (size.y * scale.y));
        g2d.setColor(Color.black);
        g2d.drawString("" + height, (int) ((position.x + size.x / 2f) * scale.x), (int) ((-position.y + size.y / 2f) * scale.y));
        g2d.dispose();
    }
}
