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
 */
public class SensorLoggingPanel extends TitledRoundedPanel implements ICarObserver {
    
    private Timer timer;
    private CarUpdateInformation latestInfo;
    private int intervalTime = 1000;

    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(infoModel);
    private final JTextField intervalField = new JTextField(String.valueOf(intervalTime), 5);

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

    private void startLoggingTimer(int intervalMs) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(intervalMs, _ -> onSimulationUpdate());
        timer.setInitialDelay(0);
        timer.start();
    }

    private void restartLoggingTimer(int intervalMs) {
        startLoggingTimer(intervalMs);
    }
    
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

    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        latestInfo = info;
    }
}