package de.frauas.GUI.controllers.output;

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
 */
public class CarPositionPanel extends TitledRoundedPanel implements ICarObserver {

    private Timer timer;
    private CarUpdateInformation latestInfo;

    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(infoModel);
    private final JTextField intervalField = new JTextField("1000", 5);

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
                if (ms <= 0) throw new NumberFormatException();
                restartLoggingTimer(ms);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a positive integer for the interval.",
                        "Invalid Interval",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
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

    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        latestInfo = info;
    }
}