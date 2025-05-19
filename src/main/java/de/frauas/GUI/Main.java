package de.frauas.GUI;

import de.frauas.GUI.controllers.InputPanel;
import de.frauas.GUI.controllers.OutputPanel;
import de.frauas.GUI.objects.*;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800,800);
        frame.setLayout(new BorderLayout());

        // create axis
        AxisPanel axisPanel = new AxisPanel();

        // parsing Data from Xml file into GUI object
        XMLScenarioLoader.loadScenario("D:\\Projekt_Uni\\digitalerzwilling\\src\\main\\java\\de\\frauas\\scenario_example.xml",
                axisPanel);

        InputPanel infoIn = new InputPanel(axisPanel);
        OutputPanel infoOut = new OutputPanel(axisPanel);

        frame.add(axisPanel, BorderLayout.CENTER);
        frame.add(infoIn, BorderLayout.WEST);
        frame.add(infoOut, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
         // Ensure path is correct

        frame.add(axisPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        axisPanel.startCar();
    }
}
