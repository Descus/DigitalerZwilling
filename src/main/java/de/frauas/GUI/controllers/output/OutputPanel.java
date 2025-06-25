package de.frauas.GUI.controllers.output;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.output.CarPositionPanel;
import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.observer.SimulationObserver;
import de.frauas.objects.Scene;

import javax.swing.*;
import java.awt.*;

public class OutputPanel extends JPanel implements SimulationObserver {
    public OutputPanel(SimulationModel model, Scene scene) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createVerticalStrut(10));
        add(Box.createVerticalStrut(10));
        add(new TitledRoundedPanel("Sensor Information",Color.BLACK,new JPanel()));
        add(new TitledRoundedPanel("Time-Status-Position", Color.BLACK,new CarPositionPanel(model, scene)));
    }

    @Override
    public void onSimulationUpdate() {

    }
}