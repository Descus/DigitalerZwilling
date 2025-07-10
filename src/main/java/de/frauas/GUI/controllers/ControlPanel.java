package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class ControlPanel extends JPanel {

    boolean started = false, paused = false;
    private File currentDir = new File(System.getProperty("user.home") + "/Documents/Scenarien");

    public ControlPanel(SimulationModel model) {
        setLayout(new FlowLayout());

        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton scenarioBtn = new JButton("ScenarioOption");

        // path list of our default scenarios
        // shows a pop-up menu in which you can switch the scene
        scenarioBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(currentDir);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                currentDir = selectedFile.getParentFile();

                model.reload(selectedFile.getAbsolutePath());

                startBtn.setText("Start");
                pauseBtn.setText("Pause");
                pauseBtn.setEnabled(false);
                started = false;
                paused = false;
            }
        });
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
        pauseBtn.addActionListener(_ -> {
            if (!paused) {
                model.pause();
                pauseBtn.setText("Continue");
                paused = true;
            } else {
                model.resume();
                pauseBtn.setText("Pause");
                paused = false;
            }
        });
        add(startBtn);
        add(pauseBtn);
        add(scenarioBtn);
    }
}