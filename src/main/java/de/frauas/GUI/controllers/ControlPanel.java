package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.CarUpdateInformation;
import de.frauas.objects.interfaces.ICarObserver;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * ControlPanel is a panel that contains buttons to control
 * the simulation flow: start/reset, pause/continue, and load scenario files.
 */
public class ControlPanel extends TitledRoundedPanel implements ICarObserver {

    private boolean started = false;
    private boolean paused  = false;
    private File currentDir = new File(System.getProperty("user.home") + "/Documents/Scenarien");
    private CarUpdateInformation latestInfo;

    private final JButton startBtn = new JButton("Start");
    private final JButton pauseBtn = new JButton("Pause");
    private final JButton scenarioBtn = new JButton("Scenario Option(s)");

    public ControlPanel(SimulationModel model) {
        super("Control Panel",Color.GREEN);
        setLayout(new FlowLayout());

        //initial status of the Pause button
        pauseBtn.setEnabled(false);

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

        // Start-Reset logic
        startBtn.addActionListener(e -> {
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

        // Pause-Continue logic
        pauseBtn.addActionListener(e -> {
            if (!paused ) {
                model.pause();
                pauseBtn.setText("Continue");
                paused = true;
            } else {
                model.resume();
                pauseBtn.setText("Pause");
                paused = false;
            }
        });

        // Add buttons to the panel
        add(startBtn);
        add(pauseBtn);
        add(scenarioBtn);
        model.getScene().addObserverToCar(this);
    }


    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        if ("FINISHED".equals(String.valueOf(info.getStatus()))) {
            pauseBtn.setEnabled(false);
        }
    }
}