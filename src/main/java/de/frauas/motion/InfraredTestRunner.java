package de.frauas.motion;

import de.frauas.scenario.components.BezierRoadTrace;
import de.frauas.scenario.components.RoadTrace;
import de.frauas.scenario.primitives.Line2F;
import de.frauas.scenario.primitives.Vec2F;
import de.frauas.scenario.xml.ScenarioXmlFile;
import de.frauas.scenario.xml.dto.Scenario;
import de.frauas.motion.InfraredSensorSimulator;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InfraredTestRunner {

    public static void main(String[] args) {

        try {
            // Szenario aus XML laden
            Scenario scenario = ScenarioXmlFile.fromExample().read();

            // Trace aufbauen
            BezierRoadTrace bezierRoadTrace = new BezierRoadTrace();
            bezierRoadTrace.addPoint(new Vec2F(
                    scenario.getStartPosition().getXPosition(),
                    scenario.getStartPosition().getYPosition()));
            scenario.getTrace().forEach(p -> bezierRoadTrace.addPoint(
                    new Vec2F(p.getXPosition(), p.getYPosition()))
            );


            // Interpolierte Linien extrahieren
            List<Line2F> trace = bezierRoadTrace.getLines();
            // Startposition und Rotation aus Szenario übernehmen
            Vec2F startPos = new Vec2F(900,
                    scenario.getStartPosition().getYPosition());

            double startRot = scenario.getStartPosition().getHeading();

            // Sensor-Simulator und Steuerung starten
            InfraredSensorSimulator sim = new InfraredSensorSimulator(startPos, startRot, trace);
            NavigationController nav = new NavigationController(sim, startPos, startRot);

            // Regelmäßiger Simulationsschritt
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> {
                nav.step();
                Vec2F pos = nav.getCurrentPosition();
                double rot = nav.getCurrentRotationDeg();
                System.out.printf("Neue Position: (%.2f, %.2f), Richtung: %.2f°%n\n", pos.x(), pos.y(), rot);
            }, 0, 100, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
