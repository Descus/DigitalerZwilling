package de.frauas.objects;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Point2D;


@Getter
@Setter
public class Car {
    private final double velocity = 100;
    private Point2D.Double positionPoint; // current data-coordinates (mm)
    private double headingDegree = 0;
    
    public Car(double x, double y, double headingDegree){
        setPositionPoint(x, y);
        this.headingDegree = headingDegree;
    }
    
    public void setPositionPoint(double x, double y) {
        this.positionPoint = new Point2D.Double(x, y);
    }

}