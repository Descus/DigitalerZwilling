package de.frauas.GUI.controllers.output;

import de.frauas.GUI.controllers.NotificationHelper;
import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.ICarObserver;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SensorLoggingPanel displays real-time sensor measurements from the car.
 * It logs ultrasonic distance values from front and rear sensors,
 * timestamped and updated at a configurable interval.
 *
 * @author GUI-Group
 */
public class SensorLoggingPanel extends TitledRoundedPanel implements ICarObserver {
    
    /**
     * Represents a {@link Timer} instance used to schedule periodic actions,
     * such as logging sensor data or updating the user interface.
     *
     * The timer runs at a configurable interval and is responsible for triggering
     * repetitive tasks within the containing class. It can be started, stopped,
     * or restarted dynamically, allowing flexibility in task scheduling.
     *
     * Managed through methods that initialize and control its behavior,
     * the timer is primarily used to invoke updates or logging operations
     * at regular intervals, ensuring timely interaction with the simulation model
     * or other data sources.
     */
    private Timer timer;
    /**
     * Holds the latest {@link CarUpdateInformation} received from the car.
     * This variable is updated whenever new car sensor data is available.
     * It is used for logging sensor updates, which may include the car's
     * status, sensor measurements, position, heading, and timestamp data.
     */
    private CarUpdateInformation latestInfo;
    /**
     * Represents the interval time in milliseconds for periodic actions such as
     * logging sensor data or updates in the associated panel. This value determines
     * the frequency of timer-triggered updates or operations.
     *
     * The default value for this variable is set to 1000 milliseconds (1 second).
     *
     * Used within methods to configure the behavior of the logging timer, such as
     * starting or restarting the timer with a specific interval.
     */
    private int intervalTime = 1000;

    /**
     * A thread-safe data model used to store and manage a list of formatted
     * entries representing periodic state updates of a car.
     *
     * Each entry contains information like timestamps, status, and other
     * sensor data, enabling efficient logging and real-time updates in the UI.
     *
     * This model binds to a {@link JList} component for user-friendly
     * visualization, supporting automatic updates and scrolling to the
     * latest entry whenever new data is added.
     */
    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    /**
     * A graphical list component used to display real-time updates related to the car's
     * position, status, and timestamps.
     * This component is backed by the {@code infoModel}, which serves as its data model
     * and stores a dynamic list of updates. Updates are frequently added and displayed
     * as formatted string entries.
     *
     * The list ensures a scrollable interface for better visualization and automatically
     * scrolls to the most recent entry when new information is appended.
     *
     * This component is part of the SensorLoggingPanel, providing an interface for
     * visualizing logged sensor data in a structured and readable format.
     */
    private final JList<String> infoList = new JList<>(infoModel);
    /**
     * A JTextField instance used to configure the interval time in milliseconds for controlling
     * periodic sensor logging updates.
     *
     * This field is initialized with a default value derived from the `intervalTime` field and
     * is displayed with a column size of 5 for a compact and user-friendly UI. The user can edit
     * this field to specify a new interval value.
     *
     * The value entered in this field is parsed and validated, and upon confirmation, the associated
     * logging timer is restarted with the newly provided interval. If the input is invalid or non-positive,
     * an error notification is displayed, and the field reverts to the previous valid interval value.
     */
    private final JTextField intervalField = new JTextField(String.valueOf(intervalTime), 5);

    /**
     * Constructs a new SensorLoggingPanel and registers it as an observer to the car.
     * Initializes the panel layout and starts the sensor logging timer.
     *
     * @param simulModel the simulation model containing the scene and car
     */
    public SensorLoggingPanel(SimulationModel simulModel) {
        super("Sensor Information",Color.BLACK);
        setLayout(new BorderLayout(5, 5));

        // Top: update interval control
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        top.add(new JLabel("Update Interval (ms):"));
        top.add(intervalField);

        JButton setBtn = new JButton("Set");
        top.add(setBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Start timer
        startLoggingTimer(Integer.parseInt(intervalField.getText()));

        // Button action to restart timer
        setBtn.addActionListener(_ -> {
            try {
                int ms = Integer.parseInt(intervalField.getText());
                if (ms <= 0) {
                    intervalField.setText(String.valueOf(intervalTime));
                    throw new NumberFormatException();
                }
                intervalTime = ms;
                restartLoggingTimer(intervalTime);
            } catch (NumberFormatException ex) {
                NotificationHelper.showError("Please enter a positive integer for the interval.");
            }
        });

        // Register observer
        simulModel.getScene().addObserverToCar(this);
    }

    /**
     * Starts or restarts the sensor logging timer with the given interval.
     *
     * @param intervalMs the interval time in milliseconds
     */
    private void startLoggingTimer(int intervalMs) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(intervalMs, _ -> onSimulationUpdate());
        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Shortcut method to restart the logging timer with a new interval.
     *
     * @param intervalMs interval time in milliseconds
     */
    private void restartLoggingTimer(int intervalMs) {
        startLoggingTimer(intervalMs);
    }


    /**
     * Called by the timer to log the latest ultrasonic sensor data.
     * Includes timestamps and six directional measurements: FL, FM, FR, RL, RM, RR.
     */
    public void onSimulationUpdate() {
        if(latestInfo == null) return;
        SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm:ss:SSS");
        String timestamp = timeFmt.format(new Date());

        String entry = String.format(
                "%s - %s - FL: %d - FM: %d - FR: %d - RL: %d - RM: %d - RR: %d",
                timestamp, latestInfo.getUsTimestamp(),
                latestInfo.getMeasurements().get(0),
                latestInfo.getMeasurements().get(1),
                latestInfo.getMeasurements().get(2),
                latestInfo.getMeasurements().get(3),
                latestInfo.getMeasurements().get(4),
                latestInfo.getMeasurements().get(5)
        );
        infoModel.addElement(entry);

        int last = infoModel.getSize() - 1;
        if (last >= 0) {
            infoList.ensureIndexIsVisible(last);
        }
    }

    /**
     * Receives the latest car sensor data through the observer interface.
     *
     * @param info the updated {@link CarUpdateInformation} with sensor readings
     */
    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        latestInfo = info;
    }
}