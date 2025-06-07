package de.frauas.objects.datastructures;


import de.frauas.objects.Transformable;
import lombok.Getter;

import java.awt.*;
import java.util.function.Function;

@Getter
public class Line3D {
    private final Vec3D start, end;

    public Line3D(Vec3D start, Vec3D end) {
        this.start = start;
        this.end = end;
    }

    public Vec3D lerp(double t) {
        return start.add(end.subtract(start).scale(t));
    }

    public double length(){
        return end.subtract(start).length();
    }
    
    public void draw(Graphics g, Function<Vec3D, Vec3D> transformFunction) {
        Vec3D start = transformFunction.apply(this.start);
        Vec3D end = transformFunction.apply(this.end);
        g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
    }
    
    public boolean rightOfLine(Vec3D point) {
        Vec3D ap = point.subtract(start);
        Vec3D abPerp = end.subtract(start).perpendicular();
        return ap.dotProd(abPerp) >= 0;
    }
    
    public String toString(){
        return String.format("%s -> %s", start.toString(), end.toString());
    }
}
