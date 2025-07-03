package de.frauas.GUI.controllers.output;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.observer.SimulationObserver;
import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarPositionPanel extends JPanel implements SimulationObserver {
    private final Scene scene;
    private Timer timer;
    private final DefaultListModel<String> infoModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(infoModel);
    private final JTextField intervalField = new JTextField("1000", 5);

    public CarPositionPanel(SimulationModel simulModel, Scene scene) {
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
        String status = scene.getCar().getStatus().toString();
        Vec3D pos = scene.getCar().getTransform().getTranslation(); //TODO let scene return a car information object to get information from
        SimpleDateFormat timeFmt = new SimpleDateFormat("ss:SSS");
        String timestamp = timeFmt.format(new Date());
        String entry = String.format(
                "%s - %s - (%.1f, %.1f)",
                timestamp, status, pos.getX(), pos.getY()
        );
        infoModel.addElement(entry);
        // Scroll to the latest entry
        int last = infoModel.getSize() - 1;
        if (last >= 0) {
            infoList.ensureIndexIsVisible(last);
        }
    }
}