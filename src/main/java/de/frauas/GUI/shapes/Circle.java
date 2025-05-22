package de.frauas.GUI.shapes;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Circle extends Ellipse2D {

    public double x, y, radius;

    public Circle(double x, double y, double radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getWidth() {
        return 2*radius;
    }

    @Override
    public double getHeight() {
        return 2*radius;
    }

    @Override
    public boolean isEmpty() {
        return radius <= 0;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.radius = Math.min(w/2, h/2);
    }

    @Override
    public Rectangle2D getBounds2D() {
        double diam = radius*2;
        return new Rectangle2D.Double(x-diam/2, y-diam/2, diam, diam);
    }
}
