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
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class Car extends Transformable implements IDrawable {

    public static final int SPEED_OF_SOUND = 34;
    public static final int SENSOR_ANGLE_FR = 20;
    public static final int SENSOR_ANGLE_FL = 25;
    public static final int SENSOR_ANGLE_REAR = 35;
    public static final int FIRST_TIMESTAMP = 4395 + (int)(Math.random() * ((4403-4395) + 1));

    private final List<IUltrasonicSensor> ultraSonicSensors = new ArrayList<>();
    private final List<IInfraredSensor> infraredSensors = new ArrayList<>();
    private final List<ICarObserver> carObservers = new ArrayList<>();
    
    private final ConcurrentLinkedQueue<Integer> measurements = new ConcurrentLinkedQueue<>(Arrays.asList(0,0,0,0,0,0));
    private final ConcurrentLinkedQueue<Boolean> infraredStatus = new ConcurrentLinkedQueue<>(Arrays.asList(false, false, false));

    private final Trace trace;
    private CarStatus status = CarStatus.STOPPED;
    private int currentTimeMillis = FIRST_TIMESTAMP;


    public Car(Scene parent, Vec3D position, double headingDegree){
        this.parent = parent;
        trace = parent.getTrace();

        transform.setTranslation(position);
        transform.setRotation(headingDegree);

        //writing the first Lines to the US output.txt
        carObservers.add(SensorLogger.getOrCreate());
        notifyObservers();
        
        ultraSonicSensors.add(new UltrasonicSensor(this, "FL", new Vec3D(-45,  110, 0), SENSOR_ANGLE_FL, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FC", new Vec3D(0, 117.5, 0), 0, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FR", new Vec3D(45, 110, 0), -SENSOR_ANGLE_FR, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RL", new Vec3D(-45, -117.5, 0), -SENSOR_ANGLE_REAR + 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RC", new Vec3D(0, -117.5, 0), 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RR", new Vec3D(45, -117.5, 0), SENSOR_ANGLE_REAR + 180, parent));

        infraredSensors.add(new InfraredSensor(this, new Vec3D(10 , 60, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(0, 60, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D( -10, 60, 0)));

        new Thread(this::ultrasonicUpdate).start();

        new Thread(this::infraredUpdate).start();

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());

            Vec3D drawPoint = new Vec3D(
                    0 - (Settings.CAR.SIZE.getX() / 2),
                    0 - (Settings.CAR.SIZE.getY() / 2),
                    0
            );

            g2.setColor(Color.RED);
            g2.drawRect(
                    (int) drawPoint.getX(),
                    (int) drawPoint.getY(),
                    (int) Settings.CAR.SIZE.getX(),
                    (int) Settings.CAR.SIZE.getY()
            );
            g2.setColor(Color.BLUE);
            if (Settings.DEBUG.ENABLED) {
                g2.fillOval(
                        (int) (0 - (double) Settings.DEBUG.POINT_RADIUS / 2),
                        (int) (0 - (double) Settings.DEBUG.POINT_RADIUS / 2),
                        Settings.DEBUG.POINT_RADIUS,
                        Settings.DEBUG.POINT_RADIUS
                );
            }
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
    public void update(double dt) {
        currentTimeMillis += (int) (dt * 1000);
        notifyObservers();
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

    private void applyMovementFromInstruction(MovementInstruction movementInstruction) {
        //Fahrbefehle ausführen
        if (!status.equals(CarStatus.RUNNING)) return;
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        switch (movementInstruction) {
            case forward -> transform.translate(forward().normalize().scale(Settings.CAR.MOVEMENT.SPEED_MM_P_S * dt));
            case left -> transform.rotate(Settings.CAR.MOVEMENT.TURN_SPEED_DEG_P_S * dt);
            case right -> transform.rotate(-Settings.CAR.MOVEMENT.TURN_SPEED_DEG_P_S * dt);
            case stop -> finish();
        }
    }

    private void notifyObservers() {
        for (ICarObserver observer : carObservers) {
            observer.onCarUpdate(new CarUpdateInformation(status, measurements.stream().toList(), infraredStatus.stream().toList(), getWorldPosition(), transform.getRotation(), currentTimeMillis));
        }
    }
    
    public void addObserver(ICarObserver observer) {
        if (!carObservers.contains(observer)) {
            carObservers.add(observer);
        }
    }
    
    public void removeObserver(ICarObserver observer) {
        carObservers.remove(observer);
    }

    private MovementInstruction getMovementInstructionFromSensors(boolean[] sensorStatus) {
        boolean L = sensorStatus[2];
        boolean M = sensorStatus[1];
        boolean R = sensorStatus[0];
        
        infraredStatus.clear();
        infraredStatus.add(L);
        infraredStatus.add(M);
        infraredStatus.add(R);

        if (L && !R) {
            return MovementInstruction.left; // links abbiegen
        } else if (!L && R) {
            return MovementInstruction.right; // rechts abbiegen
        } else { // wenn links und rechts false
            return M ? MovementInstruction.forward // falls mitte true
                    : MovementInstruction.stop; // falls mitte false
        }
    }

    private void ultrasonicUpdate() {
        while(true) {
            try {
                List<Integer> measurementsFront = new ArrayList<>();
                List<Integer> measurementsBack = new ArrayList<>();
                
                for (int i = 0; i < ultraSonicSensors.size() / 2; i++) {
                    int distance = (ultraSonicSensors.get(i).distanceToClosestObstacle() / 10);
                    Thread.sleep(distance * 2 / SPEED_OF_SOUND);
                    measurementsFront.add(distance);
                    Thread.sleep(Settings.CAR.ULTRASONIC.CHECK_DELAY_MS, 12);
                }
                
                for (Integer distance : measurementsFront) {
                    if (distance < 3)
                        finish();
                }
                Thread.sleep(Settings.CAR.ULTRASONIC.CHECK_DELAY_MS);
                
                for (int i = ultraSonicSensors.size() / 2; i < ultraSonicSensors.size(); i++) {
                    int distance = (ultraSonicSensors.get(i).distanceToClosestObstacle() / 10);
                    Thread.sleep(distance * 2 / SPEED_OF_SOUND);
                    measurementsBack.add(distance);
                    Thread.sleep(Settings.CAR.ULTRASONIC.CHECK_DELAY_MS, 12);
                }

                for (Integer distance : measurementsBack) {
                    if (distance < 3)
                        finish();
                }
                Thread.sleep(Settings.CAR.ULTRASONIC.CHECK_DELAY_MS);
                
                measurements.clear();
                measurements.addAll(measurementsFront);
                measurements.addAll(measurementsBack);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void infraredUpdate() {
        if (trace.getType() == TraceType.DEBUG) return;
        while(true) {
            boolean[] irHit = new boolean[infraredSensors.size()];
            for (int s = 0; s < infraredSensors.size(); s++) {
                IInfraredSensor ir = infraredSensors.get(s);
                irHit[s] = ir.isOnTrack((ShiftedTrace) trace);
            }

            MovementInstruction movementInstruction = getMovementInstructionFromSensors(irHit);

            applyMovementFromInstruction(movementInstruction);
            try {
                Thread.sleep(Settings.CAR.INFRARED.CHECK_DELAY_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
}
