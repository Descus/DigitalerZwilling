package de.frauas.objects.trace;

import de.frauas.objects.datastructures.Line2D;
import de.frauas.objects.datastructures.Vec2D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class RoadTrace {

    protected final List<Vec2D> points = new ArrayList<>();
    protected final List<Line2D> lines = new ArrayList<>();

    public RoadTrace(List<Vec2D> points) {
        this.points.addAll(points);
        createLines();
    }

    public RoadTrace() {
    }

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

    public Vec2D lerp(double t){
        int segment = (int) t;
        if(t > lines.size() - 1)
            return last();
        return lines.get(segment).lerp(t - segment);
    }

    public void drawLines(Graphics g, Function<Vec2D, Vec2D> transformFunction){
        lines.forEach(l -> l.draw(g, transformFunction));
    }

    public void drawPoints(Graphics g, Function<Vec2D, Vec2D> transformFunction){
        points.forEach(p -> p.draw(g, transformFunction));
    }

    protected void createLines(){
        lines.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line2D(points.get(i), points.get(i + 1)));
        }
    }
}
