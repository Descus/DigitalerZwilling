package de.frauas.objects;

import de.frauas.objects.car.CarStatus;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the information required to update the state of a car.
 * This class contains the status, measurements, position, and other
 * relevant details about the car's current state.
 * @author UltraSonic
 */
@Getter
public class CarUpdateInformation {
    /**
     * Represents the current operational status of the car.
     * It is an instance of the {@link CarStatus} enum, which defines
     * various states like STARTED, RUNNING, PAUSED, STOPPED, and FINISHED.
     * This variable is used to track the car's state during its lifecycle.
     */
    CarStatus status;
    /**
     * A list of integer measurements collected from car sensors.
     * Each value in the list represents a specific measurement captured by the sensors,
     * such as distance readings or environmental factors.
     */
    List<Integer> measurements;
    /**
     * A list of boolean values representing the status of the car's infrared sensors.
     * Each boolean value in the list corresponds to the operational state of an individual sensor,
     * where {@code true} indicates the sensor is active or detecting, and {@code false} indicates
     * the sensor is inactive or not detecting.
     */
    private final List<Boolean> infraredStatus;
    /**
     * Represents the 3D position of the car at the current state.
     * This is stored as a {@link Vec3D} object, which encapsulates
     * the X, Y, and Z coordinates. The position provides spatial
     * information about where the car is located in a three-dimensional space.
     */
    private final Vec3D position;
    /**
     * Represents the heading or orientation of the car in degrees.
     * This variable indicates the direction the car is facing at the current state.
     */
    private final double heading;
    /**
     * Represents a timestamp in microseconds that corresponds to the current state of the car.
     * This variable is used to record the precise moment in time when the car update
     * information is captured, enabling time-based tracking and analysis of the car's behavior.
     */
    int usTimestamp;

    /**
     * Constructs an instance of CarUpdateInformation with the provided details
     * about the car's state.
     *
     * @param status          the current operational status of the car
     * @param measurements    a list of integer sensor measurements from the car
     * @param infraredStatus  a list of boolean values representing the status of infrared sensors
     * @param position        the 3D position of the car as a Vec3D object
     * @param heading         the heading or orientation of the car, in degrees
     * @param usTimestamp     the timestamp in microseconds representing the current state
     */
    public CarUpdateInformation(CarStatus status, List<Integer> measurements, List<Boolean> infraredStatus, Vec3D position, double heading, int usTimestamp) {
        this.status = status;
        this.measurements = measurements;
        this.infraredStatus = infraredStatus;
        this.position = position;
        this.heading = heading;
        this.usTimestamp = usTimestamp;
    }
}
