package de.frauas;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.ControlPanel;
import de.frauas.GUI.controllers.InputPanel;
import de.frauas.GUI.controllers.OutputPanel;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.xml.ScenarioXmlFile;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame(Settings.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Settings.WIDTH,Settings.HEIGHT);
        frame.setLayout(new BorderLayout());

        // create axis
        AxisPanel axisPanel = new AxisPanel();

        // parsing Data from Xml file into GUI object
        try {
            Scenario scenario = ScenarioXmlFile.fromExample().read();
            axisPanel.populate(scenario);
        }catch (Exception e){
            e.printStackTrace();
        }
        
        InputPanel infoIn = new InputPanel(axisPanel);
        OutputPanel infoOut = new OutputPanel(axisPanel);
        ControlPanel controlPanel = new ControlPanel(axisPanel);

        // Add to frame
        frame.add(axisPanel, BorderLayout.CENTER);
        frame.add(infoIn, BorderLayout.WEST);
        frame.add(infoOut, BorderLayout.SOUTH);
        frame.add(controlPanel, BorderLayout.NORTH); // <- add buttons at top
        // Final setup
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
         // Ensure path is correct

        frame.add(axisPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // axisPanel.startCar(); the "Start" button will handle that now.
    }
}
