package de.frauas;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.ControlPanel;
import de.frauas.GUI.controllers.InputPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.OutputPanel;
import de.frauas.objects.Scene;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.xml.ScenarioXmlFile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame(Settings.WINDOW.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Settings.WINDOW.WIDTH,Settings.WINDOW.HEIGHT);
        frame.setLayout(new BorderLayout());

        // parsing Data from Xml file into GUI object
        Scenario scenario = null;
        try {
            scenario = ScenarioXmlFile.fromExample().read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(scenario);

        // Create Observer
        SimulationModel model = new SimulationModel(scene);

        // create Panel
        AxisPanel axisPanel = new AxisPanel(model);
        InputPanel infoIn = new InputPanel(model);
        OutputPanel infoOut = new OutputPanel(model);
        ControlPanel controlPanel = new ControlPanel(model);

        //Middle Area
        JPanel middle = new JPanel(new BorderLayout());
        middle.add(controlPanel, BorderLayout.NORTH);
        middle.add(axisPanel, BorderLayout.CENTER);
        frame.add(middle, BorderLayout.CENTER);

        // West Area
        frame.add(infoIn,BorderLayout.WEST);

        //South Area
        frame.add(infoOut,BorderLayout.SOUTH);


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}