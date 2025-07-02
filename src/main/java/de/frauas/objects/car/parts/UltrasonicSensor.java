package de.frauas.objects.car.parts;

import de.frauas.IDrawable;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;

import java.awt.*;
import java.util.Random;


public class UltrasonicSensor extends Transformable implements IUltrasonicSensor, IDrawable {

    public String name;
    public static final int MAX_DISTANCE = 300;
    public static final int MAX_ANGLE = 15;
    private final ISdf sceneDistanceField;
    public double stepSize = 0.1f;

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
        Vec3D closestPoint = Vec3D.identity.scale(1000);
        for (double angle = -MAX_ANGLE; angle < MAX_ANGLE; angle += stepSize) {
            Vec3D currentPoint = castRay(forward().rotate(angle));
            if (currentPoint.length() < closestPoint.length()) {
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

        while (travelDistance < MAX_DISTANCE){
            double currentSdf = sceneDistanceField.getSDF(currentPosition);
            if (currentSdf <= 0) {
                return currentPosition;
            }
            travelDistance += currentSdf;
            currentPosition = currentPosition.add(normalDirection.scale(travelDistance));
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

    /* calculate the distance from the Sensor to the closest Point*/
    public int calculateDistance(Vec3D closestPoint, Vec3D sensorPosition){
        return (int) closestPoint.subtract(sensorPosition).length();
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
        return calculateDistance(getWorldPosition(), getClosestPoint());
    }
}



