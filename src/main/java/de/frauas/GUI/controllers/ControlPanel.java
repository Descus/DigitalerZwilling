package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.Scene;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    boolean started = false, paused = false;
    public ControlPanel(SimulationModel model, Scene scene) {
        setLayout(new FlowLayout());

        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");

        //initial status of the button
        pauseBtn.setEnabled(false);

        startBtn.addActionListener(_ -> {
            if (!started) {
                scene.startCar();
                startBtn.setText("Reset");
                pauseBtn.setEnabled(true);
                started = true;
            } else {
                scene.resetCar();
                startBtn.setText("Start");
                pauseBtn.setEnabled(false);
                started = false;
            }
            
        });
        pauseBtn.addActionListener(_ ->{
            if(!paused) {
                scene.pauseCar();
                pauseBtn.setText("Continue");
                paused = true;
            } else {
                scene.resumeCar();
                pauseBtn.setText("Pause");
                paused = false;
            }
        }) ;
        add(startBtn);
        add(pauseBtn);
    }
}

