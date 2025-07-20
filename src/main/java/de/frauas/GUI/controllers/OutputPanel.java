package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.output.CarPositionPanel;
import de.frauas.GUI.controllers.output.SensorLoggingPanel;

import javax.swing.*;
import java.awt.*;

public class OutputPanel extends TitledRoundedPanel {
    public OutputPanel(SimulationModel model) {
        super("Output Panel",Color.orange);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createVerticalStrut(10));
        add(Box.createVerticalStrut(10));
        add(new SensorLoggingPanel(model));
        add(new CarPositionPanel(model));
    }
}