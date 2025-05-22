package de.frauas.objects;


import de.frauas.objects.datastructures.Vec2D;
import lombok.Getter;

import java.awt.*;
import java.util.function.Function;


@Getter
public class Obstacle {
    private final Vec2D startPoint;
    private final Vec2D endPoint;
    private final int height;

    public Obstacle(int xStart, int yStart, int xEnd, int yEnd, int height) {
        this.startPoint = new Vec2D(xStart, yStart);
        this.endPoint = new Vec2D(xEnd, yEnd);
        this.height = height;
    }

    public Obstacle(Vec2D startPoint, Vec2D endPoint, int height) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.height = height;
    }

    public void draw(Graphics g, Function<Vec2D, Vec2D> transformFunction){
        Vec2D startPoint = transformFunction.apply(this.startPoint);
        Vec2D endPoint = transformFunction.apply(this.endPoint);
        g.drawRect((int) startPoint.getX(), (int) endPoint.getY(), (int) Math.abs(endPoint.getX()-startPoint.getX()), (int) Math.abs(endPoint.getY()-startPoint.getY()));
    }

}
