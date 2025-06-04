package de.frauas.objects.trace;

import de.frauas.objects.datastructures.Vec2D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public abstract class Trace {

    protected final List<Vec2D> points = new ArrayList<>();

    public Trace(List<Vec2D> points) {
        this.points.addAll(points);
        createLines();
    }

    public Trace() {}

    public void addPoint(Vec2D point) {
        points.add(point);
        createLines();
    }

    public Vec2D first(){
        return points.getFirst();
    }

    public Vec2D last(){
        return points.getLast();
    }
    
    protected abstract void createLines();
    
    public abstract void drawLines(Graphics g, Function<Vec2D, Vec2D> transformFunction);
    
    public void drawPoints(Graphics g, Function<Vec2D, Vec2D> transformFunction) {
            points.forEach(p -> p.draw(g, transformFunction));
    }
}
