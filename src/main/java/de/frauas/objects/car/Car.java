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

/**
 * Represents a car in the simulation that is capable of processing sensor input and
 * interacting with observers. It extends Transformable and implements IDrawable to
 * manage its position, rotation, and graphical representation in the simulation.
 * <p> * The car is equipped with ultrasonic and infrared sensors to navigate and detect
 * obstacles or track edges. Sensor measurements and car state are logged and
 * communicated to observers through a notification system.
 * <p>
 * @author Scenario
 */
@Getter
public class Car extends Transformable implements IDrawable {
    /**
     * Represents the speed of sound in air, measured in centimeters per millisecond (cm/ms).
     * Used in the calculation of distances by ultrasonic sensors mounted on the vehicle.
     * This constant is specifically relevant to the Ultrasonic Sensors Team's operations
     * to derive accurate measurements based on the time-of-flight principle.
     */
    // sets the angles at which the Ultrasonic Sensors are mounted on the vehicle (part of the Ultrasonic Team)
    public static final int SPEED_OF_SOUND = 34;
    /**
     * Represents the default sensor angle for the front-right (FR) ultrasonic sensor in the car.
     * This constant defines the angular position of the front-right sensor relative
     * to the car's forward direction in degrees.
     * <p>     * The sensor angle is used to configure and calculate the field of view
     * or operational orientation for the front-right ultrasonic sensor during sensor
     * data collection and obstacle detection.
     */
    public static final int SENSOR_ANGLE_FR = 20;
    /**
     * Represents the fixed angle of the front-left ultrasonic sensor, measured in degrees.
     * This constant defines the orientation of the sensor relative to the car's forward direction.
     * It is used to calculate sensor measurements and integrate them into the car's navigation system.
     */
    public static final int SENSOR_ANGLE_FL = 25;
    /**
     * Represents the predefined angle in degrees for the rear sensor of the car.
     * This constant is used to configure the orientation of the rear-facing
     * sensor relative to the car's frame of reference.
     * <p>     * Value: 35
     * <p>     * The sensor orientation is critical in determining the detection field
     * and ensuring correct positioning for accurate obstacle measurement and
     * avoidance using the car's ultrasonic sensing system.
     * <p>     * This configuration is typically utilized in scenarios where rearward
     * obstacle detection plays a role in the car's navigation or operational logic.
     */
    public static final int SENSOR_ANGLE_REAR = 35;

    /**
     * A shared instance of the Random class used to generate random numbers
     * throughout the Car class. This provides a consistent source of randomness
     * for various calculations or decisions, such as setting random angles or
     * delays in the car's behavior.
     * <p>     * This field is marked as final to ensure that the same Random instance is
     * used, avoiding potential issues related to instantiating multiple objects
     * unnecessarily and ensuring consistent random behavior within the class.
     * <p>     * Being static allows this instance to be shared across all instances of
     * the Car class, reducing memory usage and ensuring that the same random
     * sequence is utilized for reproducibility in certain scenarios.
     */
    private static final Random random = new Random();

    /**
     * A list of ultrasonic sensors used by the car to measure distances to obstacles.
     * This list is populated with instances of {@link IUltrasonicSensor}, which provide
     * distance measurements through their respective methods.
     * <p>     * The ultrasonic sensors are integral to the car's navigation system, enabling it
     * to detect potential obstacles in its environment. Each sensor operates
     * independently and provides distance measurements that are utilized together
     * to determine the car's movement and avoid collisions.
     * <p>     * This variable is managed internally by the car's various systems, and its data
     * is processed in real-time to update the car’s state and notify associated observers.
     */
    private final List<IUltrasonicSensor> ultraSonicSensors = new ArrayList<>();
    /**
     * A list of infrared sensors installed on the car, used to detect the position of the car
     * relative to the track. These sensors implement the {@link IInfraredSensor} interface and
     * are responsible for providing input data to the car's movement control logic.
     * <p>     * The infrared sensors continuously monitor the track's boundaries and communicate
     * state information (such as whether they are "on track") to assist in determining
     * appropriate movement instructions (e.g., forward, left, right).
     * <p>     * This list is immutable and initialized as an empty list, ensuring that the car always
     * maintains a constant and predetermined set of sensors throughout its lifecycle.
     */
    private final List<IInfraredSensor> infraredSensors = new ArrayList<>();
    /**
     * A thread-safe queue for managing observers subscribed to the car's updates.
     * <p>     * This queue stores instances of {@link ICarObserver} and is used to implement
     * the observer pattern for the {@code Car} class. Observers in this queue
     * are notified of updates, such as changes in the car's state, sensor readings,
     * or position, through the {@code notifyObservers()} method in the {@code Car} class.
     * <p>     * The use of a {@code ConcurrentLinkedQueue} ensures safe handling of observer
     * subscriptions and notifications in a multi-threaded environment, as multiple
     * threads may add or remove observers or notify them concurrently.
     */
    private final ConcurrentLinkedQueue<ICarObserver> carObservers = new ConcurrentLinkedQueue<>();

