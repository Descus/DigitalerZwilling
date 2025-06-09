package de.frauas.objects;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;
import de.frauas.objects.parts.UltrasonicSensor;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static de.frauas.Settings.POINT_DEBUG_RADIUS;


@Getter
public class Car extends Transformable implements IDrawable {
    private final double velocity = 100;
    public static final int SENSOR_ANGLE = 30;

    private CarStatus status = CarStatus.STOPPED;
    private final List<IUltrasonicSensor> sensors = new ArrayList<>();
    
    private final ISdf sceneDistanceField;

    public Car(Scene parent, Vec3D position, double headingDegree){
        this.parent = parent;
        this.sceneDistanceField = parent;
        this.transform = new Transform2D(position, headingDegree);
        Vec3D CarSizeOffset = Settings.CAR_SIZE.scale(0.5);
        sensors.add(new UltrasonicSensor(this, new Vec3D(-CarSizeOffset.getX(),  CarSizeOffset.getY(), 0), SENSOR_ANGLE));
        sensors.add(new UltrasonicSensor(this, new Vec3D(0, CarSizeOffset.getY(), 0), 0));
        sensors.add(new UltrasonicSensor(this, new Vec3D(CarSizeOffset.getX(), CarSizeOffset.getY(), 0), -SENSOR_ANGLE));
        sensors.add(new UltrasonicSensor(this, new Vec3D(-CarSizeOffset.getX(), -CarSizeOffset.getY(), 0), -SENSOR_ANGLE + 180));
        sensors.add(new UltrasonicSensor(this, new Vec3D(0, -CarSizeOffset.getY(), 0), 180));
        sensors.add(new UltrasonicSensor(this, new Vec3D(CarSizeOffset.getX(), -CarSizeOffset.getY(), 0), SENSOR_ANGLE + 180));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());

            Vec3D drawPoint = new Vec3D(
                    0 - (Settings.CAR_SIZE.getX() / 2),
                    0 - (Settings.CAR_SIZE.getY() / 2),
                    0
            );

            g2.setColor(Color.RED);
            g2.drawRect(
                    (int) drawPoint.getX(),
                    (int) drawPoint.getY(),
                    (int) Settings.CAR_SIZE.getX(),
                    (int) Settings.CAR_SIZE.getY()
            );
            g2.setColor(Color.BLUE);
            g2.fillOval(
                    (int) (0 - (double) POINT_DEBUG_RADIUS / 2),
                    (int) (0 - (double) POINT_DEBUG_RADIUS / 2),
                    POINT_DEBUG_RADIUS,
                    POINT_DEBUG_RADIUS
            );
            sensors.forEach(s -> s.draw(g2));
        }
        g2.dispose();
    }

    //TODO Check / Drive / Stop logic here
}
