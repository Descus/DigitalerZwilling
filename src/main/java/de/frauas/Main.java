package de.frauas;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.ControlPanel;
import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.input.InputPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.output.OutputPanel;
import de.frauas.objects.Scene;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.xml.ScenarioXmlFile;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame(Settings.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Settings.WIDTH,Settings.HEIGHT);
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
        AxisPanel axisPanel = new AxisPanel(model,scene);
        InputPanel infoIn = new InputPanel(axisPanel);
        OutputPanel infoOut = new OutputPanel(model,scene);
        ControlPanel controlPanel = new ControlPanel(model, scene);

        //Middle Area
        //Axis Panel
        TitledRoundedPanel axisArea = new TitledRoundedPanel(
                "Axis Panel",
                Color.RED,
                axisPanel
        );

        //Control Panel
        TitledRoundedPanel ctrArea = new TitledRoundedPanel(
                "Control Panel",
                Color.GREEN,
                controlPanel
        );

        JPanel middle = new JPanel(new BorderLayout());
        middle.add(ctrArea, BorderLayout.NORTH);
        middle.add(axisArea, BorderLayout.CENTER);
        frame.add(middle, BorderLayout.CENTER);

        // Input Panel
        TitledRoundedPanel inputArea = new TitledRoundedPanel(
                "Input Panel",
                Color.BLUE,
                infoIn
        );
        frame.add(inputArea,BorderLayout.WEST);

        //Output Panel
        TitledRoundedPanel outputArea = new TitledRoundedPanel(
                "Output Panel",
                Color.ORANGE,
                infoOut
        );
        frame.add(outputArea,BorderLayout.SOUTH);



        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        model.start();
        // axisPanel.startCar(); the "Start" button will handle that now.
    }
}