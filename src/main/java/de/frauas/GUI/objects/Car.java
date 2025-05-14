package de.frauas.GUI.objects;

import java.awt.geom.Point2D;

public class Car {
    private final double width = 157;
    private final double height = 240;
    private final double velocity = 100;
    private Point2D.Double positionPoint; // current data-coordinates (mm)
    private double headingRad = 0;

    public Point2D.Double getPositionPoint() {return positionPoint;}
    public double getWidth() { return width;}
    public double getHeight() { return height;}
    public double getVelocity() { return velocity;}

    public void setPositionPoint(int x, int y) {
        this.positionPoint = new Point2D.Double(x, y);
    }
    public double getHeadingRad() {return headingRad;}
    public void setHeadingRad(double headingRad) {
        this.headingRad = headingRad;
    }

}