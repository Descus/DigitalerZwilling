package de.frauas.GUI.objects;

public class Obstacle {
    private Point startPoint;
    private Point endPoint;
    private int height;

    public Obstacle(Point startPoint, Point endPoint, int height){
        this.startPoint = startPoint ;
        this.endPoint = endPoint ;
        this.height = height;


    }
}
