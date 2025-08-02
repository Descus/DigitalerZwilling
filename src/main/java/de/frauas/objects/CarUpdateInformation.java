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
    CarStatus status;
    List<Integer> measurements;
    private final List<Boolean> infraredStatus;
    private final Vec3D position;
    private final double heading;
    int usTimestamp;

    /**
     * Constructs an instance of CarUpdateInformation with the provided details
     * about the car's state.
     * <p>
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
