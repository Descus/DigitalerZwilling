package de.frauas.objects.car.parts;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.*;


public class UltrasonicSensor extends Transformable implements IUltrasonicSensor, IDrawable {

    public String name;
    public static final int MAX_DISTANCE = 300;
    public static final int MAX_ANGLE = 15;
    private final ISdf sceneDistanceField;
    public double stepSize = 0.1f;
    
    List<Vec3D> hits = new ArrayList<>();
    List<Vec3D> steps = new ArrayList<>();
    List<Line3D> lines = new ArrayList<>();

    public UltrasonicSensor(Transformable parent, String name, Vec3D positionOffset, double orientationAngle, ISdf sceneDistanceField) {
        this.name = name;
        this.sceneDistanceField = sceneDistanceField;
        this.parent = parent;
        transform.setTranslation(positionOffset);
        transform.setRotation(orientationAngle);
    }

    /**
     * Detects the closest obstacle within the sensor's defined range and angle by casting rays
     * in different directions and evaluating their intersection with a Signed Distance Field (SDF).
     * Returns the position of the closest obstacle detected.
     *
     * @return a Vec3D representing the position of the closest obstacle detected based on SDF values.
     *         If no obstacles are detected, returns a vector of length ~800.
     */
    public Vec3D getClosestPoint(){
        lines.clear();
        hits.clear();
        steps.clear();
        Vec3D closestPoint = Vec3D.right.scale(1000);
        Vec3D sensorPosition = getWorldPosition();
        for (double angle = -MAX_ANGLE; angle < MAX_ANGLE; angle += stepSize) {
            Vec3D currentPoint = castRay(forward().rotate(angle));
            if (currentPoint.length() < closestPoint.subtract(getWorldPosition()).length()) {
                closestPoint = currentPoint;
            }
        }
        return closestPoint;
    }

    /**
     * Casts a ray in a specified direction while interacting with a Signed Distance Field (SDF)
     * until an intersection is detected or the maximum travel distance is exceeded.
     * The method iteratively increments the ray's position based on the SDF value at the current position.
     * If an intersection with the surface is detected (SDF value <= 0), the method returns the intersected position.
     * If no intersection occurs within the maximum permitted distance, a fallback random scaled vector is returned.
     *
     * @param direction the direction vector in which the ray is to be cast.
     * @return the position of the intersection with the surface based on the SDF, or a fallback position if no intersection occurs.
     */
    private Vec3D castRay(Vec3D direction) {
        double travelDistance = 0;
        Vec3D normalDirection = direction.normalize();
        Vec3D currentPosition = getWorldPosition();

        while (travelDistance <= MAX_DISTANCE){
            double currentSdf = sceneDistanceField.getSDF(currentPosition);
            if (currentSdf <= 0) {
                hits.add(currentPosition);
                return currentPosition;
            }
            travelDistance += currentSdf;
            lines.add(new Line3D(this, currentPosition, currentPosition.add(normalDirection.scale(Math.max(currentSdf, 1)))));
            currentPosition = currentPosition.add(normalDirection.scale(Math.max(currentSdf, 1)));
            steps.add(currentPosition);
        }
        Random r = new Random();
        return direction.scale(r.nextInt(796,803));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        {
            g2d.transform(transform.toAffineTransform());
            g2d.setColor(Color.BLUE);
            g2d.drawRect(-15, -10, 30, 20);
        }
        g2d.dispose();
    }

    @Override
    public void drawInScene(Graphics g) {
        g.setColor(Color.cyan);
        Vec3D wp = getWorldPosition();
        Vec3D f = forward().scale(100);
        g.drawLine((int) wp.getX(), (int) wp.getY(), (int) (wp.getX() + f.getX()), (int) (wp.getY() + f.getY()) );
        if (!Settings.SHOW_DEBUG_RAYS) return;
        for (Line3D line : lines) {
            g.setColor(Color.BLUE);
            line.drawInScene(g);
        }
        for (Vec3D step : steps) {
            g.setColor(Color.GREEN);
            step.drawInScene(g);
        }
        for (Vec3D hit : hits) {
            g.setColor(Color.RED);
            hit.drawInScene(g);
        }
    }

    /* calculate the distance from the Sensor to the closest Point*/
    public double calculateDistance(Vec3D pos1, Vec3D pos2){
        return pos1.subtract(pos2).length();
    }

    /* Adds a random value between 200 - 300 ms to the timestamp */
    public static int iterateUSTimestamp(int previousTimestamp) {
        Random random = new Random();
        int sd = 50;
        int mean = 250;
        return (previousTimestamp + (int)(random.nextGaussian()* sd +mean)) ;
    }

    @Override
    public int distanceToClosestObstacle() {
        return (int) calculateDistance(getWorldPosition(), getClosestPoint());
    }
}



