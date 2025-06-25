package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.ObstacleInfoPanel;
import de.frauas.GUI.controllers.TitledRoundedPanel;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    public InputPanel(AxisPanel axisPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        add(new TitledRoundedPanel("Car Infomation", Color.BLACK, new CarInfoPanel(axisPanel)));
        add(new TitledRoundedPanel("Obstacle Infomation", Color.BLACK, new ObstacleInfoPanel(axisPanel)));
        add(new TitledRoundedPanel("Path Information", Color.BLACK, new PathInfoPanel(axisPanel)));
    }
}