    /**
     * A thread-safe queue to store the latest six ultrasonic sensor measurements.
     * <p>     * Each element in the queue represents a measurement recorded by the sensors,
     * initialized with default values of 0. The queue is used to maintain a consistent
     * history of six measurements, which can be utilized for sensor data processing and
     * other functionalities. The queue's size and structure ensure that it can accommodate
     * the required number of recorded measurements without manual resizing or modifications.
     * <p>     * Note: This queue is initialized with six placeholder integers (0, 0, 0, 0, 0, 0)
     * to provide a default structure for immediate access and manipulation.
     */
    private final ConcurrentLinkedQueue<Integer> measurements = new ConcurrentLinkedQueue<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    /**
     * A thread-safe queue storing the current state of the car’s infrared sensors.
     * Each element in the queue represents the status of one infrared sensor,
     * where {@code true} indicates that the sensor detects the track, and {@code false} means it does not.
     * <p>     * The queue holds the statuses of three sensors as follows:
     * - Left sensor
     * - Middle sensor
     * - Right sensor
     * <p>     * When initialized, all sensors are set to {@code false}, indicating no track detection.
     * <p>     * This variable is used to continuously track and update the state of the infrared sensors
     * as part of the car's movement control logic.
     */
    private final ConcurrentLinkedQueue<Boolean> infraredStatus = new ConcurrentLinkedQueue<>(Arrays.asList(false, false, false));

    /**
     * A dedicated logger for ultrasonic sensor data in the Car system.
     * <p>     * The `sensorLogger` variable is an instance of the SensorLogger class,
     * which is responsible for recording and managing data generated by the car’s
     * ultrasonic sensors. It observes updates from the car and logs this
     * information into a structured format for analysis.
     * <p>     * The logger operates by receiving periodic updates about sensor readings
     * and writing this information into a CSV file. The logged data includes
     * front and rear sensor measurements along with timestamps, aiding in the
     * monitoring and debugging of car behavior.
     * <p>     * This logger is utilized as part of the Ultrasonic Team's implementation
     * for real-time measurement tracking and contributes to maintaining
     * a historical record of car sensor activity.
     * <p>     * The `sensorLogger` is a key utility within the Car class and plays a central
     * role in ensuring data consistency for the vehicle's ultrasonic measurement system.
     * <p>     */
    private final SensorLogger sensorLogger;
    /**
     * Represents the trace of the car's movement over time.
     * It provides information about the car's path by storing the series of points
     * the car has traversed and draws the trace in the associated scene.
     * <p>
     * This variable is an instance of the abstract {@link Trace} class, which offers
     * fundamental functionalities like storing points, calculating lengths, and
     * rendering traces. This specific trace may represent different types of paths depending
     * on the subclass of Trace used in the implementation.
     * <p>     * The trace is utilized within the {@link Car} class to track and visualize its movements,
     * enabling better control, monitoring, or debugging of the car's behavior in the scene.
     * <p>     * <strong>Key Functionality:</strong>
     * - Captures points representing the car's trajectory.
     * - Computes the total distance covered by the car.
     * - Provides visual representation of the car's path.
     */
    private final Trace trace;
    /**
     * Represents the current operational status of the car, encapsulated as an
     * {@link AtomicReference} to ensure thread-safe operations in a concurrent
     * environment. The status is initialized to {@code CarStatus.STOPPED} by default.
     * <p>
     * This variable is used to manage and update the car's lifecycle states such as
     * {@code STARTED}, {@code RUNNING}, {@code PAUSED}, {@code STOPPED}, and
     * {@code FINISHED}, ensuring synchronized access across multiple threads
     * operating on the car.
     */
    private final AtomicReference<CarStatus> status = new AtomicReference<>(CarStatus.STOPPED);

