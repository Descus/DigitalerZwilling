package de.frauas.objects.datastructures;


import lombok.Getter;

import java.awt.*;
import java.util.function.Function;

@Getter
public class Line2D {
    private final Vec2D start, end;

    public Line2D(Vec2D start, Vec2D end) {
        this.start = start;
        this.end = end;
    }

    public Vec2D lerp(double t) {
        return start.add(end.subtract(start).scale(t));
    }

    public double length(){
        return end.subtract(start).length();
    }
    
    public void draw(Graphics g, Function<Vec2D, Vec2D> transformFunction) {
        Vec2D start = transformFunction.apply(this.start);
        Vec2D end = transformFunction.apply(this.end);
        g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
    }
    
    public boolean rightOfLine(Vec2D point) {
        Vec2D ap = point.subtract(start);
        Vec2D abPerp = end.subtract(start).perpendicular();
        return ap.dotProd(abPerp) >= 0;
    }
    
    public String toString(){
        return String.format("%s -> %s", start.toString(), end.toString());
    }
}
