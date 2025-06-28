package de.frauas.objects;

import de.frauas.objects.car.CarStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CarUpdateInformation {
    CarStatus status;
    int timestamp;
    List<Integer> measurements;
    int usTimestamp;

    public CarUpdateInformation(CarStatus status, int timestamp, List<Integer> measurements, int usTimestamp) {
        this.status = status;
        this.timestamp = timestamp;
        this.measurements = measurements;
        this.usTimestamp = usTimestamp;
    }
}
