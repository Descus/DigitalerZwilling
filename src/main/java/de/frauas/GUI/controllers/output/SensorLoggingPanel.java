package de.frauas.GUI.controllers.output;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.observer.SimulationObserver;
import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SensorLoggingPanel extends JPanel implements SimulationObserver {
    private final Scene scene;
    private CarUpdateInformation carInfo;
    private Timer timer;
    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(infoModel);
    private final JTextField intervalField = new JTextField("1000", 5);

    public SensorLoggingPanel(SimulationModel simulModel, Scene scene) {
        this.scene = scene;
        setLayout(new BorderLayout(5, 5));

        // Top: update interval control
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        top.add(new JLabel("Update Interval (ms):"));
        top.add(intervalField);
        JButton setBtn = new JButton("Set");
        top.add(setBtn);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(infoList), BorderLayout.CENTER);

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


    @Override
    public void onSimulationUpdate() {
       if (scene.getCar() == null || scene.getCar().getCarInfo() == null) return;
        CarUpdateInformation carInfo = scene.getCar().getCarInfo();
        SimpleDateFormat timeFmt = new SimpleDateFormat("ss:SSS");
        String timestamp = timeFmt.format(new Date());

        String entry = String.format(
                "%s - %s - FL: %d - FM: %d - FR: %d - RL: %d - RM: %d - RR: %d",
                carInfo.getUsTimestamp(),timestamp,
                carInfo.getMeasurements().get(0),
                carInfo.getMeasurements().get(1),
                carInfo.getMeasurements().get(2),
                carInfo.getMeasurements().get(3),
                carInfo.getMeasurements().get(4),
                carInfo.getMeasurements().get(5)
        );
        infoModel.addElement(entry);

        int last = infoModel.getSize() - 1;
        if (last >= 0) {
            infoList.ensureIndexIsVisible(last);
        }
    }

}