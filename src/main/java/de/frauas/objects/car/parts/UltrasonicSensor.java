package de.frauas.objects.car.parts;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;

import java.util.Random;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Part of Ultrasonic Team
 *
 * Represents an ultrasonic sensor used to detect obstacles within a scene
 * using ray marching and Signed Distance Fields (SDFs).
 *
 * The sensor casts rays in a configurable angle and distance range to detect
 * the nearest obstacle in front of it.
 */
public class UltrasonicSensor extends Transformable implements IUltrasonicSensor, IDrawable {

    public String name;
    
    private final ISdf sceneDistanceField;

    ConcurrentLinkedQueue<Vec3D> hits = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Vec3D> steps = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Line3D> lines = new ConcurrentLinkedQueue<>();

    /**
     * Part of Ultrasonic Team
     *
     * Constructs a new UltrasonicSensor attached to a parent transform,
     * placed with a given offset and rotation, and interacting with a provided SDF scene.
     *
     * @param parent            The parent object to which this sensor is attached.
     * @param name              The name of the sensor.
     * @param positionOffset    The position offset relative to the parent.
     * @param orientationAngle  The orientation angle of the sensor.
     * @param sceneDistanceField The SDF scene used for ray marching.
     */
    public UltrasonicSensor(Transformable parent, String name, Vec3D positionOffset, double orientationAngle, ISdf sceneDistanceField) {
        this.name = name;
        this.sceneDistanceField = sceneDistanceField;
        this.parent = parent;
        transform.setTranslation(positionOffset);
        transform.setRotation(orientationAngle);
    }

    /**
     * Part of Ultrasonic Team
     *
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
        // iterates through the angles accoring to the step size
        for (double angle = -Settings.CAR.ULTRASONIC.MAX_ANGLE; angle < Settings.CAR.ULTRASONIC.MAX_ANGLE; angle += Settings.CAR.ULTRASONIC.STEP_SIZE) {
            // casts a ray to get the position off where it hits an object at the current angle
            Vec3D currentPoint = castRay(forwardVector().rotate(angle));
            // checks if the length of the hit  is smaller than the current smallest hit and saves the closest Point
            if (currentPoint.length() < closestPoint.subtract(getWorldPosition()).length()) {
                closestPoint = currentPoint;
            }
        }
        return closestPoint;
    }

    /**
     * part of Ultrasonic Team
     *
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
        // returns the position of a hit after hitting an Object
        while (travelDistance <= Settings.CAR.ULTRASONIC.MAX_DISTANCE){
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
        //Sets Values to a random high value if outside of the measuring distance
        Random r = new Random();
        return direction.scale(r.nextInt(7960,8030));
    }

    /**
     * Part of XML Team
     *
     * Renders the sensor's body on the car (top-down view).
     * Used for visualizing the sensor itself.
     *
     * @param g The graphics context.
     */

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

    /**
     * Part of XML Team
     *
     * Renders the ray marching visualization in the scene.
     * Includes ray paths (blue), step points (green), and hits (red),
     * if debug mode is enabled.
     *
     * @param g The graphics context.
     */

    @Override
    public void drawInScene(Graphics g) {
        if (Settings.DEBUG.ENABLED) {
            g.setColor(Color.cyan);
            Vec3D wp = getWorldPosition();
            Vec3D f = forwardVector().scale(100);
            g.drawLine((int) wp.getX(), (int) wp.getY(), (int) (wp.getX() + f.getX()), (int) (wp.getY() + f.getY()) );
        }
        // Rays are shown in Blue
        if (!Settings.DEBUG.SHOW_RAYS) return;
        for (Line3D line : lines) {
            g.setColor(Color.BLUE);
            line.drawInScene(g);
        }
        // The Stepsize Intervals are shown in Green
        for (Vec3D step : steps) {
            g.setColor(Color.GREEN);
            step.drawInScene(g);
        }
        // Object hits are shown in red
        for (Vec3D hit : hits) {
            g.setColor(Color.RED);
            hit.drawInScene(g);
        }
    }

    /**
     * Part of Ultrasonic Team
     *
     * Calculates the Euclidean distance between two 3D points.
     *
     * @param pos1 First position.
     * @param pos2 Second position.
     * @return Distance between the two positions.
     */
    public double calculateDistance(Vec3D pos1, Vec3D pos2){
        return pos1.subtract(pos2).length();
    }

    /**
     * Part of Ultrasonic Team
     *
     * Calculates the distance from the sensor to the nearest detected obstacle.
     *
     * @return Distance in integer form (rounded down).
     */

    @Override
    public int distanceToClosestObstacle() {
        return (int) calculateDistance(getWorldPosition(), getClosestPoint());
    }
}