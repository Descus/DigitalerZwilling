package de.frauas.objects.obstacle;


import de.frauas.IDrawable;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;

@Getter
public class Obstacle extends Transformable implements ISdf, IDrawable {
    private final Vec3D startPoint;
    private final Vec3D endPoint;
    private final Vec3D dimension;
    private final int height;

    public Obstacle(Scene parent, int xStart, int yStart, int xEnd, int yEnd, int height) {
        this.parent = parent;
        this.startPoint = new Vec3D(xStart, yStart, 0);
        this.endPoint = new Vec3D(xEnd, yEnd, 0);
        this.dimension = endPoint.subtract(startPoint).abs();
        this.height = height;
    }

    public Obstacle(Scene parent, Vec3D startPoint, Vec3D endPoint, int height) {
        this.parent = parent;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.dimension = endPoint.subtract(startPoint).abs();
        this.height = height;
    }

    @Override
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            g2.drawRect((int) startPoint.getX(), (int) startPoint.getY(), (int) dimension.getX(), (int) dimension.getY());
        }
        g2.dispose();
    }

    @Override
    public double getSDF(Vec3D otherPosition) {
        Vec3D halfDim = dimension.scale(0.5);
        Vec3D translatedPosition = otherPosition.subtract(startPoint.add(halfDim));
        Vec3D d = translatedPosition.abs().subtract(halfDim);
        d = new Vec3D(d.getX(), d.getY(), -Double.MAX_VALUE);
        return d.max(Vec3D.zero).length() + Math.min(d.maxComponent(), 0);
    }
}
