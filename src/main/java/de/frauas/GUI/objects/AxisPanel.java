package de.frauas.GUI.objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

public class AxisPanel extends JPanel {

    private double scale;
    private int x0, y0, x1, y1;

    // points in data coords (mm):
    private final List<Point2D.Double> points = new ArrayList<>(); // A list of (x,y) coordinates representing the robot’s path.
    private static final int POINT_RADIUS = 10;

    // Obstacles list
    private final List<Obstacle> obstacles = new ArrayList<>();

    //Car
    private Car car;

    //Moving Car
    private Timer carTimer;
    private int segIndex = 0;
    private double segTraveled = 0;
    private long startTime;
    private double totalTime ;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Define the dimension of the frame
        int width = getWidth();
        int height = getHeight();

        // Uniform scale between X and Y
        // Padding around the drawable area so axis labels don’t get cut off
        int MARGIN = 60;
        double drawableWidth = width - 2 * MARGIN;
        double drawableHeight = height - 2 * MARGIN;
        // Calculates scaling to convert real-world mm into pixels.
        // Data range in millimeters
        // Define the physical size of the virtual environment (in mm).
        double x_MAX = 1000;
        double y_MAX = 500;
        scale = Math.min(drawableWidth / x_MAX, drawableHeight / y_MAX);

        // Define drawing corners in coords
        x0 = MARGIN;
        y0 = height - MARGIN;
        // Endpoint of each axis depend on the scale
        x1 = x0 + (int)(x_MAX * scale);
        y1 = y0 - (int)(y_MAX * scale);

        // Draw axes
        g2.drawLine(x0, y0, x1, y0); // X-axis
        g2.drawLine(x0, y0, x0, y1); // Y-axis
        // Draw support axes
        g2.drawLine(x1, y0, x1, y1); // support Y-axis
        g2.drawLine(x0, y1, x1, y1); // support X-axis

