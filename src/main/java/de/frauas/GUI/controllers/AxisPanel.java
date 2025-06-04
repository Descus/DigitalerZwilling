package de.frauas.GUI.controllers;

import de.frauas.Settings;
import de.frauas.objects.Car;
import de.frauas.objects.Obstacle;
import de.frauas.objects.datastructures.Vec2D;
import de.frauas.objects.trace.CatmullRomTrace;
import de.frauas.objects.trace.RoadTrace;
import de.frauas.objects.trace.ShiftedTrace;
import de.frauas.objects.trace.Trace;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.dto.StartPosition;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AxisPanel extends JPanel {

    private double scale;
    private int x0, y0;

    // points in data coords (mm):
    // A list of (x,y) coordinates representing the robot’s path.
    @Getter
    private RoadTrace roadTrace;

    @Getter
    private Trace shiftedTrace;

    private static final int POINT_RADIUS = Settings.POINT_DEBUG_RADIUS;

    // Obstacles list
    @Getter
    private final List<Obstacle> obstacles = new ArrayList<>();

    //Car
    @Getter
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
        double x_MAX = Settings.SCENE_CANVAS.getX();
        double y_MAX = Settings.SCENE_CANVAS.getY();
        scale = Math.min(drawableWidth / x_MAX, drawableHeight / y_MAX);

        g2.setColor(Color.BLUE); // oder eine andere Farbe deiner Wahl
        if (shiftedTrace != null) {
            shiftedTrace.drawLines(g2, this::toPixel);
            if (Settings.DEBUG) {
                shiftedTrace.drawPoints(g2, this::toPixel);
            }
        }




        // Define drawing corners in coords
        x0 = MARGIN;
        y0 = height - MARGIN;
        // Endpoint of each axis depend on the scale
        int x1 = x0 + (int)(x_MAX * scale);
        int y1 = y0 - (int)(y_MAX * scale);

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
        if (Settings.DEBUG)
            roadTrace.drawPoints(g2, this::toPixel);

        // draw lines between consecutive points
        g2.setColor(Color.RED);
        roadTrace.drawLines(g2, this::toPixel);

        // draw Obstacle
        for (Obstacle obs : obstacles) {
            // set Obstacle color
            g2.setColor(Color.BLACK);
            obs.draw(g2, this::toPixel);
        }

        //draw Car
        Vec2D carPosition = toPixel(car.getPositionPoint());
        double carP_Width = Settings.CAR_SIZE.getX() * scale;
        double carP_Height = Settings.CAR_SIZE.getY() * scale;
        Vec2D drawPoint = new Vec2D(
                carPosition.getX() - (carP_Width/ 2),
                carPosition.getY() - (carP_Height / 2)
        );
        double radian = Math.toRadians(car.getHeadingDegree());
        g2.rotate(radian, carPosition.getX(), carPosition.getY());


        g2.setColor(Color.RED);
        g2.drawRect((int) drawPoint.getX(), (int) drawPoint.getY(), (int) carP_Width, (int) carP_Height);
        g2.setColor(Color.BLACK);
        g2.fillOval(
                (int) (carPosition.getX() - (double) POINT_RADIUS /2),
                (int) (carPosition.getY() - (double) POINT_RADIUS /2),
                POINT_RADIUS,
                POINT_RADIUS
        );

        g2.dispose();
    }

    // convert to Pixel with x0,y0 base
    private Vec2D toPixel(Vec2D oldPoint) {
        int px = x0 + (int)(oldPoint.getX() * scale);
        int py = y0 - (int)(oldPoint.getY() * scale);
        return new Vec2D(px, py);
    }

    // Add a point and repaint panel
    public void addPoint(Vec2D point) {
        roadTrace.addPoint(new Vec2D(point.getX(), point.getY()));
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
        segIndex = 0;
        segTraveled = 0;
        if (carTimer != null && carTimer.isRunning()){
            carTimer.stop();
        }
        startTime = System.currentTimeMillis();
        carTimer = new Timer(40, _ -> movingCar());

        carTimer.start();
    }

    private void movingCar(){
        // moving distance
        double traveled = car.getVelocity() * ((System.currentTimeMillis() - startTime)/1000.0);

        while (traveled > 0 && segIndex < roadTrace.getPoints().size()-1) {
            totalTime += (System.currentTimeMillis() - startTime)/1000.0;
            startTime = System.currentTimeMillis();
            Vec2D indexPoint = roadTrace.getPoints().get(segIndex);
            Vec2D nextPoint =  roadTrace.getPoints().get(segIndex +1);
            double deltaX =  (nextPoint.getX() - indexPoint.getX());
            double deltaY =  (nextPoint.getY() - indexPoint.getY());
            double segLength = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));

            double remain = segLength - segTraveled;

            double heading = Math.toDegrees(Math.atan2(deltaX, deltaY));
            car.setHeadingDegree(heading);

            if (traveled < remain){
                segTraveled += traveled;
                double ratio = segTraveled / segLength;
                car.setPositionPoint(indexPoint.getX() + deltaX * ratio, indexPoint.getY() +deltaY * ratio);
                traveled = 0;
            } else {
                car.setPositionPoint(nextPoint.getX(), nextPoint.getY());
                traveled -= remain;
                segIndex++;
                segTraveled = 0;
            }

            System.out.println( totalTime+ " "+ segTraveled + " " + segLength);

        }
        repaint();
        // stop when we reach the end of the path
        if (segIndex >= roadTrace.getPoints().size() - 1) {
            carTimer.stop();
        }
    }

    public String getCarStatus() {
        if (segIndex >= roadTrace.getPoints().size() - 1)
            return "Finished";
        if (carTimer != null && carTimer.isRunning())
            return "Moving";
        return "Stopped";
    }
    public void pauseCar() {
        if (carTimer != null && carTimer.isRunning()) {
            carTimer.stop();
        }
    }

    public void continueCar() {
        if (carTimer != null && !carTimer.isRunning()) {
            startTime = System.currentTimeMillis();
            carTimer.start();
        }
    }

    public void stopCar() {
        if (carTimer != null) {
            carTimer.stop();
            segIndex = roadTrace.getPoints().size(); // Skip to end
            repaint();
        }
    }

    public void resetCar() {
        if (carTimer != null) {
            carTimer.stop();
        }
        if (!roadTrace.getPoints().isEmpty()) {
            Vec2D startPoint = roadTrace.first();
            car.setPositionPoint(startPoint.getX(), startPoint.getY());
            segIndex = 0;
            segTraveled = 0;
            totalTime = 0;
            repaint();
        }
    }
    
    public void populate(Scenario scenario) {
        StartPosition startPosition = scenario.getStartPosition();

        this.addCar(new Car(startPosition.getX(), startPosition.getY(), startPosition.getHeading()));

        this.roadTrace = new CatmullRomTrace();
        this.addPoint(new Vec2D(startPosition.getX(), startPosition.getY()));
        scenario.getTrace().forEach(point -> this.addPoint(new Vec2D(point.getX(), point.getY())));

        ArrayList<Vec2D> points = new ArrayList<>(roadTrace.getPoints());
        this.shiftedTrace = new ShiftedTrace(points);

        scenario.getObjects().forEach(object -> this.addObstacle(new Obstacle(object.getXStart(), object.getYStart(), object.getXEnd(), object.getYEnd(), object.getHeight())));
    }
}