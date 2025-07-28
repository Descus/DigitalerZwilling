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

public class SimulationModel {

    @Setter @Getter
    private Scene scene;

    private final List<ISimulationObserver> observers = new ArrayList<>();
    private boolean running = false;
    private long lastTime = System.currentTimeMillis();
    private double delta;

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

    public void addObserver(ISimulationObserver observer) {
        observers.add(observer);

        // new observer for the new car (doesnt work?)
        if (this.scene != null && observer instanceof ICarObserver) {
            this.scene.addObserverToCar((ICarObserver) observer);
        }
    }

    public void removeObserver(ISimulationObserver observer) {
        // Observer beim aktuellen car deabonnieren
        if (this.scene != null && observer instanceof ICarObserver) {
            this.scene.removeObserverFromCar((ICarObserver) observer);
        }
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ISimulationObserver o : observers) {
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

    public void stop() {
        running = false;
    }

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