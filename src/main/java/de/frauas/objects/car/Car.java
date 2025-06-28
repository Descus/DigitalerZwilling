package de.frauas.objects.car;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.car.parts.SensorLogger;
import de.frauas.objects.interfaces.ICarObserver;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;
import de.frauas.objects.interfaces.IInfraredSensor;
import de.frauas.objects.car.parts.UltrasonicSensor;
import de.frauas.objects.car.parts.InfraredSensor;
import de.frauas.objects.trace.RoadTrace;
import de.frauas.objects.trace.ShiftedTrace;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static de.frauas.Settings.POINT_DEBUG_RADIUS;
import static de.frauas.objects.car.parts.UltrasonicSensor.firstUSTimestamp;


@Getter
public class Car extends Transformable implements IDrawable {
    
    List<ICarObserver> carObservers = new ArrayList<>();
    private final double velocity = 10;
    public static final int SENSOR_ANGLE_FR = 20;
    public static final int SENSOR_ANGLE_FL = 25;
    public static final int SENSOR_ANGLE_REAR = 35;
    private IMovementStrategy movementStrategy;
    public int usTimestamp = firstUSTimestamp;

    @Setter
    private CarStatus status = CarStatus.STOPPED;
    private final List<IUltrasonicSensor> ultraSonicSensors = new ArrayList<>();
    private final List<IInfraredSensor> infraredSensors = new ArrayList<>();
    
    private final ISdf sceneDistanceField;

    public Car(Scene parent, Vec3D position, double headingDegree){
        this.parent = parent;
        this.sceneDistanceField = parent;
        this.transform = new Transform2D(position, headingDegree);
        
        try {
            movementStrategy = new SensorMovementStrategy(transform, (ShiftedTrace) parent.getTrace(), infraredSensors);
        }catch (ClassCastException e) {
            movementStrategy = new InterpolationMovementStrategy(transform, (RoadTrace) parent.getTrace());
        }
        //writing the first Lines to the US output.txt
        new SensorLogger("output.txt", firstUSTimestamp );


        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(-45,  110, 0), SENSOR_ANGLE_FL, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(0, 117.5, 0), 0, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(45, 110, 0), -SENSOR_ANGLE_FR, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(-45, -117.5, 0), -SENSOR_ANGLE_REAR + 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(0, -117.5, 0), 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, new Vec3D(45, -117.5, 0), SENSOR_ANGLE_REAR + 180, parent));

        infraredSensors.add(new InfraredSensor(this, new Vec3D(10 , 60, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(0, 60, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D( -10, 60, 0)));

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
            ultraSonicSensors.forEach(s -> s.draw(g2));
            infraredSensors.forEach(s -> s.draw(g2));
        }
        g2.dispose();
    }

    /**
     * Moving the car forward based on its velocity and heading.
     * @param dt Time step in seconds.
     */
    public void update(int time, double dt) {

        //Ultrasonic Sensors

        List<Integer> measurements = new ArrayList<>();

        for (IUltrasonicSensor sensor : ultraSonicSensors) {
            int distance = sensor.distanceToClosestObstacle();

            measurements.add(distance);

            if(distance < 30)
                status = CarStatus.STOPPED;
        }
        usTimestamp = UltrasonicSensor.iterateUSTimestamp(usTimestamp);

        notifyObservers(time, measurements, usTimestamp);

        
        if (status != CarStatus.RUNNING) return;
        
        movementStrategy.move(dt);
        // Use transform's forward vector for direction:

    }

    private void notifyObservers(int time, List<Integer> measurements, int usTimestamp) {
        for (ICarObserver observer : carObservers) {
            observer.onCarUpdate(new CarUpdateInformation(status, time, measurements, usTimestamp));
        }
    }
}
