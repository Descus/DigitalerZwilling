package de.frauas.scenario.components;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.frauas.scenario.primitives.Vec2;

import java.awt.*;
import java.awt.geom.AffineTransform;

@JacksonXmlRootElement(localName = "StartPosition")
public class Car implements Drawable{
    public static final Vec2<Integer> CAR_SIZE = new Vec2<>(50, 80);
    
    public Vec2<Integer> position;
    public Vec2<Integer> forwardVector;
    int anInt = 0;
    
    public Car(Vec2<Integer> position, Vec2<Integer> forwardVector) {
        this.position = position;
        this.forwardVector = forwardVector;
    }
    
    @Override
    public void Draw(Graphics2D g, Vec2<Float> scale) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.RED);
        
        Rectangle rect2 = new Rectangle((int) (position.x * scale.x - CAR_SIZE.x * scale.x / 2f), (int) (-position.y * scale.y - CAR_SIZE.y * scale.y / 2f), (int) (CAR_SIZE.x * scale.x), (int) (CAR_SIZE.y * scale.y));
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(anInt), (int) (position.x * scale.x - CAR_SIZE.x * scale.x / 2f), (int) (-position.y * scale.y - CAR_SIZE.y * scale.y / 2f));
        anInt += 15;
        anInt %= 360;
        g2d.transform(at);
        g2d.fill(rect2);
        g2d.dispose();
    }
}


