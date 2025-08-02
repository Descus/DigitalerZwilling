package de.frauas.objects.interfaces;

import de.frauas.IDrawable;


/**
 * An interface representing an ultrasonic sensor used for detecting
 * and measuring the distance to the closest obstacle in its sensing range.
 * This interface extends the IDrawable interface, allowing the sensor to have
 * a visual representation within a scene or directly on a canvas.
 * <p>
 * Classes implementing this interface are expected to model the behavior and
 * capabilities of an ultrasonic sensor, including interacting with a scene's
 * spatial data to calculate distances to obstacles.
 * <p>
 * @author Ultrasonic
 */
public interface IUltrasonicSensor extends IDrawable {
    /**
     * Measures the distance to the closest obstacle detected by the ultrasonic sensor.
     * The measurement is based on the sensor's current surroundings and its detection range.
     * <p>
     * @return the distance to the closest obstacle in millimeters. If no obstacle is detected,
     *         the method may return a predefined maximum distance or a specific value indicating
     *         the absence of obstacles.
     */
    int distanceToClosestObstacle();
}
