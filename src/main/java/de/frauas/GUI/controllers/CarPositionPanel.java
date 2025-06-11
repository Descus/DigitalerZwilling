package de.frauas.GUI.controllers;

import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarPositionPanel extends JPanel {
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(model);

    private final JTextField intervalField = new JTextField("1000", 5);
    private Timer timer;

    public CarPositionPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(5, 5));
        JLabel title = new JLabel("Track Car");
        add(title, BorderLayout.NORTH);


        // Top: update interval control
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        top.add(new JLabel("Update Interval (ms):"));
        top.add(intervalField);
        JButton setBtn = new JButton("Set");
        top.add(setBtn);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(infoList), BorderLayout.CENTER);

        startTimer(Integer.parseInt(intervalField.getText()));

        // Button action to restart timer
        setBtn.addActionListener(_ -> {
            try {
                int ms = Integer.parseInt(intervalField.getText());
                if (ms <= 0) throw new NumberFormatException();
                axisPanel.getScene().startCar();
                restartTimer(ms);
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

    private void startTimer(int intervalMs) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(intervalMs, _ -> updateInfo());
        timer.setInitialDelay(0);
        timer.start();
        axisPanel.getScene().startCar();
    }

    private void restartTimer(int intervalMs) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(intervalMs, _ -> updateInfo());
        timer.setInitialDelay(0);
        timer.start();
    }

    private void updateInfo() {
        String status = axisPanel.getScene().getCar().getStatus().toString();
        Vec3D pos = axisPanel.getScene().getCar().getWorldPosition(); //TODO let scene return a car information object to get information from
        SimpleDateFormat timeFmt = new SimpleDateFormat("ss:SSS");
        String timestamp = timeFmt.format(new Date());
        String entry = String.format(
                "%s - %s - (%.1f, %.1f)",
                timestamp, status, pos.getX(), pos.getY()
        );
        model.addElement(entry);
        // Scroll to the latest entry
        int last = model.getSize() - 1;
        if (last >= 0) {
            infoList.ensureIndexIsVisible(last);
        }
        // Stop timer when car is stopped or finished
        if (!"Moving".equals(status)) {
            timer.stop();
        }
    }
}