        // X-axis ticks & labels
        // X axis with ticks
        int NUM_X_TICKS = 20;
        int LABEL_MARGIN = 25;
        int TICK_Size = 5;
        for (int i = 0; i <= NUM_X_TICKS; i++) {
            double value = i * ( x_MAX / NUM_X_TICKS);
            int x = x0 + (int)(value * scale);

            // tick mark
            if (i != 0 && i != NUM_X_TICKS) {
                g2.drawLine(x, y0 , x, y0 + TICK_Size);
            }

            // label
            String label = String.format("%.0f", value);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x - labelWidth/2 , y0 + LABEL_MARGIN);
        }

        // Y-axis ticks & labels
        // Y axis with ticks
        int NUM_Y_TICKS = 10;
        for (int i = 0; i <= NUM_Y_TICKS; i++) {
            double value = i * (y_MAX / NUM_Y_TICKS);
            int y = y0 - (int)(value * scale);

            // tick mark
            if (i != 0 && i != NUM_Y_TICKS) {
                g2.drawLine(x0 - TICK_Size, y, x0 , y);
            }

            // label
            String label = String.format("%.0f", value);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x0 - LABEL_MARGIN - labelWidth, y + labelWidth/2);
        }

        // draw points(Robot´s path)
        g2.setColor(Color.BLACK);
        for (Point2D.Double p : points) {
            Point2D.Double point = toPixel(p);
            g2.fillOval(
                    (int) (point.x - POINT_RADIUS/2),
                    (int) (point.y - POINT_RADIUS/2),
                    POINT_RADIUS,
                    POINT_RADIUS
            );
        }

        // draw lines between consecutive points
        g2.setColor(Color.RED);
        if (points.size() >= 2) {
            for (int i = 1; i < points.size(); i++) {
                Point2D.Double point = toPixel(points.get(i - 1));
                Point2D.Double pointNext = toPixel(points.get(i));
                g2.drawLine((int) point.x, (int) point.y, (int) pointNext.x, (int) pointNext.y);
            }
        }

        // draw Obstacle
        for (Obstacle obs : obstacles) {

            // map data→pixel
            Point2D.Double startPoint = toPixel(obs.getStartPoint());
            Point2D.Double endPoint = toPixel(obs.getEndPoint());

            int rx = (int) startPoint.x;
            int ry = (int) endPoint.y;
            int w = (int) Math.abs(endPoint.x - startPoint.x);
            int h = (int) Math.abs(endPoint.y - startPoint.y);

            // set Obstacle color
            g2.setColor(Color.BLACK);
            g2.fillRect(rx, ry, w, h);

        }

        //draw Car
        Point2D.Double carPosition = car.getPositionPoint();
        carPosition = toPixel(carPosition);
        double carP_Width = car.getWidth() * scale;
        double carP_Height = car.getHeight() * scale;
        Point2D.Double drawPoint = new Point2D.Double();
        drawPoint.x = carPosition.x - (carP_Width/ 2);
        drawPoint.y = carPosition.y - (carP_Height / 2);
        double radian = Math.toRadians(car.getHeadingDegree());
        g2.rotate(radian,carPosition.x, carPosition.y);


        g2.setColor(Color.RED);
        g2.drawRect((int) drawPoint.x, (int) drawPoint.y, (int) carP_Width, (int) carP_Height);
        g2.setColor(Color.BLACK);
        g2.fillOval(
                (int) (carPosition.x - POINT_RADIUS/2),
                (int) (carPosition.y - POINT_RADIUS/2),
                POINT_RADIUS,
                POINT_RADIUS
        );

        g2.dispose();
    }

    // convert to Pixel with x0,y0 base
    private Point2D.Double toPixel(Point2D.Double oldPoint) {
        int px = x0 + (int)(oldPoint.x * scale);
        int py = y0 - (int)(oldPoint.y * scale);
        return new Point2D.Double(px, py);
    }

    // Add a point and repaint panel
    public void addPoint(Point2D.Double point) {
        points.add(new Point2D.Double(point.x, point.y));
        repaint();
    }

    // Add obstacle
    public void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
        repaint();
    }

    //add Car
    public void addCar(Car car) {
        this.car = car;
        repaint();
    }

    //start a Car
    public void startCar() {
        this.car = car;
        segIndex = 0;
        segTraveled = 0;
        Point2D.Double startPoint = points.get(0);
        car.setPositionPoint(startPoint.x,startPoint.y);
        if (carTimer != null && carTimer.isRunning()){
            carTimer.stop();
        }
        startTime = System.currentTimeMillis();
        carTimer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movingCar();
            }
        });

        carTimer.start();
    }

    private void movingCar(){
        // moving distance
        double traveled = car.getVelocity() * ((System.currentTimeMillis() - startTime)/1000.0);

        while (traveled > 0 && segIndex < points.size()-1) {
            totalTime += (System.currentTimeMillis() - startTime)/1000.0;
            startTime = System.currentTimeMillis();
            Point2D.Double indexPoint = points.get(segIndex);
            Point2D.Double nextPoint =  points.get(segIndex +1);
            double deltaX =  (nextPoint.x - indexPoint.x);
            double deltaY =  (nextPoint.y - indexPoint.y);
            double segLength = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));

            double remain = segLength - segTraveled;

            double heading = Math.toDegrees(Math.atan2(deltaX, deltaY));
            car.setHeadingDegree(heading);

            if (traveled < remain){
                segTraveled += traveled;
                double ratio = segTraveled / segLength;
                car.setPositionPoint(indexPoint.x + deltaX * ratio, indexPoint.y +deltaY * ratio);
                traveled = 0;
            } else {
                car.setPositionPoint(nextPoint.x,nextPoint.y);
                traveled -= remain;
                segIndex++;
                segTraveled = 0;
            }

            System.out.println( totalTime+ " "+ segTraveled + " " + segLength);

        }
        repaint();
        // stop when we reach the end of the path
        if (segIndex >= points.size() - 1) {
            carTimer.stop();
        }
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Point2D.Double> getPoints() {
        return points;
    }

    public Car getCar() {
        return car;
    }

    public String getCarStatus() {
        if (segIndex >= points.size() - 1)
            return "Finished";
        if (carTimer != null && carTimer.isRunning())
            return "Moving";
        return "Stopped";
    }
}
