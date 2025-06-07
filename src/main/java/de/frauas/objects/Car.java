package de.frauas.objects;

import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.parts.UltrasonicSensor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Car extends Transformable {
    private final double velocity = 100;
    public static final int SENSOR_ANGLE = 30;
    
    private final Transform2D transform;
    private final List<UltrasonicSensor> sensors = new ArrayList<>();
    
    private final ISdf sceneDistanceField;

    public Car(Scene parent, double x, double y, double headingDegree){
        this.parent = parent;
        this.sceneDistanceField = parent;
        this.transform = new Transform2D(new Vec3D(x, y, 0), headingDegree);
        sensors.add(new UltrasonicSensor(this, new Vec3D(-25, 40, 0), SENSOR_ANGLE));
        sensors.add(new UltrasonicSensor(this, new Vec3D(0, 40, 0), 0));
        sensors.add(new UltrasonicSensor(this, new Vec3D(25, 40, 0), -SENSOR_ANGLE));
        sensors.add(new UltrasonicSensor(this, new Vec3D(-25, -40, 0), -SENSOR_ANGLE + 180));
        sensors.add(new UltrasonicSensor(this, new Vec3D(0, -40, 0), 180));
        sensors.add(new UltrasonicSensor(this, new Vec3D(25, -40, 0), SENSOR_ANGLE + 180));
    }
    
    //TODO Check / Drive / Stop logic here
}
