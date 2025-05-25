package de.frauas.motion;

import de.frauas.scenario.primitives.Vec2F;
import de.frauas.scenario.primitives.Line2F;

import java.util.ArrayList;
import java.util.List;

public class InfraredSensorSimulator {

    // Sensorpositionen relativ zur Fahrzeugmitte (in cm)
    private static final Vec2F SENSOR_LEFT = new Vec2F(-1.3f, 0f);
    //y normaly at -4,35f
    private static final Vec2F SENSOR_CENTER = new Vec2F(0.0f, 0f);
    private static final Vec2F SENSOR_RIGHT = new Vec2F(1.3f, 0f);

    private Vec2F carPosition;
    private double carRotationDeg;
    private List<Vec2F[]> traceSegments; // jedes Element ist ein Paar [left, right]

    public InfraredSensorSimulator(Vec2F carPosition, double carRotationDeg, List<Line2F> lineSegments) {
        this.carPosition = carPosition;
        this.carRotationDeg = carRotationDeg;
        this.traceSegments = generateEdgeSegments(lineSegments);
    }

    public int[] getSensorStates() {
        int[] result = new int[3];
        Vec2F[] sensors = { SENSOR_LEFT, SENSOR_CENTER, SENSOR_RIGHT };

        for (int i = 0; i < 3; i++) {
            Vec2F globalPos = rotateAndTranslate(sensors[i]);
            System.out.print(globalPos);
            System.out.println();
            result[i] = isWithinLine(globalPos) ? 1 : 0;
        }

        return result;
    }

    private Vec2F rotateAndTranslate(Vec2F local) {
        double angleRad = Math.toRadians(carRotationDeg);
        double cosA = Math.cos(angleRad);
        double sinA = Math.sin(angleRad);

        float xRot = (float)(local.x() * cosA - local.y() * sinA);
        float yRot = (float)(local.x() * sinA + local.y() * cosA);

        return new Vec2F(carPosition.x() + xRot, carPosition.y() + yRot);
    }

    private boolean isWithinLine(Vec2F sensorGlobal) {
        for (Vec2F[] edgePair : traceSegments) {
            Vec2F left = edgePair[0];
            Vec2F right = edgePair[1];

            // Vektor von left nach right
            float dx = right.x() - left.x();
            float dy = right.y() - left.y();

            // Vektor von left zum Sensorpunkt
            float px = sensorGlobal.x() - left.x();
            float py = sensorGlobal.y() - left.y();

            // Skalarprodukt (Projektion entlang der Linie)
            float dot = px * dx + py * dy;
            float lenSq = dx * dx + dy * dy;

            // Liegt Projektion auf Linie? (also innerhalb des Segments)
            if (dot < 0 || dot > lenSq) continue;

            // Kreuzprodukt bestimmt Abstand zur Linie (nicht entlang der Linie!)
            float cross = dx * py - dy * px;
            float distance = Math.abs(cross) / (float) Math.sqrt(lenSq);

            // Wenn Abstand zur Linie klein genug ist => Punkt liegt im Streifen
            if (distance <= 0.5f) return true;
        }
        return false;
    }



    private double pointToSegmentDistance(Vec2F p, Vec2F a, Vec2F b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();

        if (dx == 0 && dy == 0) return Math.hypot(p.x() - a.x(), p.y() - a.y());

        double t = ((p.x() - a.x()) * dx + (p.y() - a.y()) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        double projX = a.x() + t * dx;
        double projY = a.y() + t * dy;

        return Math.hypot(p.x() - projX, p.y() - projY);
    }

    public void updateCarState(Vec2F position, double rotationDeg) {
        this.carPosition = position;
        this.carRotationDeg = rotationDeg;
    }

    private List<Vec2F[]> generateEdgeSegments(List<Line2F> lineSegments) {
        List<Vec2F[]> segments = new ArrayList<>();
        for (Line2F line : lineSegments) {
            Vec2F a = line.a();
            Vec2F b = line.b();

            double dx = b.x() - a.x();
            double dy = b.y() - a.y();
            double length = Math.hypot(dx, dy);
            if (length == 0) continue;

            // Normale berechnen
            double nx = -dy / length;
            double ny = dx / length;

            // ±1.5 cm versetzt zum Mittelpunkt a
            Vec2F left = new Vec2F((float)(a.x() + nx * 1.5), (float)(a.y() + ny * 1.5));
            Vec2F right = new Vec2F((float)(a.x() - nx * 1.5), (float)(a.y() - ny * 1.5));

            segments.add(new Vec2F[] { left, right });
        }
        return segments;
    }
}