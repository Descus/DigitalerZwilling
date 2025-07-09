package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    public InputPanel(SimulationModel model) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        add(new TitledRoundedPanel("Car Infomation", Color.BLACK, new CarInfoPanel(model)));
        add(new TitledRoundedPanel("Obstacle Infomation", Color.BLACK, new ObstacleInfoPanel(model)));
        add(new TitledRoundedPanel("Path Information", Color.BLACK, new PathInfoPanel(model)));
    }
}
