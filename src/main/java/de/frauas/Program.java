package de.frauas;

import de.frauas.scenario.components.*;
import de.frauas.scenario.primitives.Vec2F;
import de.frauas.scenario.xml.dto.Scenario;
import de.frauas.scenario.xml.ScenarioXmlFile;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static de.frauas.Settings.TARGET_FPS;


public class Program {
    public static void main(String[] args) {
        JFrame frame = new JFrame(Settings.TITLE);
        frame.setSize(Settings.WIDTH, Settings.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {

            Scene scene = new Scene(Settings.SCENE_CANVAS);
            Scenario scenario = ScenarioXmlFile.fromExample().read();
            Car car = new Car(
                    new Vec2F(scenario.getStartPosition().getXPosition(),
                             scenario.getStartPosition().getYPosition()),
                    scenario.getStartPosition().getHeading());

            RoadTrace roadTrace = new RoadTrace();
            roadTrace.addPoint(new Vec2F(
                                    scenario.getStartPosition().getXPosition(),
                                    scenario.getStartPosition().getYPosition()));
            scenario.getTrace().forEach(p -> roadTrace.addPoint(new Vec2F(p.getXPosition(), p.getYPosition())));

            BezierRoadTrace bezierRoadTrace = new BezierRoadTrace();
            bezierRoadTrace.addPoint(new Vec2F(
                    scenario.getStartPosition().getXPosition(),
                    scenario.getStartPosition().getYPosition()));
            scenario.getTrace().forEach(p -> bezierRoadTrace.addPoint(new Vec2F(p.getXPosition(), p.getYPosition())));

            //scene.addDrawable(bezierRoadTrace);
            scene.addDrawable(roadTrace);
            scene.addDrawable(car);

            scenario.getObjects().forEach(object -> scene.addDrawable(
                    new Obstacle(
                        new Vec2F(object.getXPositionStart(),
                                object.getYPositionStart()),
                        new Vec2F(object.getXPositionEnd() - object.getXPositionStart(),
                                object.getYPositionEnd() - object.getYPositionStart()),
                            object.getHeight())));
            try (ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor()) {
                //Try to Achieve 60 FPS
                exec.scheduleAtFixedRate(scene::repaint, 0, (long) ((1f / TARGET_FPS) * 1000), TimeUnit.MILLISECONDS);
            }
            frame.getContentPane().add(scene);
        }catch (Exception e){
        }
        frame.setVisible(true);
    }
}
