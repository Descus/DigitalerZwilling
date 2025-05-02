package de.frauas.ultrasonic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SonicObject {
    public double x, y, z;

    public SonicObject(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceTo(SonicObject other) {
        return Math.sqrt(Math.pow(x - other.x, 2) +
                Math.pow(y - other.y, 2) +
                Math.pow(z - other.z, 2));
    }
}
