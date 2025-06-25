package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.Scene;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(SimulationModel model, Scene scene) {
        setLayout(new FlowLayout());

        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton continueBtn = new JButton("Continue");
        JButton resetBtn = new JButton("Reset");

        //initial status of the button
        pauseBtn.setEnabled(false);
        continueBtn.setEnabled(false);

        startBtn.addActionListener(e -> {
            scene.startCar();
            pauseBtn.setEnabled(true);
            startBtn.setEnabled(false);
        });
        pauseBtn.addActionListener(e ->{
            scene.pauseCar();
            pauseBtn.setEnabled(false);
            continueBtn.setEnabled(true);
        }) ;
        continueBtn.addActionListener(e -> {
            scene.resumeCar();
            pauseBtn.setEnabled(true);
            continueBtn.setEnabled(false);
        });
        resetBtn.addActionListener(e -> scene.resetCar());

        add(startBtn);
        add(pauseBtn);
        add(continueBtn);
        add(resetBtn);
    }
}

