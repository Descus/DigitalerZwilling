package de.frauas;

import de.frauas.scenario.components.Car;
import de.frauas.scenario.components.Obstacle;
import de.frauas.scenario.components.RoadTrace;
import de.frauas.scenario.components.Scene;
import de.frauas.scenario.primitives.Vec2F;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Program {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Frame");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Scene scene = new Scene(new Vec2F(1000, 500));
        scene.setBackground(Color.gray);
        scene.addDrawable(new Vec2F(0F, 100));
        scene.addDrawable(new Vec2F(50, 100));
        scene.addDrawable(new Vec2F(150, 100));
        RoadTrace roadTrace = new RoadTrace();
        roadTrace.addPoint(new Vec2F(100, 100));
        roadTrace.addPoint(new Vec2F(100, 200));
        roadTrace.addPoint(new Vec2F(150, 300));
        roadTrace.addPoint(new Vec2F(100, 400));
        roadTrace.addPoint(new Vec2F(200, 400));
        Car car = new Car(new Vec2F(100, 100), 0);
        scene.addDrawable(new Obstacle(new Vec2F(150, 200), new Vec2F(100, 100), 100));
        scene.addDrawable(roadTrace);
        scene.addDrawable(car);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        //Try to Achieve 60 FPS
        exec.scheduleAtFixedRate(scene::repaint, 0, (long)((1/60f) * 1000), TimeUnit.MILLISECONDS);
        frame.getContentPane().add(scene);
        frame.setVisible(true);
    }
}
