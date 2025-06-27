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

    public CarUpdateInformation(CarStatus status, int timestamp, List<Integer> measurements) {
        this.status = status;
        this.timestamp = timestamp;
        this.measurements = measurements;
    }
}
