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
 *
 * @author GUI-Group
 */
public class ControlPanel extends TitledRoundedPanel implements ICarObserver {

    /**
     * Indicates whether the simulation has been started.
     * This flag is used to track the current execution state of the simulation.
     * It is set to true when the simulation starts and remains false otherwise.
     */
    private boolean started = false;
    /**
     * Indicates whether the simulation is currently paused.
     * This flag is used to control the behavior of the simulation,
     * such as halting updates or animations while paused.
     */
    private boolean paused  = false;
    /**
     * Represents the current working directory used by the ControlPanel class.
     * Initialized to the "Documents/Scenarien" folder within the user's home directory.
     * This directory may be used to read or write simulation-related files.
     */
    private File currentDir = new File(System.getProperty("user.home") + "/Documents/Scenarien");
    /**
     * Stores the most recent state update information about the car, encapsulated
     * within a CarUpdateInformation object. This variable is updated whenever the
     * car's state changes and is used to reflect the latest car-related details,
     * such as status, sensor data, position, and heading. It plays a key role in
     * managing and displaying up-to-date simulation information within the ControlPanel.
     */
    private CarUpdateInformation latestInfo;

    /**
     * Represents the "Start" button used to initiate the simulation in the control panel.
     * This button is created with the text "Start" and is a component of the ControlPanel class.
     * It is used to trigger actions related to starting the simulation when interacted with by the user.
     */
    private final JButton startBtn = new JButton("Start");
    /**
     * Represents a button used to manage the pause state of the simulation.
     * This button toggles the simulation between paused and running states.
     * It is initialized with the label "Pause" and is part of the control panel
     * that allows user interaction with the simulation.
     */
    private final JButton pauseBtn = new JButton("Pause");
    /**
     * A JButton instance that provides options to configure or manage
     * simulation scenarios. The button is labeled as "Scenario Option(s)".
     * It is part of the ControlPanel and facilitates user interaction
     * to select or modify simulation scenarios.
     *
     * This button is initialized when the ControlPanel object is created
     * and is designed to be a final, unchangeable component of the UI.
     */
    private final JButton scenarioBtn = new JButton("Scenario Option(s)");

    /**
     * Constructs a new ControlPanel and wires it to the provided simulation model.
     * Initializes the layout, buttons, and behavior for controlling simulation execution.
     *
     * @param model the simulation model this panel controls
     */
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
                paused = false;
            } else {
                model.reset();
                startBtn.setText("Start");
                pauseBtn.setText("Pause");
                pauseBtn.setEnabled(false);
                started = false;
                paused = false;
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

    /**
     * Reacts to car updates. If the car has finished, disables the pause button.
     *
     * @param info the updated car information
     */
    @Override
    public void onCarUpdate(CarUpdateInformation info) {
        if ("FINISHED".equals(String.valueOf(info.getStatus()))) {
            pauseBtn.setEnabled(false);
        }
    }
}