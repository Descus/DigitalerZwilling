package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;
import de.frauas.scenario.primitives.Vec2F;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static de.frauas.Settings.CAR_SIZE;

@Getter
public class Car implements Drawable{
    
    private Vec2 position;
    private float rotation;
    
    public Car(Vec2 position, int rotation) {
        this.position = position;
        this.rotation = rotation;
    }
    
    public void addRotation(float rotation) {
        this.rotation += rotation;
        this.rotation %= 360;
    }
    
    public void addTranslation(Vec2 translation){
        this.position = this.position.add(translation);
    }
    
    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        update(deltaTime);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.RED);
        
        Rectangle rect2 = new Rectangle((int) (position.x() * scale.x() - CAR_SIZE.x() * scale.x() / 2f), (int) (-position.y() * scale.y() - CAR_SIZE.y() * scale.y() / 2f), (int) (CAR_SIZE.x() * scale.x()), (int) (CAR_SIZE.y() * scale.y()));
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(rotation), rect2.getX() + rect2.width / 2f, rect2.getY() + rect2.height / 2f);
        g2d.transform(at);
        g2d.fill(rect2);
        g2d.dispose();
    }
    
    private void update(float deltaTime){
        
    }
}


