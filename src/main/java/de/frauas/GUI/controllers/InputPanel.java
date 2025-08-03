package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.input.CarInfoPanel;
import de.frauas.GUI.controllers.input.ObstacleInfoPanel;
import de.frauas.GUI.controllers.input.PathInfoPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import javax.swing.*;
import java.awt.*;

/**
 * InputPanel is a container panel that groups multiple sub-panels
 * related to the static input data of the simulation. It includes:
 * <ul>
 *     <li>CarInfoPanel – shows car dimensions and initial state</li>
 *     <li>ObstacleInfoPanel – lists all obstacles in the scene</li>
 *     <li>PathInfoPanel – displays trace points and segment distances</li>
 * </ul>
 * The panel uses a vertical layout to stack the input components.
 *
 * @author GUI-Group
 */
public class InputPanel extends TitledRoundedPanel {
    /**
     * Constructs an InputPanel and initializes subcomponents using the given model.
     *
     * @param model the simulation model providing access to the scene and static input data
     */
    public InputPanel(SimulationModel model) {
        super("Input Panel", Color.BLUE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new CarInfoPanel(model));
        add(new ObstacleInfoPanel(model));
        add(new PathInfoPanel(model));
    }
}
