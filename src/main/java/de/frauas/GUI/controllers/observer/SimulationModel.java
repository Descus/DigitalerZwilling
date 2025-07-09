package de.frauas.GUI.controllers.observer;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import lombok.Getter;
import lombok.Setter;


import javax.swing.*;
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
            delta += (now - lastTime) / 1000.0;
            lastTime = now;
            notifyObservers();
        });

        Timer timer1 = new Timer(1000, e -> {
            if (!running) return;
            scene.update(delta);
            delta = 0;
        });
        
        timer.start();
        timer1.start();
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
    
    public void reset(){
        scene.resetCar();
        stop();
    }

    public void stop() {
        running = false;
    }
}
