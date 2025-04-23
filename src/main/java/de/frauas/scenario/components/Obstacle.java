package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;
import de.frauas.scenario.primitives.Vec2F;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import static de.frauas.Settings.DEBUG;
import static de.frauas.Settings.POINT_DEBUG_RADIUS;


@Setter
@Getter
public class Obstacle implements Drawable {
    private Vec2 position;
    private Vec2 dimension;
    private int height;
    
    public Obstacle(Vec2 position, Vec2 dimension, int height) {
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
}
