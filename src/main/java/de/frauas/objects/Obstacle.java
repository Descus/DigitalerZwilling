package de.frauas.objects;


import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;
import java.util.function.Function;


@Getter
public class Obstacle extends Transformable implements ISdf {
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

    public void draw(Graphics g){
        Vec3D startPoint = transformPoint(this.startPoint);
        Vec3D endPoint = transformPoint(this.endPoint);
        g.drawRect((int) startPoint.getX(), (int) endPoint.getY(), (int) Math.abs(endPoint.getX()-startPoint.getX()), (int) Math.abs(endPoint.getY()-startPoint.getY()));
    }

    @Override
    public double getSDF(Vec3D otherPosition) {
        Vec3D halfDim = dimension.scale(0.5);
        Vec3D translatedPosition = otherPosition.subtract(startPoint.add(halfDim));
        Vec3D d = translatedPosition.abs().subtract(halfDim);
        return d.max(Vec3D.zero).length() + Math.min(d.maxComponent(), 0);
    }
}
