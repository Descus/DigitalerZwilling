package de.frauas.objects.datastructures;


import de.frauas.IDrawable;
import de.frauas.objects.Transformable;
import lombok.Getter;

import java.awt.*;

@Getter
public class Line3D extends Transformable implements IDrawable {
    private final Vec3D start, end;

    public Line3D(Transformable parent, Vec3D start, Vec3D end) {
        this.parent = parent;
        this.start = start;
        this.end = end;
    }

    public double length(){
        return end.subtract(start).length();
    }
    
    public boolean rightOfLine(Vec3D point) {
        Vec3D ap = point.subtract(start);
        Vec3D abPerp = end.subtract(start).perpendicular();
        return ap.dotProd(abPerp) >= 0;
    }
    
    public String toString(){
        return String.format("%s -> %s", start.toString(), end.toString());
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            g.drawLine(
                    (int) this.start.getX(),
                    (int) this.start.getY(),
                    (int) this.end.getX(),
                    (int) this.end.getY());
        }
        g2.dispose();
    }

    @Override
    public void drawInScene(Graphics g) {
        g.drawLine(
                (int) this.start.getX(),
                (int) this.start.getY(),
                (int) this.end.getX(),
                (int) this.end.getY());
    }
}
