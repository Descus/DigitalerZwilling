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

// part of Ultrasonic Team
public class UltrasonicSensor extends Transformable implements IUltrasonicSensor, IDrawable {

    public String name;
    
    private final ISdf sceneDistanceField;

    ConcurrentLinkedQueue<Vec3D> hits = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Vec3D> steps = new ConcurrentLinkedQueue<>();
    ConcurrentLinkedQueue<Line3D> lines = new ConcurrentLinkedQueue<>();

    //part of Ultrasonic Team
    //Sets the Ultrasonic Sensor Position and Orientation in the Scene
    public UltrasonicSensor(Transformable parent, String name, Vec3D positionOffset, double orientationAngle, ISdf sceneDistanceField) {
        this.name = name;
        this.sceneDistanceField = sceneDistanceField;
        this.parent = parent;
        transform.setTranslation(positionOffset);
        transform.setRotation(orientationAngle);
    }

    /**
     * part of Ultrasonic Team
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
    //Part of XML Team
    //Draws the Ultrasonic Sensors onto the car
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
    //part of XML Team
    //Draws the Rays of the RayMarching Algorithm in our scene and colors Objects hit in Red. Important for testing Purposes
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

    /* part of the Ultrasonic Team
    calculate the distance from the Sensor to the closest Point*/
    public double calculateDistance(Vec3D pos1, Vec3D pos2){
        return pos1.subtract(pos2).length();
    }

    //part of the Ultrasonic Team
    //calculates the distance to the closest Obstacle from the World Position for further calculations.
    @Override
    public int distanceToClosestObstacle() {
        return (int) calculateDistance(getWorldPosition(), getClosestPoint());
    }
}



