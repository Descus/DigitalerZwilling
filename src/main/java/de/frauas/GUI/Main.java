package de.frauas.GUI;

import de.frauas.GUI.controllers.InfoPanel;
import de.frauas.GUI.controllers.ObstacleInfoPanel;
import de.frauas.GUI.objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,800);
        frame.setLayout(new BorderLayout());

        // create axis
        AxisPanel axisPanel = new AxisPanel();

        // dynamisch
        // XML -> read -> arrage -> save
        // create Object -> save Object
        // fetchXML("path of the XML file")

        // create Point
        axisPanel.addPoint(new Point2D.Double(950,450));
        axisPanel.addPoint(new Point2D.Double(800,450));
        axisPanel.addPoint(new Point2D.Double(720,450));
        axisPanel.addPoint(new Point2D.Double(664,400));
        axisPanel.addPoint(new Point2D.Double(628,328));
        axisPanel.addPoint(new Point2D.Double(564,280));
        axisPanel.addPoint(new Point2D.Double(452,264));
        axisPanel.addPoint(new Point2D.Double(320,250));
        axisPanel.addPoint(new Point2D.Double(100,250));


        // create Obstacle
        Obstacle obstacle1 = new Obstacle(50,50,370,190,20);
        Obstacle obstacle2 = new Obstacle(50,310,370,450,20);
        Obstacle obstacle3 = new Obstacle(640,190,690,240,5);

        //create Car
        Car car = new Car();
        car.setHeadingDegree(270);
        car.setPositionPoint(950,450);

        axisPanel.addObstacle(obstacle1);
        axisPanel.addObstacle(obstacle2);
        axisPanel.addObstacle(obstacle3);

        axisPanel.addCar(car);

        InfoPanel info = new InfoPanel(axisPanel,car);

        frame.add(axisPanel, BorderLayout.CENTER);
        frame.add(info, BorderLayout.EAST);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        axisPanel.startCar(car);
    }
}
