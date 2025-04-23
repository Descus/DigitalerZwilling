package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;
import de.frauas.scenario.primitives.Vec2F;

import java.awt.*;

public class Obstacle implements Drawable {
    public Vec2 position;
    public Vec2 size;
    public int height;
    
    public Obstacle(Vec2 position, Vec2 size, int height) {
        this.position = position;
        this.size = size;
        this.height = height;
    }
    
    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.magenta);
        g2d.fillRect((int) (position.x() * scale.x()), 
                (int) (-position.y() * scale.y()), 
                (int) (size.x() * scale.x()), 
                (int) (size.y() * scale.y()));
        g2d.setColor(Color.black);
        g2d.drawString("" + height, 
                (int) ((position.x() + size.x() / 2f) * scale.x()), 
                (int) ((-position.y() + size.y() / 2f) * scale.y()));
        g2d.dispose();
    }
}
