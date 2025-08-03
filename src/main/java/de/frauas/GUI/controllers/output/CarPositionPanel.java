package de.frauas.GUI.controllers.output;

import de.frauas.GUI.controllers.NotificationHelper;
import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.ICarObserver;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CarPositionPanel displays real-time updates about the car's
 * current position,status, and a timestamp.
 * It logs this data periodically in a list.
 *
 * @author GUI-Group
 */
public class CarPositionPanel extends TitledRoundedPanel implements ICarObserver {

    /**
     * The {@code timer} is a {@link Timer} instance used to schedule periodic updates in the panel.
     * It triggers actions, such as logging or UI updates, at regular intervals based on a configurable time.
     * The interval can be adjusted dynamically during runtime, ensuring flexibility in update frequency.
     * <p>
     * The timer is primarily managed by methods like {@code startLoggingTimer} and {@code restartLoggingTimer},
     * which initialize or reset the timer with a specified interval in milliseconds.
     * <p>
     * When active, the timer invokes periodic updates that interact with the simulation model,
     * logging or processing the latest information from the observed car.
     */
    private Timer timer;
    /**
     * Holds the most recent information about the car's state, represented
     * as an instance of {@link CarUpdateInformation}.
     * This variable is updated whenever a new update is available, providing
     * access to the car's latest position, status, sensor data, and other details.
     * Used as a central reference for displaying and logging the car's current state.
     */
    private CarUpdateInformation latestInfo;
    /**
     * Represents the interval time in milliseconds for periodic actions such as logging
     * or updates in the associated panel. This value determines the frequency at which
     * updates occur (e.g., sensor data logging or position updates).
     * <p>
     * The default value for this variable is set to 1000 milliseconds (1 second).
     * <p>
     * Used by methods to configure and control the timer behavior for periodic functionalities.
     */
    private int intervalTime = 1000;
    /**
     * A thread-safe data model that stores a list of car state updates.
     * <p>
     * This model is used to manage and display the log of formatted entries
     * representing the car's state updates over time, including status, position,
     * and timestamps.
     * <p>
     * The list is populated dynamically, typically during periodic updates triggered
     * by an observer pattern or a timer event. Each update reflects the current car
     * status formatted as a string entry.
     * <p>
     * This model is bound to a JList for display in the UI, enabling real-time
     * visualization of the logged entries.
     */
    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    /**
     * A graphical list component used to display real-time information updates
     * about the car's position and status.
     * <p>
     * This list is backed by the {@code infoModel} data model, which dynamically
     * updates as new information is received from the simulation. It provides a
     * scrollable view for displaying the history of logged positions and statuses.
     * <p>
     * The list ensures the most recent entry is always visible by automatically
     * scrolling to it when a new entry is added to the model.
     */
    private final JList<String> infoList = new JList<>(infoModel);
    /**
     * A JTextField component used to specify the update interval time in milliseconds for sensor logging.
     * <p>
     * This field initializes with the default interval time value (`intervalTime`) and has a fixed column
     * size of 5 for a compact display. The input provided in this field is parsed and validated before
     * restarting the logging timer with the new interval.
     */
    private final JTextField intervalField = new JTextField(String.valueOf(intervalTime), 5);


    /**
     * Constructs a CarPositionPanel which registers itself as a car observer
     * and begins logging car state at a configurable time interval.
     *
     * @param simulModel the simulation model from which the scene and car updates are observed
     */
    public CarPositionPanel(SimulationModel simulModel) {
        super("Time - Status - Position",Color.BLACK);
        setLayout(new BorderLayout(5, 5));

        // Top: update interval control
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        top.add(new JLabel("Update Interval (ms):"));
        top.add(intervalField);

        JButton setBtn = new JButton("Set");
        top.add(setBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Start logging timer with initial interval
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
        simulModel.getScene().addObserverToCar(this);
    }

    /**
     * Starts or restarts the logging timer with the specified interval in milliseconds.
     *
     * @param intervalMs update interval in milliseconds
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
     * Restarts the logging timer. Shortcut for {@link #startLoggingTimer(int)}.
     *
     * @param intervalMs the new interval time in milliseconds
     */
    private void restartLoggingTimer(int intervalMs) {
        startLoggingTimer(intervalMs);
    }

    /**
     * Called periodically by the timer to log the current car state.
     * Adds a formatted entry with timestamp, status, and position.
     */
    public void onSimulationUpdate() {
        if (latestInfo == null) return;

        SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm:ss:SSS");
        String timestamp = timeFmt.format(new Date());

        Vec3D pos = latestInfo.getPosition();
        String entry = String.format(
                "%s - %s - (%.1f, %.1f)",
                timestamp,
                latestInfo.getStatus(),
                pos.getX(),
                pos.getY()

        );

        infoModel.addElement(entry);

        // Auto-scroll the list
        int last = infoModel.getSize() - 1;
        if (last >= 0) {
            infoList.ensureIndexIsVisible(last);
        }
    }
    /**
     * Receives car updates through the observer pattern.
     *
     * @param info the latest CarUpdateInformation containing car position and status
     */
    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        latestInfo = info;
    }
}