package de.frauas.GUI.controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarPositionPanel extends JPanel {
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(model);

    private final JTextField intervalField = new JTextField("1000", 5);
    private final JButton setBtn = new JButton("Set");
    private Timer timer;

    public CarPositionPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(5, 5));
        JLabel titel = new JLabel("Track Car");
        add(titel, BorderLayout.NORTH);


        // Top: update interval control
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        top.add(new JLabel("Update Interval (ms):"));
        top.add(intervalField);
        top.add(setBtn);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(infoList), BorderLayout.CENTER);

        startTimer(Integer.parseInt(intervalField.getText()));

        // Button action to restart timer
        setBtn.addActionListener(e -> {
            try {
                int ms = Integer.parseInt(intervalField.getText());
                if (ms <= 0) throw new NumberFormatException();
                axisPanel.startCar();
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
        timer = new Timer(intervalMs, e -> updateInfo());
        timer.setInitialDelay(0);
        timer.start();
        axisPanel.startCar();
    }

    private void restartTimer(int intervalMs) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(intervalMs, e -> updateInfo());
        timer.setInitialDelay(0);
        timer.start();
    }

    private void updateInfo() {
        String status = axisPanel.getCarStatus();
        Point2D.Double pos = axisPanel.getCar().getPositionPoint();
        SimpleDateFormat timeFmt = new SimpleDateFormat("ss:SSS");
        String timestamp = timeFmt.format(new Date());
        String entry = String.format(
                "%s - %s - (%.1f, %.1f)",
                timestamp, status, pos.x, pos.y
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