package de.frauas.GUI;

import de.frauas.GUI.objects.*;

import javax.swing.*;
import java.awt.geom.Point2D;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,800);

        AxisPanel axisPanel = new AxisPanel();
        axisPanel.addPoint(new  Point2D.Double(200,370));
        axisPanel.addPoint(new Point2D.Double(894,256));

        Car car = new Car();
        Obstacle obstacle1 = new Obstacle(50,50,370,190,20);
        Obstacle obstacle2 = new Obstacle(50,310,370,450,20);
        Obstacle obstacle3 = new Obstacle(640,190,690,240,5);
        axisPanel.addObstacle(obstacle1);
        axisPanel.addObstacle(obstacle2);
        axisPanel.addObstacle(obstacle3);
        frame.add(axisPanel);



        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