    /**
     * Represents a timestamp in milliseconds used to measure or simulate
     * time intervals within the context of car operations.
     * The value is randomly initialized within a specific range
     * and can be used in conjunction with time-based processes
     * such as sensor updates or car movement control logic.
     * <p>     */
    private int currentTimeMillis = random.nextInt(4395, 4403);
    /**
     * Represents the state of the car's reset operation.
     * <p>     * This flag is used to indicate whether the car is currently in the process of
     * being reset or reinitialized. It ensures that related processes or actions
     * do not interfere with the reset operation. When set to true, the car is in
     * the resetting state, and when set to false, the reset process has completed
     * or has not been initiated.
     */
    private boolean resetting = false;


    /**
     * Constructs a new Car object with the provided parent scene, initial position,
     * and heading angle in degrees. The Car is initialized with multiple sensors,
     * logs data to a sensor logger, and starts threads for updating sensor data.
     * <p>     * @param parent The parent Scene object the Car belongs to.
     * @param position The initial position of the Car represented as a Vec3D object.
     * @param headingDegree Initial heading angle of the Car in degrees.
     */
    public Car(Scene parent, Vec3D position, double headingDegree) {
        this.parent = parent;
        trace = parent.getTrace();

        transform.setTranslation(position);
        transform.setRotation(headingDegree);

        //writing the first Lines to the US output.txt
        sensorLogger = new SensorLogger();
        carObservers.add(sensorLogger);
        notifyObservers();

        //Adds all the UltrasonicSensors for the car with degree and position offset from the center of the car part of the Ultrasonic Team
        ultraSonicSensors.add(new UltrasonicSensor(this, "FL", new Vec3D(-45, 110, 0), SENSOR_ANGLE_FL, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FC", new Vec3D(0, 117.5, 0), 0, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "FR", new Vec3D(45, 110, 0), -SENSOR_ANGLE_FR, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RL", new Vec3D(-45, -117.5, 0), -SENSOR_ANGLE_REAR + 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RC", new Vec3D(0, -117.5, 0), 180, parent));
        ultraSonicSensors.add(new UltrasonicSensor(this, "RR", new Vec3D(45, -117.5, 0), SENSOR_ANGLE_REAR + 180, parent));

        // Adds all the InfraredSensors for the Car with the position offset
        infraredSensors.add(new InfraredSensor(this, new Vec3D(10, 90, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(0, 90, 0)));
        infraredSensors.add(new InfraredSensor(this, new Vec3D(-10, 90, 0)));

        //starts the update Thread for the UltrasonicSensors part of the Ultrasonic Team
        new Thread(this::ultrasonicUpdate).start();

        new Thread(this::infraredUpdate).start();

    }

    /**
     * Renders the car and its associated components, such as sensors, onto the
     * provided {@code Graphics} object. This method applies the car's current
     * transformation, draws its bounding box and debug points if enabled, and
     * delegates the drawing of sensors to their respective draw methods.
     * <p>     * @param g The {@code Graphics} context used for rendering the car and its components.
     */
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

    /**
     * Renders the car's sensors within the current scene context. This method iterates
     * through all the car's ultrasonic and infrared sensors and delegates the rendering
     * of each sensor to their respective {@code drawInScene} methods.
     * <p>     * @param g the Graphics context used for rendering the sensors within the scene.
     */
    @Override
    public void drawInScene(Graphics g) {
        ultraSonicSensors.forEach(s -> s.drawInScene(g));
        infraredSensors.forEach(s -> s.drawInScene(g));
    }

    /**
     * Resets the car's position and heading to the specified values, clears sensor data,
     * and reinitializes internal states to prepare for subsequent operations.
     * Notifies observers once the reset is complete.
     * <p>     * @param position The new position of the car represented as a Vec3D object.
     * @param headingDegree The new heading angle of the car in degrees.
     */
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

    /**
     * Sets the car's status to {@code RUNNING}, indicating that the car is
     * currently operational and ready for motion or other processes.
     */
    public void start() {
        status.set(CarStatus.RUNNING);
    }


    /**
     * Sets the car's status to {@code STOPPED}, indicating that the car
     * is no longer in motion or operation. This method is typically used
     * to halt ongoing processes and ensure the car is in a stationary state.
     */
    public void stop() {
        status.set(CarStatus.STOPPED);
    }

    /**
     * Sets the car's current status to {@code PAUSED}.
     * This method is used to temporarily halt the car's operations or motion
     * without fully stopping it, allowing for later resumption.
     */
    public void pause() {
        status.set(CarStatus.PAUSED);
    }

    /**
     * Sets the car's status to {@code FINISHED}, indicating that the process or
     * operation involving the car has been completed. This method updates the
     * internal status field to reflect the final state of the car.
     */
    public void finish() {
        status.set(CarStatus.FINISHED);
    }

    /**
     * @author Infrared-Team
     * Controls the movement of the car based on infrared sensor input.
     * <p>
     * forward() moves the car straight ahead at a fixed speed of 300 mm/s;
     * left() rotates the car left using a random angle between 88–143°/s and turns are intentionally faster than right turns, based on observations;
     * right() rotates the car right using a random angle between 30–57°/s.
     * Every movement is scaled by the time step defined in CHECK_DELAY_MS
     */
    private void forward(){
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        transform.translate(forwardVector().normalize().scale(Settings.CAR.MOVEMENT.SPEED_MM_P_S * dt));
    }

    /**
     * Rotates the car to the left by a random angle, scaled by the time step.
     * <p>     * The rotation angle is determined by generating a random value between 88°/s
     * and 143°/s and is then scaled by the time step calculated from the infrared
     * sensor check delay in milliseconds (CHECK_DELAY_MS). This method simulates
     * the turning of the car based on the infrared sensor input.
     * <p>     * The higher range of rotation angles for left turns supports faster and
     * sharper adjustments compared to right turns, enhancing maneuverability
     * in certain scenarios.
     */
    private void left(){
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        transform.rotate(random.nextDouble(88, 143) * dt);
    }

    /**
     * Rotates the car to the right by a random angle, scaled by a time step.
     * <p>     * The rotation angle is determined by generating a random value between 30°/s
     * and 57°/s. This value is then scaled by the time step, which is derived from
     * the infrared sensor check delay (`CHECK_DELAY_MS`).
     * <p>     * This method simulates the car's rightward turning movement based on the
     * input from the infrared sensor system, facilitating smoother adjustments
     * when navigating.
     */
    private void right(){
        double dt = Settings.CAR.INFRARED.CHECK_DELAY_MS / 1000.0;
        transform.rotate(-random.nextDouble(30, 57) * dt);
    }

    /**
     * Notifies all registered observers of updates to the car's state.
     * <p>     * This method iterates through the list of observers and invokes their
     * {@code onCarUpdate} method, passing a {@link CarUpdateInformation} object
     * containing the relevant updates. The updates include the car's current
     * operational status, sensor measurements, infrared sensor states, position,
     * rotation, and timestamp.
     * <p>     * Observers are typically components that need to react to changes in the car's
     * state, such as simulation components, control systems, or logging utilities.
     * <p>     * The notification mechanism ensures that all observers remain in sync with
     * the car's state at the time the method is called.
     */
    private void notifyObservers() {
        for (ICarObserver observer : carObservers) {
            observer.onCarUpdate(new CarUpdateInformation(status.get(), measurements.stream().toList(), infraredStatus.stream().toList(), getWorldPosition(), transform.getRotation(), currentTimeMillis));
        }
    }

    /**
     * Registers an observer to receive updates regarding the car's state and changes
     * in its operational attributes. The observer must implement the {@link ICarObserver}
     * interface. Observers are notified when the car's state is updated, enabling them
     * to respond to changes such as sensor readings, position updates, or other events.
     * <p>     * If the observer is already registered, it will not be added again.
     * <p>     * @param observer The observer to be added, implementing the {@link ICarObserver} interface.
     */
    public void addObserver(ICarObserver observer) {
        if (!carObservers.contains(observer)) {
            carObservers.add(observer);
        }
    }

    /**
     * Removes the specified observer from the list of observers monitoring the car's state.
     * Once removed, the observer will no longer receive updates or notifications about
     * the car's operations, such as changes in position, sensor readings, or status changes.
     * <p>     * @param observer The observer to be removed, which must implement the {@link ICarObserver} interface.
     */
    public void removeObserver(ICarObserver observer) {
        carObservers.remove(observer);
    }

    /**
     * @author Infrared-Team, Determines the car's movement based on the infrared sensor input.
     * Left, right, forward, or finishes depending on sensor values.
     * <p>
     * @param sensorStatus boolean array representing sensor hits (L, M, R)
     */
    private void getMovementInstructionFromSensors(boolean[] sensorStatus) {
        boolean L = sensorStatus[2];
        boolean M = sensorStatus[1];
        boolean R = sensorStatus[0];

        infraredStatus.clear();
        infraredStatus.add(L);
        infraredStatus.add(M);
        infraredStatus.add(R);

        // If only the left sensor detects the track turn left.
        if (L && !R) {
            left();
        } else if (!L && R) {
            // If only the right sensor detects the track turn right.
            right();
        } else {
            // If only the middle sensor detects the track → drive forward.
            if (M) {
                forward();
            }
            else {
                // If no sensor detects the track stop.
                finish();
            }
        }
    }
    /**
     * Part of Ultrasonic Team
     * <p>
     * Continuously updates the measurements from the ultrasonic sensors.
     * <p>
     * The method runs in an infinite loop and:
     * <p>
     *  Collects measurements from the front and rear ultrasonic sensors</li>
     *  Converts distances from mm to cm</li>
     *  Stops the car if any measured distance is less than 3 cm</li>
     *  Stores all measurements in a unified list</li>
     *  Notifies observers of new data</li>
     * <p>
     * <p>
     * The update frequency is controlled by sleeping for 1/6 of the last timestamp duration
     * between each individual sensor reading to simulate realistic delays.
     */
    private void ultrasonicUpdate() {
        while (true) {
            try {
                // two integer Lists for the Front and Back Sensor Measurements
                List<Integer> measurementsFront = new ArrayList<>();
                List<Integer> measurementsBack = new ArrayList<>();
                //iteration for the TimeStep after each passthrough off all Sensors
                int lastTimeStep = iterateUSTimestamp();

                // Measures the distance to the closest Obstacle in mm for the front 3 Sensors and saves it to the list in cm
                for (int i = 0; i < ultraSonicSensors.size() / 2; i++) {
                    int distance = (ultraSonicSensors.get(i).distanceToClosestObstacle() / 10);
                    measurementsFront.add(distance);
                    //since the calculations are faster than the real life sound waves, the thread is paused for 1/6 of the last added time to the TimeStep
                    Thread.sleep(lastTimeStep / 6);
                }

                //checks if an Obstacle is detected too close and if so, stops the car
                for (Integer distance : measurementsFront) {
                    if (distance < 3)
                        finish();
                }
                // Same as with the front Sensors but for the back Sensors
                for (int i = ultraSonicSensors.size() / 2; i < ultraSonicSensors.size(); i++) {
                    int distance = (ultraSonicSensors.get(i).distanceToClosestObstacle() / 10);
                    measurementsBack.add(distance);

                    Thread.sleep(lastTimeStep / 6);
                }

                for (Integer distance : measurementsBack) {
                    if (distance < 3)
                        finish();
                }
                // adds all the measurements into one list for the Observer Pattern
                measurements.clear();
                measurements.addAll(measurementsFront);
                measurements.addAll(measurementsBack);

                //adds the time off one run through all Sensors to the Timestamp
                currentTimeMillis += lastTimeStep;
                if (resetting) return;
                //notifys the Observer off changes to the measurements
                notifyObservers();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author Infrared-Team,
     * Continuously checks the state of all infrared sensors and updates the car's
     * movement instructions accordingly.
     * <p>
     * After an initial delay, the method repeatedly checks whether each sensor
     * detects the track using isOnTrack(), and passes the resulting
     * sensor states to getMovementInstructionFromSensors().
     * <p>
     * This method runs in its own thread and only executes updates while the
     * car is in CarStatus.RUNNING.
     */
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
    /**
     * Part of the Ultrasonic Team.
     * <p>
     * Generates a semi-random time step to be added to the global timestamp later.
     * This simulates realistic variation in sensor processing time.
     * <p>
     * @return A randomly generated time interval in milliseconds.
     */
    public static int iterateUSTimestamp() {
        return (int)(random.nextGaussian(250, 50)); // 200 - 300
    }
}
