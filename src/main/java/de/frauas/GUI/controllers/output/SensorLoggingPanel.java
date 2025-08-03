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
    
    private Timer timer;
    private CarUpdateInformation latestInfo;
    private int intervalTime = 1000;

    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(infoModel);
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