package de.frauas.GUI.controllers;

import javax.swing.*;

public class OutputPanel extends JPanel {
    public OutputPanel(AxisPanel axisPanel) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createVerticalStrut(10));
        add(new TrackTimePanel());
        add(Box.createVerticalStrut(10));
        add(new CarPositionPanel(axisPanel));
    }
}