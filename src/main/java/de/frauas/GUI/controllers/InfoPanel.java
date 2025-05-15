package de.frauas.GUI.controllers;

import de.frauas.GUI.objects.*;

import javax.swing.*;

public class InfoPanel extends JPanel {
    public InfoPanel(AxisPanel axisPanel, Car car) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new ObstacleInfoPanel(axisPanel));
        add(Box.createVerticalStrut(10));
        add(new TrackTimePanel());
        add(Box.createVerticalStrut(10));
        add(new CarPositionPanel(axisPanel, car));
    }
}