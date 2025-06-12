package de.frauas.GUI.controllers;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(AxisPanel axisPanel) {
        setLayout(new FlowLayout());

        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton continueBtn = new JButton("Continue");
        JButton stopBtn = new JButton("Stop");
        JButton resetBtn = new JButton("Reset");

        startBtn.addActionListener(e -> axisPanel.getScene().startCar());
        pauseBtn.addActionListener(e -> axisPanel.getScene().pauseCar());
        continueBtn.addActionListener(e -> axisPanel.getScene().resumeCar());
        stopBtn.addActionListener(e -> axisPanel.getScene().stopCar());
        resetBtn.addActionListener(e -> axisPanel.getScene().resetCar());

        add(startBtn);
        add(pauseBtn);
        add(continueBtn);
        add(stopBtn);
        add(resetBtn);
    }
}

