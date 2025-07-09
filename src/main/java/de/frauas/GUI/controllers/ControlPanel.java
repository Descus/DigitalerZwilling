package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    boolean started = false, paused = false;
    public ControlPanel(SimulationModel model) {
        setLayout(new FlowLayout());

        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
//        JButton scenarioBtn = new JButton("ScenarioOption");
//
//        // path list of our example scenarios
//        File scenariosDir = new File("src/main/resources/Scenario");
//        String[] choices = scenariosDir.list((dir, name) -> name.toLowerCase().endsWith(".xml"));
//
//        // shows a pop-up menu in which you can switch the scene
//        scenarioBtn.addActionListener(e -> {
//            String input = (String) JOptionPane.showInputDialog(this, "choose a scene",
//                    "Scene", JOptionPane.QUESTION_MESSAGE, null,
//                    choices,
//                    Settings.SCENARIO_FILE.substring(Settings.SCENARIO_FILE.lastIndexOf("/")+1));
//            if (input != null) {
//                //Settings.SCENARIO_FILE = "Scenario/" + input;
//                model.loadNewScene("Scenario/" + input);
//
//                startBtn.setText("Start");
//                pauseBtn.setText("Pause");
//                pauseBtn.setEnabled(false);
//                started = false;
//                paused = false;
//            }
//        });

        //initial status of the button
        pauseBtn.setEnabled(false);

        startBtn.addActionListener(_ -> {
            if (!started) {
                model.start();
                startBtn.setText("Reset");
                pauseBtn.setEnabled(true);
                started = true;
            } else {
                model.reset();
                startBtn.setText("Start");
                pauseBtn.setEnabled(false);
                started = false;
            }
            
        });
        pauseBtn.addActionListener(_ ->{
            if(!paused) {
                model.pause();
                pauseBtn.setText("Continue");
                paused = true;
            } else {
                model.resume();
                pauseBtn.setText("Pause");
                paused = false;
            }
        }) ;
        add(startBtn);
        add(pauseBtn);

    }
}

