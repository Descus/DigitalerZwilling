package de.frauas.GUI.controllers.observer;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.scenario.xml.ScenarioLoader;
import lombok.Setter;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationModel {

    @Setter
    private Scene scene;
    private final Timer timer;
    private final List<SimulationObserver> observers = new ArrayList<>();
    private boolean running = false;
    private long lastTime = System.currentTimeMillis();

    public SimulationModel(Scene scene) {
        this.scene = scene;
        timer = new Timer(1000 / Settings.WINDOW.TARGET_FPS, e -> {
            if (running) {
                long now = System.currentTimeMillis();
                double delta = (now - lastTime) / 1000.0;
                lastTime = now;

                scene.update(delta);
                
                notifyObservers();
            }
        });
    }

    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(SimulationObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (SimulationObserver o : observers) {
            o.onSimulationUpdate();
        }
    }

    public void start() {
        running = true;
        timer.start();
    }

    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    public void stop() {
        running = false;
        timer.stop();
    }
}
