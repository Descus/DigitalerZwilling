package de.frauas.GUI.controllers;

import javax.swing.*;

public class InputPanel extends JPanel {
    public InputPanel(AxisPanel axisPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new ObstacleInfoPanel(axisPanel));
        add(new PathInfoPanel(axisPanel));
    }
}
