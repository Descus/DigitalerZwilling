package de.frauas.objects.car;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.car.movement.MovementInstruction;
import de.frauas.objects.car.parts.SensorLogger;
import de.frauas.objects.interfaces.ICarObserver;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;
import de.frauas.objects.interfaces.IInfraredSensor;
import de.frauas.objects.car.parts.UltrasonicSensor;
import de.frauas.objects.car.parts.InfraredSensor;
import de.frauas.objects.trace.TraceType;
import de.frauas.objects.trace.ShiftedTrace;
import de.frauas.objects.trace.Trace;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.frauas.Settings.POINT_DEBUG_RADIUS;

@Getter
public class Car extends Transformable implements IDrawable {

    public static final int SENSOR_ANGLE_FR = 20;
    public static final int SENSOR_ANGLE_FL = 25;
    public static final int SENSOR_ANGLE_REAR = 35;
    public static final int FIRST_TIMESTAMP = 4395 + (int)(Math.random() * ((4403-4395) + 1));

    private final List<IUltrasonicSensor> ultraSonicSensors = new ArrayList<>();
    private final List<IInfraredSensor> infraredSensors = new ArrayList<>();
    private final List<ICarObserver> carObservers = new ArrayList<>();

    private final Trace trace;

    private CarStatus status = CarStatus.STOPPED;
    private int usTimestamp = FIRST_TIMESTAMP;


    public Car(Scene parent, Vec3D position, double headingDegree){
        this.parent = parent;
        trace = parent.getTrace();

        transform.setTranslation(position);
        transform.setRotation(headingDegree);

        //writing the first Lines to the US output.txt
        carObservers.add(SensorLogger.getOrCreate());
        notifyObservers(usTimestamp, Arrays.asList(0,0,0,0,0,0), usTimestamp);


        ultraSonicSensors.add(new UltrasonicSensor(this, "FL", new Vec3D(-45,  110, 0), SENSOR_ANGLE_FL, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FC", new Vec3D(0, 117.5, 0), 0, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FR", new Vec3D(45, 110, 0), -SENSOR_ANGLE_FR, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RL", new Vec3D(-45, -117.5, 0), -SENSOR_ANGLE_REAR + 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RC", new Vec3D(0, -117.5, 0), 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RR", new Vec3D(45, -117.5, 0), SENSOR_ANGLE_REAR + 180, parent));

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

    @Override
    public void drawInScene(Graphics g) {
        ultraSonicSensors.forEach(s -> s.drawInScene(g));
        infraredSensors.forEach(s -> s.drawInScene(g));
    }

    /**
     * Moving the car forward based on its velocity and heading.
     * @param dt Time step in seconds.
     */
    public void update(int time, double dt) {
        //new Thread(() -> ultrasonicUpdate(time)).start();
        ultrasonicUpdate(time);

        if (trace.getType() == TraceType.DEBUG) return;

        new Thread(() -> infraredUpdate(dt)).start();
    }

    public void reset(Vec3D position, double headingDegree){
        stop();  // Stop the car first
        transform.setTranslation(position); // Reset position
        transform.setRotation(headingDegree);
    }

    public void start() {
        status = CarStatus.RUNNING;
    }

    public void stop() {
        status = CarStatus.STOPPED;
    }

    public void pause() {
        status = CarStatus.PAUSED;
    }

    public void finish(){
        status = CarStatus.FINISHED;
    }

    private void applyMovementFromInstruction(double dt, MovementInstruction movementInstruction) {
        //Fahrbefehle ausführen
        if (!status.equals(CarStatus.RUNNING)) return;
        switch (movementInstruction) {
            case forward -> transform.translate(forward().normalize().scale(Settings.STEP_MM * dt));
            case left -> transform.rotate(Settings.TURN_DEG * dt);
            case right -> transform.rotate(-Settings.TURN_DEG * dt);
            case stop -> finish();
        }
    }

    private void notifyObservers(int time, List<Integer> measurements, int usTimestamp) {
        for (ICarObserver observer : carObservers) {
            observer.onCarUpdate(new CarUpdateInformation(status, time, measurements, usTimestamp));
        }
    }

    private MovementInstruction getMovementInstructionFromSensors(boolean[] sensorStatus) {
        boolean L = sensorStatus[2];
        boolean M = sensorStatus[1];
        boolean R = sensorStatus[0];

        if (L && !R) {
            return MovementInstruction.left; // links abbiegen
        } else if (!L && R) {
            return MovementInstruction.right; // rechts abbiegen
        } else { // wenn links und rechts false
            return M ? MovementInstruction.forward // falls mitte true
                    : MovementInstruction.stop; // falls mitte false
        }
    }

    private void ultrasonicUpdate(int time) {
        //try{
        List<Integer> measurements = new ArrayList<>();
        for (IUltrasonicSensor sensor : ultraSonicSensors) {
            int distance = sensor.distanceToClosestObstacle();

            measurements.add(distance);

            if (distance < 30)
                finish();

            //Thread.sleep(2000);
        }


        usTimestamp = UltrasonicSensor.iterateUSTimestamp(usTimestamp);
        notifyObservers(time, measurements, usTimestamp);
        //} catch (InterruptedException e){
        //    e.printStackTrace();
        //}
    }

    private void infraredUpdate(double dt) {
        boolean[] irHit = new boolean[infraredSensors.size()];
        for (int s = 0; s < infraredSensors.size(); s++) {
            IInfraredSensor ir = infraredSensors.get(s);
            irHit[s] = ir.isOnTrack((ShiftedTrace) trace);
        }

        MovementInstruction movementInstruction = getMovementInstructionFromSensors(irHit);

        applyMovementFromInstruction(dt, movementInstruction);
    }
}
