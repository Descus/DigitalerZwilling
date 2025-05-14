package de.frauas.GUI.objects;


import java.awt.geom.Point2D;

public class Obstacle {
    private Point2D.Double startPoint;
    private Point2D.Double endPoint;
    private int height;

    public Obstacle(int xStart, int yStart, int xEnd, int yEnd, int height) {
        this.startPoint = new Point2D.Double(xStart, yStart);
        this.endPoint = new Point2D.Double(xEnd, yEnd);
        this.height = height;
    }

    public Point2D.Double getStartPoint() {return startPoint;}
    public Point2D.Double getEndPoint() {return endPoint;}
    public int getHeight() { return height; }

}
