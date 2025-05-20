package de.frauas.GUI.controllers;

import de.frauas.GUI.objects.AxisPanel;

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

        startBtn.addActionListener(e -> axisPanel.startCar());
        pauseBtn.addActionListener(e -> axisPanel.pauseCar());
        continueBtn.addActionListener(e -> axisPanel.continueCar());
        stopBtn.addActionListener(e -> axisPanel.stopCar());
        resetBtn.addActionListener(e -> axisPanel.resetCar());

        add(startBtn);
        add(pauseBtn);
        add(continueBtn);
        add(stopBtn);
        add(resetBtn);
    }
}

