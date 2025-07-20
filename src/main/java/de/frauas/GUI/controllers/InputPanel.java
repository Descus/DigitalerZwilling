package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.input.CarInfoPanel;
import de.frauas.GUI.controllers.input.ObstacleInfoPanel;
import de.frauas.GUI.controllers.input.PathInfoPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import javax.swing.*;
import java.awt.*;

public class InputPanel extends TitledRoundedPanel {
    public InputPanel(SimulationModel model) {
        super("Input Panel", Color.BLUE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new CarInfoPanel(model));
        add(new ObstacleInfoPanel(model));
        add(new PathInfoPanel(model));
    }
}
