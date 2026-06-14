package de.frauas.GUI.controllers.observer;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.interfaces.ICarObserver;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.xml.ScenarioXmlFile;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The SimulationModel class represents the core model in the simulation system.
 * It holds the current Scene, manages simulation observers, and controls
 * the simulation lifecycle (start, pause, resume, reset, reload).
 *
 * @author GUI-Group
 */
public class SimulationModel {

    @Setter @Getter
    private Scene scene;

    private final List<ISimulationObserver> observers = new ArrayList<>();
    private boolean running = false;

    /**
     * Constructs a SimulationModel using the given initial scene.
     * Starts a timer that periodically notifies all observers based on the target FPS.
     *
     * @param scene the initial scene to be simulated
     */
    public SimulationModel(Scene scene) {
        this.scene = scene;

        Timer timer = new Timer(1000 / Settings.WINDOW.TARGET_FPS, e -> {
            notifyObservers();
        });

        timer.start();
    }

    /**
     * Adds a new simulation observer. If the observer is a ISimulationObserver,
     * it will also be added to the car object in the scene.
     *
     * @param observer the observer to add
     */
    public void addObserver(ISimulationObserver observer) {
        observers.add(observer);

        if (this.scene != null && observer instanceof ICarObserver) {
            this.scene.addObserverToCar((ICarObserver) observer);
        }
    }

    /**
     * Removes a simulation observer. If it's a ISimulationObserver,
     * it will also be removed from the car object in the scene.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(ISimulationObserver observer) {
        if (this.scene != null && observer instanceof ICarObserver) {
            this.scene.removeObserverFromCar((ICarObserver) observer);
        }
        observers.remove(observer);
    }


    /**
     * Notifies all registered observers of a simulation update.
     */
    private void notifyObservers() {
        for (ISimulationObserver o : observers) {
            o.onSimulationUpdate();
        }
    }

    /**
     * Starts the simulation by starting the car movement.
     */
    public void start() {
        running = true;
        scene.startCar();
    }

    /**
     * Pauses the simulation.
     */
    public void pause() {
        scene.pauseCar();
        running = false;
    }

    /**
     * Resumes the simulation from pause.
     */
    public void resume() {
        scene.resumeCar();
        running = true;
    }

    /**
     * Resets the simulation to the initial state and notifies all observers.
     */
    public void reset() {
        scene.resetCar();
        stop();
        notifyObservers();
    }

    /**
     * Stops the simulation without resetting it.
     */
    public void stop() {
        running = false;
    }

    /**
     * Reloads a new scenario from an XML file path. Car observers are preserved
     * and reattached to the new scene.
     *
     * @param scenarioFile the path to the scenario XML file
     * @throws RuntimeException if the file cannot be read
     */
    public void reload(String scenarioFile) {
        try {
            List<ICarObserver> carObservers = this.scene.getCar().getCarObservers().stream().toList();

            for (ICarObserver observer : carObservers) {
                this.scene.removeObserverFromCar(observer);
            }

            Scenario scenario = ScenarioXmlFile.fromPath(scenarioFile).read();
            this.scene = new Scene(scenario);

            for (ICarObserver observer : carObservers) {
                this.scene.addObserverToCar(observer);
            }

            reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}