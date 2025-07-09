package de.frauas.GUI.controllers.observer;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.interfaces.ICarObserver;
import de.frauas.scenario.dto.Scenario;
import de.frauas.scenario.xml.ScenarioLoader;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationModel {

    @Setter
    @Getter
    private Scene scene;
    private final List<SimulationObserver> observers = new ArrayList<>();
    private boolean running = false;
    private long lastTime = System.currentTimeMillis();
    double delta;

    public SimulationModel(Scene scene) {
        this.scene = scene;
        Timer timer = new Timer(1000 / Settings.WINDOW.TARGET_FPS, e -> {
            long now = System.currentTimeMillis();
            delta = (now - lastTime) / 1000.0;
            lastTime = now;
            notifyObservers();
            scene.update(delta);
        });

        timer.start();
    }

    public void addObserver(SimulationObserver observer) {
        observers.add(observer);

        // new observer for the new car (doesnt work?)
        if (this.scene != null && observer instanceof ICarObserver) {
            this.scene.addObserverToCar((ICarObserver) observer);
        }
    }

    public void removeObserver(SimulationObserver observer) {
        // Observer beim aktuellen car deabonnieren
        if (this.scene != null && observer instanceof ICarObserver) {
            this.scene.removeObserverFromCar((ICarObserver) observer);
        }
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (SimulationObserver o : observers) {
            o.onSimulationUpdate();
        }
    }

    public void start() {
        running = true;
        scene.startCar();
    }

    public void pause() {
        scene.pauseCar();
        running = false;
    }

    public void resume() {
        scene.resumeCar();
        running = true;
    }

    public void reset() {
        scene.resetCar();
        stop();

        this.delta = 0;
        this.lastTime = System.currentTimeMillis();
        notifyObservers();
    }

    public void reload(String scenarioFile) {
        try {

            if (this.scene != null) {
                for (SimulationObserver observer : observers) {
                    if (observer instanceof ICarObserver) {
                        scene.removeObserverFromCar((ICarObserver) observer);
                    }
                }
            }

            Scenario scenario = ScenarioLoader.loadFromFile(scenarioFile);
            this.scene = new Scene(scenario);


            for (SimulationObserver observer : observers) {
                if (observer instanceof ICarObserver) {
                    scene.addObserverToCar((ICarObserver) observer);
                }
            }

            reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        running = false;
    }
}