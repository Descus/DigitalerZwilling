package de.frauas.objects;

import de.frauas.objects.car.CarStatus;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

//Contains all Information from the different car parts
// measurements and usTimestamp are part of the Ultrasonic Team

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
