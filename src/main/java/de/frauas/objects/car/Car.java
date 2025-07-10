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
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class Car extends Transformable implements IDrawable {

    public static final int SPEED_OF_SOUND = 34;
    public static final int SENSOR_ANGLE_FR = 20;
    public static final int SENSOR_ANGLE_FL = 25;
    public static final int SENSOR_ANGLE_REAR = 35;
    
    private static final Random random = new Random();

    private final List<IUltrasonicSensor> ultraSonicSensors = new ArrayList<>();
    private final List<IInfraredSensor> infraredSensors = new ArrayList<>();
    private final ConcurrentLinkedQueue<ICarObserver> carObservers = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Integer> measurements = new ConcurrentLinkedQueue<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    private final ConcurrentLinkedQueue<Boolean> infraredStatus = new ConcurrentLinkedQueue<>(Arrays.asList(false, false, false));

    private final SensorLogger sensorLogger;
    private final Trace trace;
    private AtomicReference<CarStatus> status = new AtomicReference<>(CarStatus.STOPPED);
    private int currentTimeMillis = random.nextInt(4395, 4403);
    private boolean resetting = false;


    public Car(Scene parent, Vec3D position, double headingDegree) {
        this.parent = parent;
        trace = parent.getTrace();

        transform.setTranslation(position);
        transform.setRotation(headingDegree);

        //writing the first Lines to the US output.txt
        sensorLogger = new SensorLogger();
        carObservers.add(sensorLogger);
        notifyObservers();

        ultraSonicSensors.add(new UltrasonicSensor(this, "FL", new Vec3D(-45, 110, 0), SENSOR_ANGLE_FL, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FC", new Vec3D(0, 117.5, 0), 0, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FR", new Vec3D(45, 110, 0), -SENSOR_ANGLE_FR, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RL", new Vec3D(-45, -117.5, 0), -SENSOR_ANGLE_REAR + 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RC", new Vec3D(0, -117.5, 0), 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RR", new Vec3D(45, -117.5, 0), SENSOR_ANGLE_REAR + 180, parent));

        infraredSensors.add(new InfraredSensor(this, new Vec3D(10, 90, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(0, 90, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(-10, 90, 0)));

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
     *
     * @param dt Time step in seconds.
     */
    public void update(double dt) {
    }

    public void reset(Vec3D position, double headingDegree) {
        resetting = true;
        stop();  // Stop the car first
        currentTimeMillis = random.nextInt(4395, 4403);
        measurements.clear();
        infraredStatus.clear();
        
        measurements.addAll(Arrays.asList(0, 0, 0, 0, 0, 0));
        infraredStatus.addAll(Arrays.asList(false, false, false));
        
        sensorLogger.reset();
        transform.setTranslation(position); // Reset position
        transform.setRotation(headingDegree);
        
        notifyObservers();
        resetting = false;
    }

    public void start() {
        status.set(CarStatus.RUNNING);
    }

    public void stop() {
        status.set(CarStatus.STOPPED);
    }

    public void pause() {
        status.set(CarStatus.PAUSED);
    }

    public void finish() {
        status.set(CarStatus.FINISHED);
    }
    
    private void forward(){
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        transform.translate(forwardVector().normalize().scale(Settings.CAR.MOVEMENT.SPEED_MM_P_S * dt));
    }
    
    private void left(){
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        transform.rotate(random.nextDouble(88, 143) * dt);
    }
    
    private void right(){
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        transform.rotate(-random.nextDouble(30, 57) * dt);
    }
    
    private void notifyObservers() {
        for (ICarObserver observer : carObservers) {
            observer.onCarUpdate(new CarUpdateInformation(status.get(), measurements.stream().toList(), infraredStatus.stream().toList(), getWorldPosition(), transform.getRotation(), currentTimeMillis));
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

    private void getMovementInstructionFromSensors(boolean[] sensorStatus) {
        boolean L = sensorStatus[2];
        boolean M = sensorStatus[1];
        boolean R = sensorStatus[0];

        infraredStatus.clear();
        infraredStatus.add(L);
        infraredStatus.add(M);
        infraredStatus.add(R);

        if (L && !R) {
            left(); // links abbiegen
        } else if (!L && R) {
            right(); // rechts abbiegen
        } else { // wenn links und rechts false
            if (M) {
                forward();
            } 
            else {
                finish();
            }
        }
    }

    private void ultrasonicUpdate() {
        while (true) {
            try {
                List<Integer> measurementsFront = new ArrayList<>();
                List<Integer> measurementsBack = new ArrayList<>();
                int lastTimeStep = iterateUSTimestamp();

                for (int i = 0; i < ultraSonicSensors.size() / 2; i++) {
                    int distance = (ultraSonicSensors.get(i).distanceToClosestObstacle() / 10);
                    measurementsFront.add(distance);
                    Thread.sleep(lastTimeStep / 6);
                }

                for (Integer distance : measurementsFront) {
                    if (distance < 3)
                        finish();
                }

                for (int i = ultraSonicSensors.size() / 2; i < ultraSonicSensors.size(); i++) {
                    int distance = (ultraSonicSensors.get(i).distanceToClosestObstacle() / 10);
                    measurementsBack.add(distance);

                    Thread.sleep(lastTimeStep / 6);
                }

                for (Integer distance : measurementsBack) {
                    if (distance < 3)
                        finish();
                }

                measurements.clear();
                measurements.addAll(measurementsFront);
                measurements.addAll(measurementsBack);

                
                currentTimeMillis += lastTimeStep;
                if (resetting) return;
                notifyObservers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void infraredUpdate() {
        try {
            if (trace.getType() == TraceType.DEBUG) return;
            Thread.sleep(2000);
            while (true) {
                if (status.get() != CarStatus.RUNNING) continue;
                boolean[] irHit = new boolean[infraredSensors.size()];
                for (int s = 0; s < infraredSensors.size(); s++) {
                    IInfraredSensor ir = infraredSensors.get(s);
                    irHit[s] = ir.isOnTrack((ShiftedTrace) trace);
                }

                getMovementInstructionFromSensors(irHit);
                
                Thread.sleep(Settings.CAR.INFRARED.CHECK_DELAY_MS);

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int iterateUSTimestamp() {
        return (int)(random.nextGaussian(250, 50)); // 200 - 300
    }
}
