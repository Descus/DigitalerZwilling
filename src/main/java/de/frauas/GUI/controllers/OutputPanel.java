package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.output.CarPositionPanel;
import de.frauas.GUI.controllers.output.SensorLoggingPanel;

import javax.swing.*;
import java.awt.*;

/**
 * OutputPanel is a container panel that displays real-time simulation output.
 * It includes sub-panels such as:
 * <ul>
 *     <li>SensorLoggingPanel – logs ultrasonic sensor measurements</li>
 *     <li>CarPositionPanel – logs timestamped position and status of the car</li>
 * </ul>
 * The layout is horizontal, stacking the output components side by side.
 *
 * @author GUI-Group
 */
public class OutputPanel extends TitledRoundedPanel {
    /**
     * Constructs a new OutputPanel using the given simulation model.
     * Initializes and adds the sensor and position sub-panels.
     *
     * @param model the simulation model providing real-time car data
     */
    public OutputPanel(SimulationModel model) {
        super("Output Panel",Color.orange);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createVerticalStrut(10));
        add(Box.createVerticalStrut(10));
        add(new SensorLoggingPanel(model));
        add(new CarPositionPanel(model));
    }
}