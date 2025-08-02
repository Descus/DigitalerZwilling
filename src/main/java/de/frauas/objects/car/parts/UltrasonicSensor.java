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
 * Represents an ultrasonic sensor used to detect obstacles within a scene
 * using ray marching and Signed Distance Fields (SDFs).
 * The sensor casts rays in a configurable angle and distance range to detect
 * the nearest obstacle in front of it.
 * @author Ultrasonic
 */
public class UltrasonicSensor extends Transformable implements IUltrasonicSensor, IDrawable {

    /**
     * Represents the name of the ultrasonic sensor.
     * <p>
     * This variable is used to identify the sensor, which can be useful
     * for distinguishing between multiple sensors attached to the same parent
     * or within a system. The name is assigned during the construction of the
     * UltrasonicSensor instance.
     */
    public String name;
    
    /**
     * Represents the Signed Distance Field (SDF) used by the ultrasonic sensor for
     * detecting obstacles and surfaces within a 3D space. The SDF is a mathematical
     * representation that provides the shortest distance from a given point to the
     * nearest surface or boundary in the scene. Negative values indicate positions
     * inside the boundary, while positive values indicate positions outside.
     * <p>
     * This field is a critical component for ray marching and collision detection,
     * enabling the sensor to calculate object proximity and geometry interactions.
     * It is provided during the sensor's initialization.
     */
    private final ISdf sceneDistanceField;


    /**
     * A thread-safe queue storing the results of intersection points (hits)
     * detected by ray casting in a Signed Distance Field (SDF) during ultrasonic sensor operations.
     * Each element in the queue represents a 3D positional vector (Vec3D) where a
     * ray has intersected with an obstacle or surface.
     * The `hits` queue is utilized primarily for visualization purposes and debugging,
     * enabling the rendering of intersected points in the scene during ray marching
     * calculations.
     */
    ConcurrentLinkedQueue<Vec3D> hits = new ConcurrentLinkedQueue<>();

    /**
     * A thread-safe, non-blocking queue that holds step points calculated during
     * ray marching operations performed by the sensor. Each step represents an
     * intermediate position in the path of a ray as it interacts with the Signed Distance Field (SDF).
     * These steps are visualized when debug mode is enabled to aid in understanding the
     * sensor's operation and ray marching process.
     * Each element in the queue is a Vec3D object representing a 3D point.
     */
    ConcurrentLinkedQueue<Vec3D> steps = new ConcurrentLinkedQueue<>();
    /**
     * Represents a thread-safe, concurrent collection of 3D lines used in ray tracing
     * or visualization tasks. Each line in the collection is defined as a directed
     * segment in 3D space, represented by instances of the `Line3D` class.
     * This queue supports operations that facilitate safe manipulation of its elements
     * across multiple threads without external synchronization.
     * <p>
     * The `lines` collection is primarily used in the context of the `UltrasonicSensor`
     * class to store and manage the 3D lines generated during ray-marching operations.
     * These lines may represent visualized rays, detected collision paths, or other
     * sensor outputs and are processed asynchronously to improve performance.
     */
    ConcurrentLinkedQueue<Line3D> lines = new ConcurrentLinkedQueue<>();

    /**
     * Constructs a new UltrasonicSensor attached to a parent transform,
     * placed with a given offset and rotation, and interacting with a provided SDF scene.
     * <p>
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
     * Detects the closest obstacle within the sensor's defined range and angle by casting rays
     * in different directions and evaluating their intersection with a Signed Distance Field (SDF).
     * Returns the position of the closest obstacle detected.
     * @return a Vec3D representing the position of the closest obstacle detected based on SDF values.
     * <p>
     * If no obstacles are detected, returns a vector of length ~800.
     */
    public Vec3D getClosestPoint(){
        lines.clear();
        hits.clear();
        steps.clear();
        Vec3D closestPoint = Vec3D.right.scale(1000);
        // iterates through the angles according to the step size
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
     * Casts a ray in a specified direction while interacting with a Signed Distance Field (SDF)
     * until an intersection is detected or the maximum travel distance is exceeded.
     * The method iteratively increments the ray's position based on the SDF value at the current position.
     * If an intersection with the surface is detected (SDF value <= 0), the method returns the intersected position.
     * If no intersection occurs within the maximum permitted distance, a fallback random scaled vector is returned.
     * <p>
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
     * Renders the sensor's body on the car (top-down view).
     * Used for visualizing the sensor itself.
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
     * Renders the ray marching visualization in the scene.
     * Includes ray paths (blue), step points (green), and hits (red),
     * if debug mode is enabled.
     * <p>
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
     * Calculates the Euclidean distance between two 3D points.
     * @param pos1 First position.
     * @param pos2 Second position.
     * @return Distance between the two positions.
     */
    public double calculateDistance(Vec3D pos1, Vec3D pos2){
        return pos1.subtract(pos2).length();
    }

    /**
     * Calculates the distance from the sensor to the nearest detected obstacle.
     * @return Distance in integer form (rounded down).
     */

    @Override
    public int distanceToClosestObstacle() {
        return (int) calculateDistance(getWorldPosition(), getClosestPoint());
    }
}