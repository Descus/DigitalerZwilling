package de.frauas.objects;

import de.frauas.objects.car.CarStatus;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Scenario-Group
 * A data transfer object (DTO) that encapsulates a snapshot of the car's state at a specific point in time.
 * It holds combined information from various car components, such as sensors, position, and operational status.
 * This class is used to pass around a consistent set of car data between different parts of the simulation.
 */
@Getter
public class CarUpdateInformation {
    CarStatus status;
    List<Integer> measurements;
    private final List<Boolean> infraredStatus;
    private final Vec3D position;
    private final double heading;
    int usTimestamp;

    public CarUpdateInformation(CarStatus status, List<Integer> measurements, List<Boolean> infraredStatus, Vec3D position, double heading, int usTimestamp) {
        this.status = status;
        this.measurements = measurements;
        this.infraredStatus = infraredStatus;
        this.position = position;
        this.heading = heading;
        this.usTimestamp = usTimestamp;
    }
}
