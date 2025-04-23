package de.frauas;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.scenario.components.Car;
import de.frauas.scenario.components.Obstacle;
import de.frauas.scenario.components.RoadTrace;
import de.frauas.scenario.components.Scene;
import de.frauas.scenario.primitives.Line2;
import de.frauas.scenario.primitives.Vec2;
import de.frauas.scenario.xml.ScenarioXmlFile;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Program {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Frame");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Scene scene = new Scene(new Vec2<>(1000, 500));
        scene.setBackground(Color.gray);
        scene.Add(new Vec2<>(0, 100));
        scene.Add(new Vec2<>(50, 100));
        scene.Add(new Vec2<>(150, 100));
        RoadTrace roadTrace = new RoadTrace();
        roadTrace.add(new Vec2<>(100, 100));
        roadTrace.add(new Vec2<>(100, 200));
        roadTrace.add(new Vec2<>(150, 300));
        roadTrace.add(new Vec2<>(100, 400));
        roadTrace.add(new Vec2<>(200, 400));
        Car car = new Car(new Vec2<>(100, 100), new Vec2<>(0, 0));
        scene.Add(new Obstacle(new Vec2<>(150, 200), new Vec2<>(100, 100), 100));
        scene.Add(roadTrace);
        scene.Add(car);
        frame.getContentPane().add(scene);
        frame.setVisible(true);




    }
}
