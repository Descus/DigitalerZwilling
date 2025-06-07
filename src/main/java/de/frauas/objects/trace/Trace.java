package de.frauas.objects.trace;

import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public abstract class Trace extends Transformable {

    protected final List<Vec3D> points = new ArrayList<>();

    public Trace(Scene parent, List<Vec3D> points) {
        this.parent = parent;
        this.points.addAll(points);
    }

    public Trace(Scene parent) {
        this.parent = parent;
    }

    public void addPoint(Vec3D point) {
        points.add(point);
    }

    public Vec3D first(){
        return points.getFirst();
    }

    public Vec3D last(){
        return points.getLast();
    }
    
    protected abstract void createLines();
    
    public abstract void drawLines(Graphics g);
    
    public void drawPoints(Graphics g) {
            points.forEach(p -> transformPoint(p).draw(g));
    }
}
